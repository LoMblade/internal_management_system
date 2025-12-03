package com.example.internal_management_system.modules.warehouse.service;

import com.example.internal_management_system.modules.warehouse.dto.StockExportDetailDto;

import java.util.List;

public interface StockExportDetailService {

    StockExportDetailDto create(StockExportDetailDto dto);

    StockExportDetailDto update(Long id, StockExportDetailDto dto);

    void delete(Long id);

    List<StockExportDetailDto> getAll();

    StockExportDetailDto getById(Long id);
}
