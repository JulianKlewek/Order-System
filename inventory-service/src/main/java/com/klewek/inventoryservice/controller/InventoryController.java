package com.klewek.inventoryservice.controller;

import com.klewek.inventoryservice.record.InventoryResponseDto;
import com.klewek.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryResponseDto>> isInStock(@RequestParam List<String> skuCodes){
        List<InventoryResponseDto> inventoryResponseRecords = inventoryService.getProductsQuantities(skuCodes);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(inventoryResponseRecords);
    }
}
