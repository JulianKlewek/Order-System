package com.klewek.productservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
@JsonSerialize
public record ProductResponseDto(String name, String description, BigDecimal price, ProductStatus status) {
}
