package com.example.internal_management_system.security.controller;

import com.example.internal_management_system.security.dto.*;
import com.example.internal_management_system.security.model.Permission;
import com.example.internal_management_system.security.model.Role;
import com.example.internal_management_system.security.model.User;
import com.example.internal_management_system.security.repository.PermissionRepository;
import com.example.internal_management_system.security.repository.ResourceRepository;
import com.example.internal_management_system.security.repository.RoleRepository;
import com.example.internal_management_system.security.repository.UserRepository;
import com.example.internal_management_system.security.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/permissions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPermissionController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ResourceRepository resourceRepository;
    private final PermissionService permissionService;

    /**
     * Lấy danh sách permissions của user
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserPermissionDto> getUserPermissions(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserPermissionDto dto = new UserPermissionDto();
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        // Lấy roles
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        dto.setRoles(roles);

        // Lấy permissions
        Set<Permission> allPermissions = permissionService.getUserPermissions(userId);
        List<PermissionDto> permissionDtos = allPermissions.stream()
                .map(p -> {
                    PermissionDto pd = new PermissionDto();
                    pd.setId(p.getId());
                    pd.setResourceCode(p.getResource().getCode());
                    pd.setResourceName(p.getResource().getName());
                    pd.setPermissionCode(p.getCode());
                    pd.setPermissionName(p.getName());
                    pd.setDescription(p.getDescription());
                    // Xác định source
                    boolean fromRole = user.getRoles().stream()
                            .anyMatch(role -> role.getPermissions().contains(p));
                    pd.setSource(fromRole ? "ROLE" : "DIRECT");
                    return pd;
                })
                .collect(Collectors.toList());
        dto.setPermissions(permissionDtos);

        return ResponseEntity.ok(dto);
    }

    /**
     * Gán roles cho user
     */
    @PostMapping("/users/{userId}/roles")
    public ResponseEntity<?> assignRoles(@PathVariable Long userId,
                                         @RequestBody AssignRoleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Role> roles = request.getRoleIds().stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleId)))
                .collect(Collectors.toSet());

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok("Roles assigned successfully");
    }

    /**
     * Gán permissions trực tiếp cho user (override)
     */
    @PostMapping("/users/{userId}/permissions")
    public ResponseEntity<?> assignPermissions(@PathVariable Long userId,
                                               @RequestBody AssignPermissionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Permission> permissions = request.getPermissions().stream()
                .map(item -> permissionRepository
                        .findByResource_CodeAndCode(item.getResourceCode(), item.getPermissionCode())
                        .orElseThrow(() -> new RuntimeException(
                                "Permission not found: " + item.getResourceCode() + "_" + item.getPermissionCode())))
                .collect(Collectors.toSet());

        user.setDirectPermissions(permissions);
        userRepository.save(user);

        return ResponseEntity.ok("Permissions assigned successfully");
    }

    /**
     * Lấy tất cả roles
     */
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleRepository.findAll());
    }

    /**
     * Lấy tất cả permissions
     */
    @GetMapping("/permissions")
    public ResponseEntity<List<Permission>> getAllPermissions() {
        return ResponseEntity.ok(permissionRepository.findAll());
    }

    /**
     * Lấy permissions theo resource
     */
    @GetMapping("/resources/{resourceCode}/permissions")
    public ResponseEntity<List<Permission>> getPermissionsByResource(@PathVariable String resourceCode) {
        return ResponseEntity.ok(permissionRepository.findByResourceCode(resourceCode));
    }
}


