package com.klewek.orderservice.service;

import com.klewek.orderservice.dto.OrderRequestDto;
import com.klewek.orderservice.dto.OrderResponseDto;
import com.klewek.orderservice.dto.OrderedProductDto;
import com.klewek.orderservice.event.OrderPlacedEvent;
import com.klewek.orderservice.mapper.OrderLineItemMapper;
import com.klewek.orderservice.model.Order;
import com.klewek.orderservice.model.OrderLineItem;
import com.klewek.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.klewek.orderservice.mapper.OrderMapper.toDto;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNullElseGet;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    private static final String SERVICE_NAME = "order-service";
    private static final String FALLBACK_METHOD = "orderNotPlacedInformation";


    @CircuitBreaker(name = SERVICE_NAME, fallbackMethod = FALLBACK_METHOD)
    public OrderResponseDto placeOrder(OrderRequestDto orderRequest) {
        List<OrderLineItem> orderedItems = orderRequest.orderLineItemDtoList()
                .stream()
                .map(OrderLineItemMapper::toEntity)
                .sorted(comparing(OrderLineItem::getSkuCode))
                .toList();
        List<OrderedProductDto> missingProducts = findNotAvailableProducts(orderedItems);
        if (!missingProducts.isEmpty()) {
            throw new IllegalArgumentException("Not enough product quantity.");
        }
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(orderedItems)
                .build();
        orderRepository.save(order);
        kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
        return toDto(order);
    }

    private List<OrderedProductDto> findNotAvailableProducts(List<OrderLineItem> orderedItems) {
        List<String> skuCodes = orderedItems
                .stream()
                .map(OrderLineItem::getSkuCode)
                .toList();
        List<OrderedProductDto> orderedProductsInventoryList = getAvailableProductsQuantityFromInventoryService(skuCodes);
        return createMissingProductsList(orderedItems, orderedProductsInventoryList);
    }

    private List<OrderedProductDto> getAvailableProductsQuantityFromInventoryService(List<String> skuCodes) {
        List<OrderedProductDto> orderedProductDtoList;
        orderedProductDtoList = webClientBuilder.build()
                .get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes)
                                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<OrderedProductDto>>() {
                })
                .block();
        return requireNonNullElseGet(orderedProductDtoList, ArrayList::new);
    }

    private List<OrderedProductDto> createMissingProductsList(List<OrderLineItem> orderedItems,
                                                              List<OrderedProductDto> availableItems) {
        availableItems.sort(comparing(OrderedProductDto::skuCode));
        List<OrderedProductDto> missingProductsList = new ArrayList<>();
        for (int i = 0; i < orderedItems.size(); i++) {
            if (orderedItems.get(i).getQuantity() > availableItems.get(i).quantity()) {
                missingProductsList.add(availableItems.get(i));
            }
        }
        return missingProductsList;
    }

    /**
     * @param orderRequest
     * @param exc
     * @return false
     * <p>
     * FALLBACK_METHOD
     */
    public boolean orderNotPlacedInformation(OrderRequestDto orderRequest, Exception exc) {
        return false;
    }
}
