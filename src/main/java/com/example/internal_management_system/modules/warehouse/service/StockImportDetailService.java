package com.example.internal_management_system.modules.warehouse.service;

import com.example.internal_management_system.modules.warehouse.dto.StockImportDetailDto;

import java.util.List;

public interface StockImportDetailService {

    StockImportDetailDto create(StockImportDetailDto dto);

    StockImportDetailDto update(Long id, StockImportDetailDto dto);

    void delete(Long id);

    List<StockImportDetailDto> getAll();

    StockImportDetailDto getById(Long id);
}
