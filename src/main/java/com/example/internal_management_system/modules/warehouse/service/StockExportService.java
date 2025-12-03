package com.example.internal_management_system.modules.warehouse.service;

import com.example.internal_management_system.modules.warehouse.dto.StockExportDto;

import java.util.List;

public interface StockExportService {

    StockExportDto create(StockExportDto dto);

    StockExportDto update(Long id, StockExportDto dto);

    void delete(Long id);

    List<StockExportDto> getAll();

    StockExportDto getById(Long id);
}
