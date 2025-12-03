package com.example.internal_management_system.modules.warehouse.service;

import com.example.internal_management_system.modules.warehouse.dto.WarehouseDto;

import java.util.List;

public interface WarehouseService {

    WarehouseDto create(WarehouseDto dto);

    WarehouseDto update(Long id, WarehouseDto dto);

    void delete(Long id);

    List<WarehouseDto> getAll();

    WarehouseDto getById(Long id);
}
