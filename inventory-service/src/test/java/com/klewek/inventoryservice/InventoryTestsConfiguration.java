package com.klewek.inventoryservice;

import com.klewek.inventoryservice.repository.InventoryRepository;
import com.klewek.inventoryservice.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

@SpringBootTest
@Slf4j
public class InventoryTestsConfiguration {

    @Autowired
    protected InventoryRepository inventoryRepository;
    @Autowired
    protected InventoryService inventoryService;

    @Container
    static final MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0.32");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry properties) {
        properties.add("spring.data.mysql.url", mySQLContainer::getJdbcUrl);
    }

    static {
        mySQLContainer.start();
    }

    @BeforeEach
    void tearDown() {
        inventoryRepository.deleteAll();
        log.info("Clearing Inventory table.");
    }
}
