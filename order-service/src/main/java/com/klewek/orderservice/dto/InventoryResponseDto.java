package com.klewek.orderservice.dto;

import lombok.Builder;

@Builder
public record InventoryResponseDto(String skuCode, int quantity){

}
