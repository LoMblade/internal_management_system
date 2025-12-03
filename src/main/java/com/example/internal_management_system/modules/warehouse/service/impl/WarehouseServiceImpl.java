package com.example.internal_management_system.modules.warehouse.service.impl;

import com.example.internal_management_system.modules.warehouse.dto.WarehouseDto;
import com.example.internal_management_system.modules.warehouse.mapper.WarehouseMapper;
import com.example.internal_management_system.modules.warehouse.model.Warehouse;
import com.example.internal_management_system.modules.warehouse.repository.WarehouseRepository;
import com.example.internal_management_system.modules.warehouse.service.WarehouseService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository repository;
    private final WarehouseMapper mapper;

    @Override
    public WarehouseDto create(WarehouseDto dto) {
        Warehouse entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public WarehouseDto update(Long id, WarehouseDto dto) {
        Warehouse existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + id));

        Warehouse updatedEntity = mapper.toEntity(dto);
        updatedEntity.setId(id);

        return mapper.toDto(repository.save(updatedEntity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Warehouse not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<WarehouseDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public WarehouseDto getById(Long id) {
        Warehouse entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + id));
        return mapper.toDto(entity);
    }
}
