package com.klewek.inventoryservice.util;


import com.klewek.inventoryservice.model.Inventory;
import com.klewek.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        Inventory inventory = new Inventory();
        inventory.setSkuCode("iphone_14_blue");
        inventory.setQuantity(100);

        Inventory inventory1 = new Inventory();
        inventory1.setSkuCode("iphone_14_black");
        inventory1.setQuantity(0);

        Inventory inventory2 = new Inventory();
        inventory2.setSkuCode("iphone_13_blue");
        inventory2.setQuantity(100);

        Inventory inventory3 = new Inventory();
        inventory3.setSkuCode("iphone_13_black");
        inventory3.setQuantity(100);

        inventoryRepository.save(inventory);
        inventoryRepository.save(inventory1);
        inventoryRepository.save(inventory2);
        inventoryRepository.save(inventory3);
    }
}