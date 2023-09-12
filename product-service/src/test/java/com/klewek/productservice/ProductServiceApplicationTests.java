package com.klewek.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klewek.productservice.record.ProductRequestRecord;
import com.klewek.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

    @Container
    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.0.10");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    static {
        mongoDBContainer.start();
    }
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry properties){
        properties.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductRequestRecord requestRecord = new ProductRequestRecord(
                "iPhone 14", "blue", BigDecimal.valueOf(1300)
        );
        String requestString = objectMapper.writeValueAsString(requestRecord);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestString))

                    .andExpect(status().isCreated())
                    .andExpect(content().string("Product added."));

        Assertions.assertEquals(1, productRepository.findAll().size());
    }

}
