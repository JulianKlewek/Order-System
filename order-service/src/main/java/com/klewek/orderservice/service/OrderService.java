package com.klewek.orderservice.service;

import com.klewek.orderservice.event.OrderPlacedEvent;
import com.klewek.orderservice.model.Order;
import com.klewek.orderservice.model.OrderLineItem;
import com.klewek.orderservice.dto.OrderedProductDto;
import com.klewek.orderservice.dto.OrderLineItemDto;
import com.klewek.orderservice.dto.OrderRequestDto;
import com.klewek.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

import static java.util.Comparator.comparing;

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
    public boolean placeOrder(OrderRequestDto orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> orderedItems = orderRequest.itemRecordList()
                .stream()
                .map(this::mapRecordToEntity)
                .sorted(comparing(OrderLineItem::getSkuCode))
                .toList();

        order.setOrderLineItemsList(orderedItems);

        List<String> skuCodes = getSkuCodes(orderedItems);

        List<OrderedProductDto> orderedProductsInventoryList = getOrderedProductsQuantity(skuCodes);
        orderedProductsInventoryList.sort(comparing(OrderedProductDto::skuCode));

        List<OrderedProductDto> missingProductsList = findMissingProductsList(orderedItems, orderedProductsInventoryList);

        if (missingProductsList.isEmpty()) {
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return true;
        } else {
            throw new IllegalArgumentException("Not enough product quantity.");
        }
    }

    private List<String> getSkuCodes(List<OrderLineItem> orderedItems) {
        return orderedItems
                .stream()
                .map(OrderLineItem::getSkuCode)
                .toList();
    }

    private List<OrderedProductDto> findMissingProductsList(List<OrderLineItem> orderedItems,
                                                            List<OrderedProductDto> orderedProductsInventoryList) {
        List<OrderedProductDto> missingProductsList = new ArrayList<>();

        for (int i = 0; i < orderedItems.size(); i++) {
            if (orderedItems.get(i).getQuantity() > orderedProductsInventoryList.get(i).quantity()) {
                missingProductsList.add(orderedProductsInventoryList.get(i));
            }
        }

        return missingProductsList;
    }

    private List<OrderedProductDto> getOrderedProductsQuantity(List<String> skuCodes) {
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

        return Objects.requireNonNullElseGet(orderedProductDtoList, ArrayList::new);
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

    private OrderLineItem mapRecordToEntity(OrderLineItemDto orderLineItemDto) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSkuCode(orderLineItemDto.skuCode());
        orderLineItem.setPrice(orderLineItemDto.price());
        orderLineItem.setQuantity(orderLineItemDto.quantity());

        return orderLineItem;
    }
}
