package com.example.internal_management_system.modules.warehouse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", insertable = false, updatable = false)
    private Warehouse warehouse;

    private BigDecimal currentQuantity;

    private BigDecimal reservedQuantity;

    private BigDecimal availableQuantity;

    private BigDecimal minQuantity;

    private BigDecimal maxQuantity;

    private BigDecimal unitCost;

    @Column(name = "location_code", length = 50)
    private String locationCode;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        lastUpdated = LocalDateTime.now();
        calculateAvailableQuantity();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
        calculateAvailableQuantity();
    }

    private void calculateAvailableQuantity() {
        BigDecimal current = currentQuantity != null ? currentQuantity : BigDecimal.ZERO;
        BigDecimal reserved = reservedQuantity != null ? reservedQuantity : BigDecimal.ZERO;
        this.availableQuantity = current.subtract(reserved);
    }
}
