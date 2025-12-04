<<<<<<< Current (Your changes)
package com.example.internal_management_system.modules.hrm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.internal_management_system.modules.hrm.dto.EmployeeDto;
import com.example.internal_management_system.modules.hrm.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService service;

    /**
     * Tạo mới Employee - chỉ ADMIN và HR có quyền
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> create(@RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    /**
     * Cập nhật Employee - chỉ ADMIN và HR có quyền
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * Xóa Employee - chỉ ADMIN và HR có quyền
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Lấy danh sách tất cả Employees - chỉ ADMIN và HR có quyền
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * Lấy danh sách Employees đã được filter theo quyền của user hiện tại
     * - ADMIN: thấy tất cả
     * - HR: thấy tất cả employees
     */
    @GetMapping("/filtered")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> listFiltered() {
        return ResponseEntity.ok(service.getAllFiltered());
    }

    /**
     * Lấy Employee theo ID - chỉ ADMIN và HR có quyền
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
=======
 
>>>>>>> Incoming (Background Agent changes)
