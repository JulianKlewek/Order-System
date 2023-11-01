package com.klewek.orderservice.mapper;

import com.klewek.orderservice.dto.OrderLineItemDto;
import com.klewek.orderservice.model.OrderLineItem;

public class OrderLineItemMapper {

    public static OrderLineItem toEntity(OrderLineItemDto dto){
        return OrderLineItem.builder()
                .skuCode(dto.skuCode())
                .price(dto.price())
                .quantity(dto.quantity())
                .build();
    }
}
