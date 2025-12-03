package com.example.internal_management_system.modules.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockExportDetailDto {

    private Long id;
    private Long stockExportId;
    private String exportCode;
    private Long productId;
    private String productName;
    private String productCode;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private String batchNumber;
    private String notes;
}
