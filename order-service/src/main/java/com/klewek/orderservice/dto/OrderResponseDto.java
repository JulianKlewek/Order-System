package com.klewek.orderservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderResponseDto(OrderStatus status, String orderNumber, List<OrderLineItemDto> orderLineItemsList) {
}
