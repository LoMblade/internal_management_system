package com.example.internal_management_system.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {
    private Long id;
    private String resourceCode;
    private String resourceName;
    private String permissionCode;
    private String permissionName;
    private String description;
    private String source; // "ROLE" or "DIRECT"
}


