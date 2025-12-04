package com.example.internal_management_system.security.service;

import com.example.internal_management_system.security.model.User;
import com.example.internal_management_system.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service tiện ích để lấy thông tin user đang đăng nhập
 * Dùng ở bất kỳ đâu trong ứng dụng (ví dụ: lấy user hiện tại, kiểm tra role, v.v.)
 */
@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;

    /**
     * Lấy User entity của người đang đăng nhập
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    /**
     * Lấy ID của user hiện tại
     */
    public Long getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    /**
     * Lấy username của user hiện tại
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated()
                ? authentication.getName()
                : null;
    }

    /**
     * Kiểm tra user hiện tại có role cụ thể không
     */
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(granted -> granted.getAuthority().equals("ROLE_" + role.toUpperCase()));
    }

    /**
     * Kiểm tra user hiện tại có phải ADMIN không
     */
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    /**
     * Kiểm tra user hiện tại có phải chính chủ tài khoản này không (dùng cho update/delete own profile)
     */
    public boolean isOwner(Long userId) {
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(userId);
    }
}