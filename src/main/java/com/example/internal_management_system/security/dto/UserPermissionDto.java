package com.example.internal_management_system.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPermissionDto {
    private Long userId;
    private String username;
    private String email;
    private List<String> roles;
    private List<PermissionDto> permissions;
}


