package com.example.internal_management_system.modules.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockImportDetailDto {

    private Long id;
    private Long stockImportId;
    private String importCode;
    private Long productId;
    private String productName;
    private String productCode;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private LocalDate expiryDate;
    private String batchNumber;
    private String notes;
}
