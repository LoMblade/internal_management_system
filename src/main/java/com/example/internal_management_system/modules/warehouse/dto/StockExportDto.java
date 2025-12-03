package com.example.internal_management_system.modules.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockExportDto {

    private Long id;
    private String exportCode;
    private Long warehouseId;
    private String warehouseName;
    private String customerName;
    private String customerOrder;
    private LocalDate exportDate;
    private BigDecimal totalQuantity;
    private BigDecimal totalAmount;
    private String status;
    private String type;
    private String notes;
    private String createdBy;
    private String approvedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
