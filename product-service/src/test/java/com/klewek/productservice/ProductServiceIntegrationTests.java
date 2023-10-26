package com.klewek.productservice;

import com.klewek.productservice.dto.ProductRequestDto;
import com.klewek.productservice.dto.ProductResponseDto;
import com.klewek.productservice.dto.ProductStatus;
import com.klewek.productservice.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.Instant;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductServiceIntegrationTests extends IntegrationConfiguration {

    @Test
    @DisplayName("Should create product and receive product with status CREATED")
    void should_create_product() throws Exception {
        //given
        ProductRequestDto request = ProductRequestDto.builder()
                .name("iPhone14")
                .description("blue")
                .price(BigDecimal.valueOf(1300))
                .build();
        ProductResponseDto response = ProductResponseDto.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .status(ProductStatus.CREATED)
                .build();
        String requestString = objectMapper.writeValueAsString(request);
        String responseString = objectMapper.writeValueAsString(response);
        //when
        ResultActions perform = mockMvc.perform(post(PRODUCT_SERVICE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString));
        //then
        perform
                .andExpect(status().isCreated())
                .andExpect(content().string(responseString));
    }

    @Test
    @DisplayName("Should not create duplicated product and receive product with status CONFLICT")
    void should_not_create_duplicated_product() throws Exception {
        //given
        productRepository.save(Product.builder()
                .name("iPhone14")
                .description("blue")
                .price(BigDecimal.valueOf(1300))
                .build());
        ProductRequestDto request = ProductRequestDto.builder()
                .name("iPhone14")
                .description("blue")
                .price(BigDecimal.valueOf(1300))
                .build();
        ProductResponseDto response = ProductResponseDto.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .status(ProductStatus.ALREADY_EXISTS)
                .build();
        String requestString = objectMapper.writeValueAsString(request);
        String responseString = objectMapper.writeValueAsString(response);
        //when
        ResultActions perform = mockMvc.perform(post(PRODUCT_SERVICE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString));
        //then
        perform
                .andExpect(status().isConflict())
                .andExpect(content().string(responseString));
    }

    @ParameterizedTest
    @MethodSource("PROVIDE_INVALID_PRICES")
    @DisplayName("Should not create product and receive price error")
    void should_not_create_product_with_zero_or_negative_price(BigDecimal price) throws Exception {
        //given
        ProductRequestDto request = ProductRequestDto.builder()
                .name("iPhone14")
                .description("blue")
                .price(price)
                .build();
        String requestString = objectMapper.writeValueAsString(request);
        when(clock.instant()).thenReturn(
                Instant.parse(FIXED_CLOCK_TIME));
        //when
        ResultActions perform = mockMvc.perform(post(PRODUCT_SERVICE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString));
        //then
        perform
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.timestamp")
                        .value(FIXED_CLOCK_TIME))
                .andExpect(jsonPath("$.detailedMessage")
                        .value("Price must be higher than 0"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should not create product and receive name error")
    void should_not_create_product_with_null_or_empty_name(String name) throws Exception {
        //given
        ProductRequestDto request = ProductRequestDto.builder()
                .name(name)
                .description("blue")
                .price(BigDecimal.valueOf(1300))
                .build();
        String requestString = objectMapper.writeValueAsString(request);
        when(clock.instant()).thenReturn(
                Instant.parse(FIXED_CLOCK_TIME));
        //when
        ResultActions perform = mockMvc.perform(post(PRODUCT_SERVICE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString));
        //then
        perform
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.timestamp")
                        .value(FIXED_CLOCK_TIME))
                .andExpect(jsonPath("$.detailedMessage")
                        .value("Name can not be null or empty"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should not create product and receive description error")
    void should_not_create_product_with_null_or_empty_description(String description) throws Exception {
        //given
        ProductRequestDto request = ProductRequestDto.builder()
                .name("iPhone13")
                .description(description)
                .price(BigDecimal.valueOf(1300))
                .build();
        String requestString = objectMapper.writeValueAsString(request);
        when(clock.instant()).thenReturn(
                Instant.parse(FIXED_CLOCK_TIME));
        //when
        ResultActions perform = mockMvc.perform(post(PRODUCT_SERVICE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString));
        //then
        perform
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.timestamp")
                        .value(FIXED_CLOCK_TIME))
                .andExpect(jsonPath("$.detailedMessage")
                        .value("Description can not be null or empty"));
    }

    @ParameterizedTest
    @MethodSource("PROVIDE_INVALID_PRODUCT_VALUES")
    @DisplayName("Should not create product and receive three errors")
    void should_not_create_product_with_invalid_parameters(String name, String description, BigDecimal price)
            throws Exception {
        //given
        ProductRequestDto request = ProductRequestDto.builder()
                .name(name)
                .description(description)
                .price(price)
                .build();
        String requestString = objectMapper.writeValueAsString(request);
        when(clock.instant()).thenReturn(
                Instant.parse(FIXED_CLOCK_TIME));
        //when
        ResultActions perform = mockMvc.perform(post(PRODUCT_SERVICE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString));
        //then
        perform
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.timestamp")
                        .value(FIXED_CLOCK_TIME))
                .andExpect(jsonPath("$.detailedMessage", containsInAnyOrder(
                        "Name can not be null or empty",
                        "Description can not be null or empty",
                        "Price must be higher than 0"
                )));
    }

    @Test
    @DisplayName("Should return two products")
    void should_return_two_products() throws Exception {
        //given
        productRepository.save(Product.builder()
                .name("iPhone14")
                .description("blue")
                .price(BigDecimal.valueOf(1300))
                .build());
        productRepository.save(Product.builder()
                .name("iPhone14")
                .description("green")
                .price(BigDecimal.valueOf(1300))
                .build());
        //when
        ResultActions perform = mockMvc.perform(get(PRODUCT_SERVICE_PATH)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }
}
