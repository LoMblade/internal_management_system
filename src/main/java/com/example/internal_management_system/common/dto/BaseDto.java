package com.example.internal_management_system.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDto {

    protected Long id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
}
