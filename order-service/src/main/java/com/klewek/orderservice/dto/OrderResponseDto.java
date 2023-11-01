package com.klewek.orderservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderResponseDto(String orderNumber, List<OrderLineItemDto> orderLineItemsList) {
}
