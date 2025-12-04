package com.example.internal_management_system.security.service;

import com.example.internal_management_system.security.model.User;
import com.example.internal_management_system.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    public static class UserDetailsImpl implements UserDetails {
        private static final long serialVersionUID = 1L;

        private Long id;
        private String username;
        private String email;
        private String password;
        private GrantedAuthority authority;

        public UserDetailsImpl(Long id, String username, String email, String password, GrantedAuthority authority) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.password = password;
            this.authority = authority;
        }

        public static UserDetailsImpl build(User user) {
            // Spring Security convention: ROLE_ prefix for role-based checks (hasRole)
            // Ví dụ: role ADMIN trong DB -> authority "ROLE_ADMIN"
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());

            return new UserDetailsImpl(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    authority);
        }

        public Long getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public List<GrantedAuthority> getAuthorities() {
            return Collections.singletonList(authority);
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
            return true;
        }
    }
}
