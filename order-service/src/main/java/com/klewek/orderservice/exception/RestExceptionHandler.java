package com.klewek.orderservice.exception;

import com.klewek.orderservice.dto.InventoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class RestExceptionHandler {

    private final Clock clock;

    @ExceptionHandler(NoAvailableProductsException.class)
    public ResponseEntity<StandardError> handleNoAvailableProductsException(
            NoAvailableProductsException exc, WebRequest request) {
        log.error(exc.getMessage());
        List<String> missingProducts = exc.availableProducts.stream()
                .map(InventoryResponseDto::skuCode)
                .toList();
        StandardError error = StandardError.builder()
                .timestamp(clock.instant())
                .status(HttpStatus.CONFLICT.value())
                .error(exc.getMessage())
                .detailedMessage(missingProducts)
                .path(request.getDescription(false))
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
}