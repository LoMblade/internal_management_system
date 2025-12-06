package com.example.internal_management_system.security.service;

import com.example.internal_management_system.security.model.Permission;
import com.example.internal_management_system.security.model.User;
import com.example.internal_management_system.security.repository.PermissionRepository;
import com.example.internal_management_system.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final SecurityService securityService;

    /**
     * Kiểm tra user có permission không
     */
    @Transactional(readOnly = true)
    public boolean hasPermission(Long userId, String resourceCode, String permissionCode) {
        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return false;
        }

        // Admin có tất cả quyền
        if (user.hasRole("ADMIN")) {
            return true;
        }

        String permissionString = "PERM_" + resourceCode + "_" + permissionCode;

        return user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(permissionString)
                        || auth.getAuthority().equals("PERM_ALL"));
    }

    /**
     * Kiểm tra user hiện tại có permission không
     */
    public boolean hasPermission(String resourceCode, String permissionCode) {
        User currentUser = securityService.getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        return hasPermission(currentUser.getId(), resourceCode, permissionCode);
    }

    /**
     * Kiểm tra user có quyền đọc tất cả hay chỉ đọc của mình
     */
    public boolean canReadAll(String resourceCode) {
        return hasPermission(resourceCode, "READ_ALL");
    }

    /**
     * Kiểm tra user có quyền đọc của chính mình
     */
    public boolean canReadOwn(String resourceCode) {
        return hasPermission(resourceCode, "READ_OWN");
    }

    /**
     * Lấy tất cả permissions của user (từ roles + direct)
     */
    @Transactional(readOnly = true)
    public Set<Permission> getUserPermissions(Long userId) {
        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return Set.of();
        }

        Set<Permission> allPermissions = new java.util.HashSet<>();

        // Lấy từ roles
        if (user.getRoles() != null) {
            for (var role : user.getRoles()) {
                if (role.getPermissions() != null) {
                    allPermissions.addAll(role.getPermissions());
                }
            }
        }

        // Lấy trực tiếp (override)
        if (user.getDirectPermissions() != null) {
            allPermissions.addAll(user.getDirectPermissions());
        }

        return allPermissions;
    }

    /**
     * Lấy permissions của user hiện tại
     */
    public Set<Permission> getCurrentUserPermissions() {
        User currentUser = securityService.getCurrentUser();
        if (currentUser == null) {
            return Set.of();
        }
        return getUserPermissions(currentUser.getId());
    }

    /**
     * Kiểm tra user có thể xem employee này không
     */
    public boolean canViewEmployee(Long employeeId) {
        User currentUser = securityService.getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        // Admin hoặc có READ_ALL
        if (hasPermission("EMPLOYEE", "READ_ALL")) {
            return true;
        }

        // Có READ_OWN và là chính mình
        if (hasPermission("EMPLOYEE", "READ_OWN")) {
            // TODO: Kiểm tra employeeId có phải của user hiện tại không
            // Cần thêm logic kiểm tra employee.userId == currentUser.id
            return true; // Tạm thời
        }

        return false;
    }
}


