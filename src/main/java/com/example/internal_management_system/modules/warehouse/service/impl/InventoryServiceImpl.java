package com.example.internal_management_system.modules.warehouse.service.impl;

import com.example.internal_management_system.modules.warehouse.dto.InventoryDto;
import com.example.internal_management_system.modules.warehouse.mapper.InventoryMapper;
import com.example.internal_management_system.modules.warehouse.model.Inventory;
import com.example.internal_management_system.modules.warehouse.repository.InventoryRepository;
import com.example.internal_management_system.modules.warehouse.service.InventoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repository;
    private final InventoryMapper mapper;

    @Override
    public InventoryDto create(InventoryDto dto) {
        Inventory entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public InventoryDto update(Long id, InventoryDto dto) {
        Inventory existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));

        Inventory updatedEntity = mapper.toEntity(dto);
        updatedEntity.setId(id);

        return mapper.toDto(repository.save(updatedEntity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Inventory not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<InventoryDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryDto getById(Long id) {
        Inventory entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
        return mapper.toDto(entity);
    }
}
