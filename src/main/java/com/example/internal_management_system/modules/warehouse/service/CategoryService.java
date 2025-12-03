package com.example.internal_management_system.modules.warehouse.service;

import com.example.internal_management_system.modules.warehouse.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto create(CategoryDto dto);

    CategoryDto update(Long id, CategoryDto dto);

    void delete(Long id);

    List<CategoryDto> getAll();

    CategoryDto getById(Long id);
}
