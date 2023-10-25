package com.klewek.productservice;

import org.junit.jupiter.params.provider.Arguments;

import java.math.BigDecimal;
import java.util.stream.Stream;

public interface IntegrationTestConstants {

    String PRODUCT_SERVICE_PATH = "/api/product";

    static Stream<Arguments> INVALID_PRICES() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(0)),
                Arguments.of(BigDecimal.valueOf(-100))
        );
    }
}
