package com.example.internal_management_system.modules.hrm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollDto {

    private Long id;
    private Long employeeId;
    private String employeeName;
    private YearMonth payPeriod;
    private BigDecimal baseSalary;
    private Double overtimeHours;
    private BigDecimal overtimeRate;
    private BigDecimal bonus;
    private BigDecimal deductions;
    private BigDecimal taxAmount;
    private BigDecimal netSalary;
    private LocalDate paymentDate;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
