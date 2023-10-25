package com.klewek.productservice.mapper;

import com.klewek.productservice.dto.ProductRequestDto;
import com.klewek.productservice.dto.ProductStatus;
import com.klewek.productservice.dto.ProductResponseDto;
import com.klewek.productservice.model.Product;

import java.util.List;

public class ProductMapper {

    public static ProductResponseDto productToResponseDto(Product product, ProductStatus status){
        return new ProductResponseDto(product.getName(), product.getDescription(), product.getPrice(), status);
    }

    public static ProductResponseDto requestToResponseDto(ProductRequestDto product, ProductStatus status){
        return new ProductResponseDto(product.name(), product.description(), product.price(), status);
    }

    public static List<ProductResponseDto> toDtoList(List<Product> products, ProductStatus status){
        return products.stream()
                .map(product -> ProductMapper.productToResponseDto(product, status))
                .toList();
    }
}
