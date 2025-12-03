package com.example.internal_management_system.modules.hrm.service;

import com.example.internal_management_system.modules.hrm.dto.DepartmentDto;

import java.util.List;

public interface DepartmentService {

    DepartmentDto create(DepartmentDto dto);

    DepartmentDto update(Long id, DepartmentDto dto);

    void delete(Long id);

    List<DepartmentDto> getAll();

    DepartmentDto getById(Long id);
}
