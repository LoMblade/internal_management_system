package com.example.internal_management_system.modules.hrm.service;

import com.example.internal_management_system.modules.hrm.dto.AttendanceDto;

import java.util.List;

public interface AttendanceService {

    AttendanceDto create(AttendanceDto dto);

    AttendanceDto update(Long id, AttendanceDto dto);

    void delete(Long id);

    List<AttendanceDto> getAll();

    AttendanceDto getById(Long id);
}
