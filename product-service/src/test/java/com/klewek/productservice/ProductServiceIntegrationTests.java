package com.klewek.productservice;

import com.klewek.productservice.dto.ProductRequestDto;
import com.klewek.productservice.dto.ProductResponseDto;
import com.klewek.productservice.dto.ProductStatus;
import com.klewek.productservice.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

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
    @MethodSource("INVALID_PRICES")
    @DisplayName("Should not create product and receive price error")
    void should_not_create_product_with_zero_or_negative_price(BigDecimal price) throws Exception {
        //given
        ProductRequestDto request = ProductRequestDto.builder()
                .name("iPhone14")
                .description("blue")
                .price(price)
                .build();

        String requestString = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(post(PRODUCT_SERVICE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString));

        //then
        perform
                .andExpect(status().isUnprocessableEntity())
//                .andExpect(jsonPath("$.timestamp")
//                        .value("Price must be higher than 0"))
                .andExpect(jsonPath("$.detailedMessage")
                        .value("Price must be higher than 0"));

    }

}
