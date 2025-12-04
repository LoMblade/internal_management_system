package com.example.internal_management_system.security.service;

import com.example.internal_management_system.security.model.User;
import com.example.internal_management_system.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new UserDetailsImpl(user);
    }

    // Inner class static để Spring Security dùng được
    public static class UserDetailsImpl extends org.springframework.security.core.userdetails.User {

        private final Long id;
        private final String email;

        public UserDetailsImpl(User user) {
            super(user.getUsername(),
                    user.getPassword(),
                    user.isEnabled(), true, true, true, // accountNonExpired, credentialsNonExpired, accountNonLocked
                    user.getAuthorities());
            this.id = user.getId();
            this.email = user.getEmail();
        }

        public Long getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }
    }
}