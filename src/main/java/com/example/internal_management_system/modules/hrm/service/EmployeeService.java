package com.example.internal_management_system.modules.hrm.service;

import com.example.internal_management_system.modules.hrm.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto create(EmployeeDto dto);

    EmployeeDto update(Long id, EmployeeDto dto);

    void delete(Long id);

    List<EmployeeDto> getAll();

    List<EmployeeDto> getAllFiltered(); // Filtered based on user permissions

    EmployeeDto getById(Long id);
}

