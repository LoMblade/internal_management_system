package com.example.internal_management_system.security.evaluator;

import com.example.internal_management_system.security.model.User;
import com.example.internal_management_system.security.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component("permissionEvaluator")
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final PermissionService permissionService;

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object targetDomainObject,
                                 Object permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User)) {
            return false;
        }

        User user = (User) principal;

        // Admin có tất cả quyền
        if (user.hasRole("ADMIN")) {
            return true;
        }

        String resourceCode = (String) targetDomainObject;
        String permissionCode = (String) permission;

        return permissionService.hasPermission(user.getId(), resourceCode, permissionCode);
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        return hasPermission(authentication, targetType, permission);
    }
}


