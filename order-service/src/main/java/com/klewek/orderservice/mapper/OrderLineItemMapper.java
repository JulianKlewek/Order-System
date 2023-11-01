package com.klewek.orderservice.mapper;

import com.klewek.orderservice.dto.OrderLineItemDto;
import com.klewek.orderservice.model.OrderLineItem;

import java.util.List;

public class OrderLineItemMapper {

    public static OrderLineItem toEntity(OrderLineItemDto dto){
        return OrderLineItem.builder()
                .skuCode(dto.skuCode())
                .price(dto.price())
                .quantity(dto.quantity())
                .build();
    }

    public static OrderLineItemDto toDto(OrderLineItem item){
        return OrderLineItemDto.builder()
                .skuCode(item.getSkuCode())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .build();
    }
    public static List<OrderLineItemDto> listToDtoList(List<OrderLineItem> items){
        return items.stream()
                .map(OrderLineItemMapper::toDto)
                .toList();
    }
}
