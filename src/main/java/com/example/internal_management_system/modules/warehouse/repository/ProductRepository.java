package com.example.internal_management_system.modules.warehouse.repository;

import com.example.internal_management_system.modules.warehouse.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
