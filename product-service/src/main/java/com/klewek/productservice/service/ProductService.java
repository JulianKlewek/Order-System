package com.klewek.productservice.service;

import com.klewek.productservice.dto.ProductStatus;
import com.klewek.productservice.mapper.ProductMapper;
import com.klewek.productservice.model.Product;
import com.klewek.productservice.dto.ProductRequestDto;
import com.klewek.productservice.dto.ProductResponseDto;
import com.klewek.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.klewek.productservice.mapper.ProductMapper.productToResponseDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDto createProduct(ProductRequestDto productRequest) {
        if (checkIfProductExists(productRequest)) {
            return ProductResponseDto.builder()
                    .name(productRequest.name())
                    .description(productRequest.description())
                    .price(productRequest.price())
                    .status(ProductStatus.ALREADY_EXISTS)
                    .build();
        }
        Product saved = productRepository.save(Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build());

        log.info("Product {} is saved", saved.getId());

        return productToResponseDto(saved, ProductStatus.CREATED);
    }

    private boolean checkIfProductExists(ProductRequestDto productRequest) {
        return productRepository.existsByNameAndDescription(productRequest.name(), productRequest.description());
    }

    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return ProductMapper.toDtoList(products, ProductStatus.LOADED_FROM_DB);
    }
}
