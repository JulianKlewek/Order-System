package com.klewek.productservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.math.BigDecimal;

@JsonSerialize
@Builder
public record ProductRequestDto(
        @NotEmpty(message = "Name can not be null or empty") String name,
        @NotEmpty(message = "Description can not be null or empty") String description,
        @DecimalMin(message = "Price must be higher than 0", value = "0.0", inclusive = false) BigDecimal price) {
}
