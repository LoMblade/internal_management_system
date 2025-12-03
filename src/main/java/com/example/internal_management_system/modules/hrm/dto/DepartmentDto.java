package com.example.internal_management_system.modules.hrm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {

    private Long id;
    private String name;
    private String description;
    private Long managerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
