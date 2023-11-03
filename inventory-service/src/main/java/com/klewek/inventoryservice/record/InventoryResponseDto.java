package com.klewek.inventoryservice.record;

import lombok.Builder;

@Builder
public record InventoryResponseDto(String skuCode, int quantity) {
}
