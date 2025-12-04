package com.example.internal_management_system.modules.hrm.mapper;

import com.example.internal_management_system.modules.hrm.dto.DepartmentDto;
import com.example.internal_management_system.modules.hrm.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DepartmentMapper {

    Department toEntity(DepartmentDto dto);

    DepartmentDto toDto(Department entity);

    // Dùng cho update: chỉ cập nhật các field không null từ DTO vào entity hiện có
    void updateEntityFromDto(DepartmentDto dto, @MappingTarget Department entity);
}