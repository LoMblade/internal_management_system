package com.example.internal_management_system.modules.warehouse.service.impl;

import com.example.internal_management_system.modules.warehouse.dto.CategoryDto;
import com.example.internal_management_system.modules.warehouse.mapper.CategoryMapper;
import com.example.internal_management_system.modules.warehouse.model.Category;
import com.example.internal_management_system.modules.warehouse.repository.CategoryRepository;
import com.example.internal_management_system.modules.warehouse.service.CategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public CategoryDto create(CategoryDto dto) {
        Category entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public CategoryDto update(Long id, CategoryDto dto) {
        Category existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        Category updatedEntity = mapper.toEntity(dto);
        updatedEntity.setId(id);

        return mapper.toDto(repository.save(updatedEntity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<CategoryDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        Category entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return mapper.toDto(entity);
    }
}
