package com.example.internal_management_system.modules.warehouse.repository;

import com.example.internal_management_system.modules.warehouse.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
