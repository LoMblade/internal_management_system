package com.example.internal_management_system.modules.hrm.service.impl;

import com.example.internal_management_system.modules.hrm.dto.AttendanceDto;
import com.example.internal_management_system.modules.hrm.mapper.AttendanceMapper;
import com.example.internal_management_system.modules.hrm.model.Attendance;
import com.example.internal_management_system.modules.hrm.repository.AttendanceRepository;
import com.example.internal_management_system.modules.hrm.service.AttendanceService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository repository;
    private final AttendanceMapper mapper;

    @Override
    public AttendanceDto create(AttendanceDto dto) {
        Attendance entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public AttendanceDto update(Long id, AttendanceDto dto) {
        Attendance existingEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found with id: " + id));

        Attendance updatedEntity = mapper.toEntity(dto);
        updatedEntity.setId(id);

        return mapper.toDto(repository.save(updatedEntity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Attendance not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<AttendanceDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceDto getById(Long id) {
        Attendance entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found with id: " + id));
        return mapper.toDto(entity);
    }
}
