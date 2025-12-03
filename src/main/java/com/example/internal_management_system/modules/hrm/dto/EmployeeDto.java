package com.example.internal_management_system.modules.hrm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private Long id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String address;
    private LocalDate hireDate;
    private Long departmentId;
    private String departmentName;
    private Long positionId;
    private String positionTitle;
    private BigDecimal salary;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
