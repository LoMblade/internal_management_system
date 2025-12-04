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
@Table(name = "stock_exports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockExport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "export_code", nullable = false, unique = true, length = 20)
    private String exportCode;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", insertable = false, updatable = false)
    private Warehouse warehouse;

    @Column(name = "customer_name", length = 200)
    private String customerName;

    @Column(name = "customer_order", length = 50)
    private String customerOrder;

    @Column(name = "export_date", nullable = false)
    private LocalDate exportDate;

    private BigDecimal totalQuantity;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ExportStatus status;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ExportType type;

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

    @OneToMany(mappedBy = "stockExport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StockExportDetail> exportDetails;

    public enum ExportStatus {
        DRAFT, PENDING_APPROVAL, APPROVED, SHIPPED, DELIVERED, CANCELLED
    }

    public enum ExportType {
        SALE, TRANSFER, RETURN, ADJUSTMENT, SCRAP
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = ExportStatus.DRAFT;
        }
        // Auto-populate audit fields
        populateAuditFields(true);
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        // Auto-populate audit fields
        populateAuditFields(false);
    }

    /**
     * Auto-populate audit fields từ SecurityContext
     */
    private void populateAuditFields(boolean isCreate) {
        try {
            // Lấy username từ SecurityContext
            String currentUsername = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

            if (isCreate) {
                this.createdBy = currentUsername;
            }
            // StockExport không có updatedBy field, chỉ có createdBy
        } catch (Exception e) {
            // Nếu không có authentication context (ví dụ trong tests), set default values
            if (isCreate) {
                this.createdBy = "system";
            }
        }
    }
}
