package com.klewek.orderservice.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderRequestDto(List<OrderLineItemDto> orderLineItemDtoList) {
}
