package com.example.internal_management_system.modules.hrm.service.impl;

import com.example.internal_management_system.modules.hrm.dto.PayrollDto;
import com.example.internal_management_system.modules.hrm.mapper.PayrollMapper;
import com.example.internal_management_system.modules.hrm.model.Payroll;
import com.example.internal_management_system.modules.hrm.repository.PayrollRepository;
import com.example.internal_management_system.modules.hrm.service.PayrollService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository repository;
    private final PayrollMapper mapper;

    @Override
    public PayrollDto create(PayrollDto dto) {
        Payroll entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public PayrollDto update(Long id, PayrollDto dto) {
        Payroll existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with id: " + id));

        Payroll updatedEntity = mapper.toEntity(dto);
        updatedEntity.setId(id);

        return mapper.toDto(repository.save(updatedEntity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Payroll not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<PayrollDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PayrollDto getById(Long id) {
        Payroll entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payroll not found with id: " + id));
        return mapper.toDto(entity);
    }
}
