package com.example.internal_management_system.modules.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {

    private Long id;
    private Long productId;
    private String productName;
    private String productCode;
    private Long warehouseId;
    private String warehouseName;
    private BigDecimal currentQuantity;
    private BigDecimal reservedQuantity;
    private BigDecimal availableQuantity;
    private BigDecimal minQuantity;
    private BigDecimal maxQuantity;
    private BigDecimal unitCost;
    private String locationCode;
    private LocalDateTime lastUpdated;
}
