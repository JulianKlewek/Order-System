package com.klewek.productservice.record;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.klewek.productservice.model.Product;

import java.math.BigDecimal;

@JsonSerialize
public record ProductResponseRecord(String id, String name, String description, BigDecimal price) {

    public ProductResponseRecord(Product product){
        this(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }
}
