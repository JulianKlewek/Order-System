package com.klewek.inventoryservice.service;

import com.klewek.inventoryservice.model.Inventory;
import com.klewek.inventoryservice.record.InventoryResponseDto;
import com.klewek.inventoryservice.repository.InventoryRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.klewek.inventoryservice.mapper.InventoryResponseMapper.*;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponseDto> getProductsQuantities(List<String> skuCodes){
        List<Inventory> inventories = inventoryRepository.findBySkuCodes(skuCodes)
                .orElseThrow(() -> new NotFoundException("Could not find products for given sku codes"));
        return listToDtoList(inventories);
    }
    public Integer updateProductQuantity(String skuCode, Integer quantity) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new NotFoundException("Inventory not exists with skuCode: " + skuCode));
        quantity += inventory.getQuantity();
        inventory.setQuantity(quantity);
        inventoryRepository.save(inventory);
        return quantity;
    }
}
