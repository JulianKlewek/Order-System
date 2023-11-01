package com.klewek.orderservice.controller;

import com.klewek.orderservice.dto.OrderRequestDto;
import com.klewek.orderservice.dto.OrderResponseDto;
import com.klewek.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody OrderRequestDto orderRequestDto){
        OrderResponseDto orderResponseDto = orderService.placeOrder(orderRequestDto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(orderResponseDto);
    }

}
