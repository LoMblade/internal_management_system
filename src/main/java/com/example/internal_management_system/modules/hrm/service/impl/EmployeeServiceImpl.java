package com.example.internal_management_system.modules.hrm.service.impl;

import com.example.internal_management_system.common.exceptions.ResourceNotFoundException;
import com.example.internal_management_system.modules.hrm.dto.EmployeeDto;
import com.example.internal_management_system.modules.hrm.mapper.EmployeeMapper;
import com.example.internal_management_system.modules.hrm.model.Employee;
import com.example.internal_management_system.modules.hrm.repository.EmployeeRepository;
import com.example.internal_management_system.modules.hrm.service.EmployeeService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    @Override
    public EmployeeDto create(EmployeeDto dto) {
        Employee entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public EmployeeDto update(Long id, EmployeeDto dto) {
        Employee existingEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));

        Employee updatedEntity = mapper.toEntity(dto);
        updatedEntity.setId(id);

        return mapper.toDto(repository.save(updatedEntity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Employee", id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<EmployeeDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách employees đã được filter theo quyền của user hiện tại
     * Hiện tại: tất cả HR users đều thấy tất cả employees
     */
    @Override
    public List<EmployeeDto> getAllFiltered() {
        // TODO: Implement filtering logic based on user role and permissions
        // For now, return all employees (same as getAll)
        // Future enhancement: filter by department for department managers, etc.
        return getAll();
    }

    @Override
    public EmployeeDto getById(Long id) {
        Employee entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
        return mapper.toDto(entity);
    }
}

