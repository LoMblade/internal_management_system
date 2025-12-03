package com.example.internal_management_system.modules.warehouse.service;

import com.example.internal_management_system.modules.warehouse.dto.InventoryDto;

import java.util.List;

public interface InventoryService {

    InventoryDto create(InventoryDto dto);

    InventoryDto update(Long id, InventoryDto dto);

    void delete(Long id);

    List<InventoryDto> getAll();

    InventoryDto getById(Long id);
}
