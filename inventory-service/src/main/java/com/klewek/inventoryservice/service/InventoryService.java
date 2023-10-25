package com.klewek.inventoryservice.service;

import com.klewek.inventoryservice.model.Inventory;
import com.klewek.inventoryservice.record.ProductQuantityRecord;
import com.klewek.inventoryservice.repository.InventoryRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<ProductQuantityRecord> getProductsQuantities(List<String> skuCodes){
        return inventoryRepository.findBySkuCodeIn(skuCodes)
                .stream()
                .map(this::mapToInventoryResponseRecord)
                .toList();
    }

    private ProductQuantityRecord mapToInventoryResponseRecord(Inventory inventory) {
            return new ProductQuantityRecord(inventory.getSkuCode(), inventory.getQuantity());
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
