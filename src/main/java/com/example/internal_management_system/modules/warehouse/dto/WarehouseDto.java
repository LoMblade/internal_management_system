package com.example.internal_management_system.modules.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDto {

    private Long id;
    private String warehouseCode;
    private String name;
    private String description;
    private String address;
    private String phone;
    private String managerName;
    private BigDecimal capacity;
    private String unitOfMeasure;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
