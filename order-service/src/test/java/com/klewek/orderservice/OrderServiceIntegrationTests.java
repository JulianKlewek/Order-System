package com.klewek.orderservice;

import com.klewek.orderservice.dto.*;
import com.klewek.orderservice.exception.NoAvailableProductsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceIntegrationTests extends OrderTestsConfiguration {

    @Test
    @DisplayName("Should place order with status CREATED")
    void should_return_dto_with_status_CREATED() throws Exception {
        //given
        OrderLineItemDto product1 = OrderLineItemDto.builder()
                .skuCode("iphone_14_blue")
                .price(BigDecimal.TEN)
                .quantity(10)
                .build();
        List<OrderLineItemDto> orderLineItems = List.of(product1);
        InventoryResponseDto invResponse = InventoryResponseDto.builder()
                .quantity(100)
                .skuCode("iphone_14_blue")
                .build();
        String responseBody = objectMapper.writeValueAsString(List.of(invResponse));
        wireMockServer.stubFor(get(urlPathEqualTo("/api/inventory"))
                .withQueryParam("skuCodes", equalTo("iphone_14_blue"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "*/*")
                        .withBody(responseBody)
                )
        );
        //when
        OrderResponseDto response = orderService.placeOrder(new OrderRequestDto(List.of(product1)));
        //then
        assertAll(() -> {
            assertEquals(OrderStatus.CREATED, response.status());
            assertEquals(orderLineItems, response.orderLineItemsList());
        });
    }

    @Test
    @DisplayName("Should not place order and throw NoAvailableProductsException")
    void should_throw_NoAvailableProductsException() throws Exception {
        //given
        String expectedMessage = "Inventory does not contain sufficient quantity of products.";
        OrderLineItemDto requestProduct1 = OrderLineItemDto.builder()
                .skuCode("iphone_14_blue")
                .price(BigDecimal.TEN)
                .quantity(10)
                .build();
        OrderLineItemDto requestProduct2 = OrderLineItemDto.builder()
                .skuCode("iphone_14_black")
                .price(BigDecimal.TEN)
                .quantity(10)
                .build();
        InventoryResponseDto invResponse1 = InventoryResponseDto.builder()
                .quantity(1)
                .skuCode("iphone_14_blue")
                .build();
        InventoryResponseDto invResponse2 = InventoryResponseDto.builder()
                .quantity(100)
                .skuCode("iphone_14_black")
                .build();
        String responseBody = objectMapper.writeValueAsString(List.of(invResponse1, invResponse2));
        wireMockServer.stubFor(get(urlPathEqualTo("/api/inventory"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "*/*")
                        .withBody(responseBody)
                )
        );
        //when&then
        Exception exception = assertThrows(
                NoAvailableProductsException.class, () ->
                        orderService.placeOrder(new OrderRequestDto(List.of(requestProduct1, requestProduct2)))
        );
        assertEquals(exception.getMessage(), expectedMessage);
    }
}
