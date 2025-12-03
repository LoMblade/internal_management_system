package com.example.internal_management_system.modules.warehouse.service;

import com.example.internal_management_system.modules.warehouse.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto create(ProductDto dto);

    ProductDto update(Long id, ProductDto dto);

    void delete(Long id);

    List<ProductDto> getAll();

    ProductDto getById(Long id);
}
