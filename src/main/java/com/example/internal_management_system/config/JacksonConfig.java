package com.example.internal_management_system.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Register JavaTimeModule for LocalDateTime, LocalDate support
        objectMapper.registerModule(new JavaTimeModule());

        // Configure property naming strategy (optional - using camelCase by default)
        // objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        // Configure serialization features
        // objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper;
    }
}
