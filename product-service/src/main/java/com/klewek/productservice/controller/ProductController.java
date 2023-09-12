package com.klewek.productservice.controller;

import com.klewek.productservice.record.ProductRequestRecord;
import com.klewek.productservice.record.ProductResponseRecord;
import com.klewek.productservice.service.ProductService;
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
    public ResponseEntity<String> createProduct(@RequestBody ProductRequestRecord productRequest){
        productService.createProduct(productRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Product added.");
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseRecord>> getAllProducts(){
        List<ProductResponseRecord> allProducts = productService.getAllProducts();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allProducts);
    }
}
