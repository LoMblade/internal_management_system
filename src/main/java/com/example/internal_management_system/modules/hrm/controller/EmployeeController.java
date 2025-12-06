package com.example.internal_management_system.modules.hrm.controller;

import com.example.internal_management_system.modules.hrm.dto.EmployeeDto;
import com.example.internal_management_system.modules.hrm.service.EmployeeService;
import com.example.internal_management_system.security.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;
    private final PermissionService permissionService;

    /**
     * Tạo mới Employee - cần permission EMPLOYEE_CREATE
     */
    @PostMapping
    @PreAuthorize("hasPermission('EMPLOYEE', 'CREATE')")
    public ResponseEntity<?> create(@Valid @RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    /**
     * Cập nhật Employee - cần permission EMPLOYEE_UPDATE
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission('EMPLOYEE', 'UPDATE')")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * Xóa Employee - cần permission EMPLOYEE_DELETE
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission('EMPLOYEE', 'DELETE')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Lấy danh sách Employees
     * - Có READ_ALL: thấy tất cả
     * - Chỉ có READ_OWN: thấy của chính mình
     */
    @GetMapping
    @PreAuthorize("hasPermission('EMPLOYEE', 'READ_ALL') or hasPermission('EMPLOYEE', 'READ_OWN')")
    public ResponseEntity<?> list() {
        // Kiểm tra quyền và trả về dữ liệu phù hợp
        if (permissionService.canReadAll("EMPLOYEE")) {
            return ResponseEntity.ok(service.getAll());
        } else if (permissionService.canReadOwn("EMPLOYEE")) {
            return ResponseEntity.ok(service.getAllFiltered());
        }
        return ResponseEntity.ok(service.getAllFiltered());
    }

    /**
     * Lấy danh sách Employees đã được filter theo quyền
     */
    @GetMapping("/filtered")
    @PreAuthorize("hasPermission('EMPLOYEE', 'READ_ALL') or hasPermission('EMPLOYEE', 'READ_OWN')")
    public ResponseEntity<?> listFiltered() {
        return ResponseEntity.ok(service.getAllFiltered());
    }

    /**
     * Lấy Employee theo ID
     * - Có READ_ALL: xem được tất cả
     * - Chỉ có READ_OWN: chỉ xem được của chính mình
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission('EMPLOYEE', 'READ_ALL') or @permissionService.canViewEmployee(#id)")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}

