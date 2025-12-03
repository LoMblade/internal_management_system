package com.example.internal_management_system.modules.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String productCode;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private BigDecimal unitPrice;
    private String unitOfMeasure;
    private BigDecimal minStockLevel;
    private BigDecimal maxStockLevel;
    private String barcode;
    private String supplier;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
