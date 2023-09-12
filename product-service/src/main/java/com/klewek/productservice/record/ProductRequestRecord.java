package com.klewek.productservice.record;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

@JsonSerialize
public record ProductRequestRecord(String name, String description, BigDecimal price) {
}
