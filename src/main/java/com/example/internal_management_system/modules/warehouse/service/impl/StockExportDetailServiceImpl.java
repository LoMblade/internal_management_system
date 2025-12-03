package com.example.internal_management_system.modules.warehouse.service.impl;

import com.example.internal_management_system.modules.warehouse.dto.StockExportDetailDto;
import com.example.internal_management_system.modules.warehouse.mapper.StockExportDetailMapper;
import com.example.internal_management_system.modules.warehouse.model.StockExportDetail;
import com.example.internal_management_system.modules.warehouse.repository.StockExportDetailRepository;
import com.example.internal_management_system.modules.warehouse.service.StockExportDetailService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockExportDetailServiceImpl implements StockExportDetailService {

    private final StockExportDetailRepository repository;
    private final StockExportDetailMapper mapper;

    @Override
    public StockExportDetailDto create(StockExportDetailDto dto) {
        StockExportDetail entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public StockExportDetailDto update(Long id, StockExportDetailDto dto) {
        StockExportDetail existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("StockExportDetail not found with id: " + id));

        StockExportDetail updatedEntity = mapper.toEntity(dto);
        updatedEntity.setId(id);

        return mapper.toDto(repository.save(updatedEntity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("StockExportDetail not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<StockExportDetailDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public StockExportDetailDto getById(Long id) {
        StockExportDetail entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("StockExportDetail not found with id: " + id));
        return mapper.toDto(entity);
    }
}
