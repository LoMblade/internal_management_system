package com.example.internal_management_system.modules.hrm.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private Long id;

    /** Mã nhân viên duy nhất */
    @NotBlank
    private String employeeCode;

    /** Code nội bộ cho doanh nghiệp */
    @NotBlank
    private String code;
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
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

