package com.example.internal_management_system.modules.hrm.service.impl;

import com.example.internal_management_system.modules.hrm.dto.DepartmentDto;
import com.example.internal_management_system.modules.hrm.mapper.DepartmentMapper;
import com.example.internal_management_system.modules.hrm.model.Department;
import com.example.internal_management_system.modules.hrm.repository.DepartmentRepository;
import com.example.internal_management_system.modules.hrm.service.DepartmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;
    private final DepartmentMapper mapper;

    @Override
    public DepartmentDto create(DepartmentDto dto) {
        Department entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public DepartmentDto update(Long id, DepartmentDto dto) {
        Department existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        Department updatedEntity = mapper.toEntity(dto);
        updatedEntity.setId(id);

        return mapper.toDto(repository.save(updatedEntity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Department not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<DepartmentDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDto getById(Long id) {
        Department entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        return mapper.toDto(entity);
    }
}
