package com.klewek.inventoryservice.repository;

import com.klewek.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<List<Inventory>> findBySkuCodes(List<String> skuCodes);

    Optional<Inventory> findBySkuCode(String skuCode);
}
