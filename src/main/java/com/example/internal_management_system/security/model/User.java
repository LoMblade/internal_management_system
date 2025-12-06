package com.example.internal_management_system.security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // Giữ lại role cũ để backward compatibility (có thể xóa sau)
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private LegacyRole role;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Nhiều roles
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Permissions trực tiếp (override)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_permissions",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> directPermissions = new HashSet<>();

    // Enum cũ để backward compatibility
    public enum LegacyRole {
        ADMIN, HR, WAREHOUSE, MANAGER, STAFF
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isActive == null) {
            isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ================== BẮT BUỘC CHO SPRING SECURITY ==================
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Admin có tất cả quyền
        if (hasRole("ADMIN")) {
            authorities.add(new SimpleGrantedAuthority("PERM_ALL"));
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return authorities;
        }

        // Lấy permissions từ roles
        if (roles != null) {
            for (Role role : roles) {
                // Thêm role authority
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                
                // Thêm permissions từ role
                if (role.getPermissions() != null) {
                    for (Permission permission : role.getPermissions()) {
                        String permissionString = "PERM_" + permission.getResource().getCode() + "_" + permission.getCode();
                        authorities.add(new SimpleGrantedAuthority(permissionString));
                    }
                }
            }
        }

        // Lấy permissions trực tiếp (override)
        if (directPermissions != null) {
            for (Permission permission : directPermissions) {
                String permissionString = "PERM_" + permission.getResource().getCode() + "_" + permission.getCode();
                authorities.add(new SimpleGrantedAuthority(permissionString));
            }
        }

        // Backward compatibility: Nếu có role cũ
        if (role != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        }

        return authorities;
    }

    /**
     * Kiểm tra user có role không
     */
    public boolean hasRole(String roleName) {
        if (roles != null) {
            return roles.stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase(roleName));
        }
        // Backward compatibility
        if (role != null) {
            return role.name().equalsIgnoreCase(roleName);
        }
        return false;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(this.isActive);
    }
}