package com.klewek.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klewek.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
@AutoConfigureMockMvc
class IntegrationConfiguration implements IntegrationTestConstants {


    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected ProductRepository productRepository;
    @Container
    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.0.10");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry properties) {
        properties.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    static {
        mongoDBContainer.start();
    }

}
