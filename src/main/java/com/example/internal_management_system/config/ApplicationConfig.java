package com.example.internal_management_system.config;

import com.example.internal_management_system.common.constants.AppConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public String jwtSecret() {
        return AppConstants.JWT_SECRET;
    }

    @Bean
    public Long jwtExpiration() {
        return AppConstants.JWT_EXPIRATION;
    }
}
