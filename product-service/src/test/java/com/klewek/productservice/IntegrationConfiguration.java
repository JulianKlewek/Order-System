package com.klewek.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klewek.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.Clock;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
class IntegrationConfiguration implements IntegrationTestConstants {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected ProductRepository productRepository;
    @MockBean
    protected Clock clock;
    @Container
    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.0.10");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry properties) {
        properties.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    static {
        mongoDBContainer.start();
    }

    @BeforeEach
    void tearDown() {
        productRepository.deleteAll();
        log.info("Clearing Products table.");
    }

}
