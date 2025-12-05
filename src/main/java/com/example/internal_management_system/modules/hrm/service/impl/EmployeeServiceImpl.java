package com.example.internal_management_system.modules.hrm.service.impl;

import com.example.internal_management_system.common.exceptions.ResourceNotFoundException;
import com.example.internal_management_system.modules.hrm.dto.EmployeeDto;
import com.example.internal_management_system.modules.hrm.mapper.EmployeeMapper;
import com.example.internal_management_system.modules.hrm.model.Employee;
import com.example.internal_management_system.modules.hrm.repository.EmployeeRepository;
import com.example.internal_management_system.modules.hrm.service.EmployeeService;
import com.example.internal_management_system.security.service.SecurityService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;
    private final SecurityService securityService;

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
        // Admin, HR, Manager: xem tất cả
        if (securityService.hasAnyRole("ADMIN", "HR", "MANAGER")) {
            return repository.findAll().stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }

        // Staff: chỉ xem chính mình (lọc theo email user hiện tại)
        if (securityService.hasRole("STAFF")) {
            String currentEmail = securityService.getCurrentUserEmail();
            if (currentEmail == null) {
                return List.of();
            }
            return repository.findAll().stream()
                    .filter(emp -> currentEmail.equalsIgnoreCase(emp.getEmail()))
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }

        // Không có role phù hợp: không trả về dữ liệu
        throw new AccessDeniedException("Access denied");
    }

    /**
     * Lấy danh sách employees đã được filter theo quyền của user hiện tại
     * Hiện tại: tất cả HR users đều thấy tất cả employees
     * Có thể mở rộng sau: filter theo department, manager, etc.
     */
    @Override
    public List<EmployeeDto> getAllFiltered() {
        // Tái sử dụng logic getAll: đã filter theo role (Admin/HR/Manager: all; Staff: chỉ bản thân)
        return getAll();
    }

    @Override
    public EmployeeDto getById(Long id) {
        Employee entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));

        // Staff chỉ được xem thông tin của chính mình (theo email)
        if (securityService.hasRole("STAFF")) {
            String currentEmail = securityService.getCurrentUserEmail();
            if (currentEmail == null || !currentEmail.equalsIgnoreCase(entity.getEmail())) {
                throw new AccessDeniedException("Access denied");
            }
        }

        // Manager/HR/Admin được xem tất cả
        return mapper.toDto(entity);
    }
}

