package com.klewek.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.klewek.orderservice.repository.OrderRepository;
import com.klewek.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Clock;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
public class OrderTestsConfiguration {
    protected static final String WIREMOCK_SERVER_HOST = "http://localhost";

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected OrderRepository orderRepository;
    @RegisterExtension
    protected static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();
    @Autowired
    protected OrderService orderService;
    @MockBean
    protected Clock clock;
    @Container
    static final MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0.32");
    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.0.1"));

    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry properties) {
        properties.add("spring.data.mysql.url", mySQLContainer::getJdbcUrl);
        properties.add("inventory.service.url", () -> WIREMOCK_SERVER_HOST + ":" + wireMockServer.getPort());
        properties.add("spring.cloud.loadbalancer.enabled", () -> false);
        properties.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    static {
        mySQLContainer.start();
    }

    @BeforeEach
    void tearDown() {
        orderRepository.deleteAll();
        log.info("Clearing Orders table.");
    }

    @AfterEach
    void afterEach() {
        log.info("Resetting wireMock");
        wireMockServer.resetAll();
    }
}
