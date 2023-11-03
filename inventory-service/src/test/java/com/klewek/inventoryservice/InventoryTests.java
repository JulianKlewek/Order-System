package com.klewek.inventoryservice;

import com.klewek.inventoryservice.model.Inventory;
import com.klewek.inventoryservice.record.InventoryResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InventoryTests extends InventoryTestsConfiguration {

    @Test
    @DisplayName("Should return two products")
    void should_return_two_products() throws Exception {
        //given
        String skuCode1 = "skuCode1";
        String skuCode2 = "skuCode2";
        Inventory inventory1 = Inventory.builder()
                .skuCode(skuCode1)
                .quantity(100)
                .build();
        Inventory inventory2 = Inventory.builder()
                .skuCode(skuCode2)
                .quantity(100)
                .build();
        inventoryRepository.save(inventory1);
        inventoryRepository.save(inventory2);
        List<String> skuCodes = List.of(skuCode1, skuCode2);
        //when
        List<InventoryResponseDto> inventories = inventoryService.getProductsQuantities(skuCodes);
        //then
        assertNotNull(inventories);
        assertEquals(2, inventories.size());
    }
}
