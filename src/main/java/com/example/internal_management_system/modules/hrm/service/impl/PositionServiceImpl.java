package com.example.internal_management_system.modules.hrm.service.impl;

import com.example.internal_management_system.modules.hrm.dto.PositionDto;
import com.example.internal_management_system.modules.hrm.mapper.PositionMapper;
import com.example.internal_management_system.modules.hrm.model.Position;
import com.example.internal_management_system.modules.hrm.repository.PositionRepository;
import com.example.internal_management_system.modules.hrm.service.PositionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository repository;
    private final PositionMapper mapper;

    @Override
    public PositionDto create(PositionDto dto) {
        Position entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PositionDto update(Long id, PositionDto dto) {
        Position existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found with id: " + id));

        Position updatedEntity = mapper.toEntity(dto);
        updatedEntity.setId(id);

        return mapper.toDto(repository.save(updatedEntity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Position not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<PositionDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PositionDto getById(Long id) {
        Position entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found with id: " + id));
        return mapper.toDto(entity);
    }
}
