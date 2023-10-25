package com.klewek.productservice.controller;

import com.klewek.productservice.dto.ProductRequestDto;
import com.klewek.productservice.dto.ProductResponseDto;
import com.klewek.productservice.dto.ProductStatus;
import com.klewek.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody @Valid ProductRequestDto productRequest){
        ProductResponseDto createProductDto = productService.createProduct(productRequest);

        HttpStatus httpStatus = createProductDto.status() == ProductStatus.ALREADY_EXISTS
                ? HttpStatus.CONFLICT
                : HttpStatus.CREATED;

        return ResponseEntity
                .status(httpStatus)
                .body(createProductDto);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(){
        List<ProductResponseDto> allProducts = productService.getAllProducts();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allProducts);
    }
}
