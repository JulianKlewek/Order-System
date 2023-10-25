package com.klewek.productservice.repository;

import com.klewek.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {

    boolean existsByNameAndDescription(String name, String description);
}
