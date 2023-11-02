package com.klewek.orderservice.mapper;

import com.klewek.orderservice.dto.OrderResponseDto;
import com.klewek.orderservice.dto.OrderStatus;
import com.klewek.orderservice.model.Order;

import static com.klewek.orderservice.mapper.OrderLineItemMapper.*;

public class OrderMapper {
    public static OrderResponseDto toDto(Order order){
        return OrderResponseDto.builder()
                .orderNumber(order.getOrderNumber())
                .orderLineItemsList(listToDtoList(order.getOrderLineItemsList()))
                .build();
    }
    public static OrderResponseDto toDto(Order order, OrderStatus status){
        return OrderResponseDto.builder()
                .status(status)
                .orderNumber(order.getOrderNumber())
                .orderLineItemsList(listToDtoList(order.getOrderLineItemsList()))
                .build();
    }
}
