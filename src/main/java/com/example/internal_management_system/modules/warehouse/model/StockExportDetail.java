package com.example.internal_management_system.modules.warehouse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "stock_export_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockExportDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_export_id", nullable = false)
    private Long stockExportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_export_id", insertable = false, updatable = false)
    private StockExport stockExport;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    private BigDecimal quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalAmount;

    @Column(name = "batch_number", length = 50)
    private String batchNumber;

    @Column(length = 500)
    private String notes;

    @PrePersist
    @PreUpdate
    protected void calculateTotalAmount() {
        if (quantity != null && unitPrice != null) {
            this.totalAmount = quantity.multiply(unitPrice);
        }
    }
}
