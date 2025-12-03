package com.example.internal_management_system.modules.warehouse.service.impl;

import com.example.internal_management_system.modules.warehouse.dto.ProductDto;
import com.example.internal_management_system.modules.warehouse.mapper.ProductMapper;
import com.example.internal_management_system.modules.warehouse.model.Product;
import com.example.internal_management_system.modules.warehouse.repository.ProductRepository;
import com.example.internal_management_system.modules.warehouse.service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public ProductDto create(ProductDto dto) {
        Product entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public ProductDto update(Long id, ProductDto dto) {
        Product existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        Product updatedEntity = mapper.toEntity(dto);
        updatedEntity.setId(id);

        return mapper.toDto(repository.save(updatedEntity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<ProductDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getById(Long id) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapper.toDto(entity);
    }
}
