package com.example.internal_management_system.modules.hrm.controller;

import com.example.internal_management_system.modules.hrm.dto.DepartmentDto;
import com.example.internal_management_system.modules.hrm.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final Department    Service service;

    /**
     * Tạo mới Department - chỉ ADMIN và HR có quyền
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> create(@RequestBody DepartmentDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    /**
     * Cập nhật Department - chỉ ADMIN và HR có quyền
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody DepartmentDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * Xóa Department - chỉ ADMIN và HR có quyền
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Lấy danh sách tất cả Departments - chỉ ADMIN và HR có quyền
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * Lấy Department theo ID - chỉ ADMIN và HR có quyền
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}

