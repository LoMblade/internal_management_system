package com.example.internal_management_system.modules.hrm.service;

import com.example.internal_management_system.modules.hrm.dto.PayrollDto;

import java.util.List;

public interface PayrollService {

    PayrollDto create(PayrollDto dto);

    PayrollDto update(Long id, PayrollDto dto);

    void delete(Long id);

    List<PayrollDto> getAll();

    PayrollDto getById(Long id);
}
