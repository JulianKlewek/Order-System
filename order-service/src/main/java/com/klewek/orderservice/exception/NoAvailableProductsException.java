package com.klewek.orderservice.exception;

import com.klewek.orderservice.dto.InventoryResponseDto;

import java.util.ArrayList;
import java.util.List;

public class NoAvailableProductsException extends RuntimeException{
    static final long serialVersionUID = -2227969267685043310L;
    final transient List<InventoryResponseDto> missingProducts;
    public NoAvailableProductsException(){
        super();
        missingProducts = new ArrayList<>();
    }

    public NoAvailableProductsException(String s){
        super(s);
        missingProducts = new ArrayList<>();
    }
    public NoAvailableProductsException(String message, List<InventoryResponseDto> availableProducts){
        super(message);
        this.missingProducts = availableProducts;
    }
}
