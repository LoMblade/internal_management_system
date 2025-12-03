package com.example.internal_management_system.modules.hrm.service;

import com.example.internal_management_system.modules.hrm.dto.PositionDto;

import java.util.List;

public interface PositionService {

    PositionDto create(PositionDto dto);

    PositionDto update(Long id, PositionDto dto);

    void delete(Long id);

    List<PositionDto> getAll();

    PositionDto getById(Long id);
}
