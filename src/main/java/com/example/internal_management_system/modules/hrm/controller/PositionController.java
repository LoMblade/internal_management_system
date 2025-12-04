package com.example.internal_management_system.modules.hrm.controller;

import com.example.internal_management_system.modules.hrm.dto.PositionDto;
import com.example.internal_management_system.modules.hrm.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService service;

    /**
     * Tạo mới Position - chỉ ADMIN và HR có quyền
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> create(@RequestBody PositionDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    /**
     * Cập nhật Position - chỉ ADMIN và HR có quyền
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody PositionDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * Xóa Position - chỉ ADMIN và HR có quyền
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Lấy danh sách tất cả Positions - chỉ ADMIN và HR có quyền
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * Lấy Position theo ID - chỉ ADMIN và HR có quyền
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
