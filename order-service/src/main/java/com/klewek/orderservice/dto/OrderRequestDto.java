package com.klewek.orderservice.dto;

import java.util.List;

public record OrderRequestDto(List<OrderLineItemDto> orderLineItemDtoList) {
}
