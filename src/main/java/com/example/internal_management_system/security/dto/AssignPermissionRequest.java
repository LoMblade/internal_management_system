package com.example.internal_management_system.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignPermissionRequest {
    private List<PermissionItem> permissions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PermissionItem {
        private String resourceCode;
        private String permissionCode;
    }
}


