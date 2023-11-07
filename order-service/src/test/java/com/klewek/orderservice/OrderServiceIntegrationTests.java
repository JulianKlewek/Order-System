package com.klewek.orderservice;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.klewek.orderservice.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderServiceIntegrationTests extends OrderTestsConfiguration {

    @Test
    @DisplayName("Should return response with status CREATED")
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
                .withQueryParam("skuCodes", WireMock.equalTo("iphone_14_blue"))
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
}
