package com.klewek.productservice.service;

import com.klewek.productservice.model.Product;
import com.klewek.productservice.record.ProductRequestRecord;
import com.klewek.productservice.record.ProductResponseRecord;
import com.klewek.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(final ProductRequestRecord productRequest){
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponseRecord> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(ProductResponseRecord::new)
                .toList();
    }
}
