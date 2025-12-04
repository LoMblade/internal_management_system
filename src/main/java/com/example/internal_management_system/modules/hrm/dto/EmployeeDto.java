<<<<<<< Current (Your changes)
package com.example.internal_management_system.modules.hrm.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
=======
 
>>>>>>> Incoming (Background Agent changes)
