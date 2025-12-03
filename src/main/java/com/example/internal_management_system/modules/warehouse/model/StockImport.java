package com.example.internal_management_system.modules.warehouse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "stock_imports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockImport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "import_code", nullable = false, unique = true, length = 20)
    private String importCode;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", insertable = false, updatable = false)
    private Warehouse warehouse;

    @Column(name = "supplier_name", length = 200)
    private String supplierName;

    @Column(name = "supplier_invoice", length = 50)
    private String supplierInvoice;

    @Column(name = "import_date", nullable = false)
    private LocalDate importDate;

    private BigDecimal totalQuantity;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ImportStatus status;

    @Column(length = 500)
    private String notes;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "stockImport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StockImportDetail> importDetails;

    public enum ImportStatus {
        DRAFT, PENDING_APPROVAL, APPROVED, RECEIVED, CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = ImportStatus.DRAFT;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
