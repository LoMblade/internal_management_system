package com.example.internal_management_system.modules.warehouse.service;

import com.example.internal_management_system.modules.warehouse.dto.StockImportDto;

import java.util.List;

public interface StockImportService {

    StockImportDto create(StockImportDto dto);

    StockImportDto update(Long id, StockImportDto dto);

    void delete(Long id);

    List<StockImportDto> getAll();

    StockImportDto getById(Long id);
}
