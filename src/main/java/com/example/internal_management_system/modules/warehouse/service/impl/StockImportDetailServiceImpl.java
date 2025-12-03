package com.example.internal_management_system.modules.warehouse.service.impl;

import com.example.internal_management_system.modules.warehouse.dto.StockImportDetailDto;
import com.example.internal_management_system.modules.warehouse.mapper.StockImportDetailMapper;
import com.example.internal_management_system.modules.warehouse.model.StockImportDetail;
import com.example.internal_management_system.modules.warehouse.repository.StockImportDetailRepository;
import com.example.internal_management_system.modules.warehouse.service.StockImportDetailService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockImportDetailServiceImpl implements StockImportDetailService {

    private final StockImportDetailRepository repository;
    private final StockImportDetailMapper mapper;

    @Override
    public StockImportDetailDto create(StockImportDetailDto dto) {
        StockImportDetail entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public StockImportDetailDto update(Long id, StockImportDetailDto dto) {
        StockImportDetail existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("StockImportDetail not found with id: " + id));

        StockImportDetail updatedEntity = mapper.toEntity(dto);
        updatedEntity.setId(id);

        return mapper.toDto(repository.save(updatedEntity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("StockImportDetail not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<StockImportDetailDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public StockImportDetailDto getById(Long id) {
        StockImportDetail entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("StockImportDetail not found with id: " + id));
        return mapper.toDto(entity);
    }
}
