package com.klewek.inventoryservice.mapper;

import com.klewek.inventoryservice.model.Inventory;
import com.klewek.inventoryservice.record.InventoryResponseDto;

import java.util.List;

public class InventoryResponseMapper {
    public static InventoryResponseDto toDto(Inventory inventory){
        return InventoryResponseDto.builder()
                .skuCode(inventory.getSkuCode())
                .quantity(inventory.getQuantity())
                .build();
    }
    public static List<InventoryResponseDto> listToDtoList(List<Inventory> items){
        return items.stream()
                .map(InventoryResponseMapper::toDto)
                .toList();
    }
}