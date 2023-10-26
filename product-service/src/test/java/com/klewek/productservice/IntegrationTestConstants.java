package com.klewek.productservice;

import org.junit.jupiter.params.provider.Arguments;

import java.math.BigDecimal;
import java.util.stream.Stream;

public interface IntegrationTestConstants {

    String PRODUCT_SERVICE_PATH = "/api/product";
    String FIXED_CLOCK_TIME = "2023-04-04T04:04:04.040Z";

    static Stream<Arguments> PROVIDE_INVALID_PRICES() {
        return Stream.of(
                Arguments.of(BigDecimal.ZERO),
                Arguments.of(BigDecimal.valueOf(-100))
        );
    }

    static Stream<Arguments> PROVIDE_INVALID_PRODUCT_VALUES() {
        return Stream.of(
                Arguments.of(null, null, BigDecimal.ZERO)
        );
    }
}
