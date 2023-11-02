package com.klewek.orderservice.mapper;

import com.klewek.orderservice.dto.InventoryResponseDto;
import com.klewek.orderservice.model.OrderLineItem;

import java.util.List;

public class OrderedProductInventoryMapper {

    public static InventoryResponseDto toDto(OrderLineItem item){
        return InventoryResponseDto.builder()
                .skuCode(item.getSkuCode())
                .quantity(item.getQuantity())
                .build();
    }
    public static List<InventoryResponseDto> listToDtoList(List<OrderLineItem> items){
        return items.stream()
                .map(OrderedProductInventoryMapper::toDto)
                .toList();
    }
}
