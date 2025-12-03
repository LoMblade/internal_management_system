package com.example.internal_management_system.modules.warehouse.service.impl;

import com.example.internal_management_system.modules.warehouse.dto.StockExportDto;
import com.example.internal_management_system.modules.warehouse.mapper.StockExportMapper;
import com.example.internal_management_system.modules.warehouse.model.StockExport;
import com.example.internal_management_system.modules.warehouse.repository.StockExportRepository;
import com.example.internal_management_system.modules.warehouse.service.StockExportService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockExportServiceImpl implements StockExportService {

    private final StockExportRepository repository;
    private final StockExportMapper mapper;

    @Override
    public StockExportDto create(StockExportDto dto) {
        StockExport entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public StockExportDto update(Long id, StockExportDto dto) {
        StockExport existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("StockExport not found with id: " + id));

        StockExport updatedEntity = mapper.toEntity(dto);
        updatedEntity.setId(id);

        return mapper.toDto(repository.save(updatedEntity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("StockExport not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<StockExportDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public StockExportDto getById(Long id) {
        StockExport entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("StockExport not found with id: " + id));
        return mapper.toDto(entity);
    }
}
