package com.example.internal_management_system.modules.warehouse.service.impl;

import com.example.internal_management_system.modules.warehouse.dto.StockImportDto;
import com.example.internal_management_system.modules.warehouse.mapper.StockImportMapper;
import com.example.internal_management_system.modules.warehouse.model.StockImport;
import com.example.internal_management_system.modules.warehouse.repository.StockImportRepository;
import com.example.internal_management_system.modules.warehouse.service.StockImportService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockImportServiceImpl implements StockImportService {

    private final StockImportRepository repository;
    private final StockImportMapper mapper;

    @Override
    public StockImportDto create(StockImportDto dto) {
        StockImport entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public StockImportDto update(Long id, StockImportDto dto) {
        StockImport existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("StockImport not found with id: " + id));

        StockImport updatedEntity = mapper.toEntity(dto);
        updatedEntity.setId(id);

        return mapper.toDto(repository.save(updatedEntity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("StockImport not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<StockImportDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public StockImportDto getById(Long id) {
        StockImport entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("StockImport not found with id: " + id));
        return mapper.toDto(entity);
    }
}
