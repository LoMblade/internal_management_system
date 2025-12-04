package com.example.internal_management_system.modules.warehouse.controller;

import com.example.internal_management_system.modules.warehouse.dto.StockExportDto;
import com.example.internal_management_system.modules.warehouse.service.StockExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-exports")
@RequiredArgsConstructor
public class StockExportController {

    private final StockExportService service;

    /**
     * Tạo mới StockExport - chỉ ADMIN và WAREHOUSE có quyền
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")
    public ResponseEntity<?> create(@RequestBody StockExportDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    /**
     * Cập nhật StockExport - ADMIN và WAREHOUSE có thể edit tất cả,
     * Owner có thể edit record của mình nếu chưa được approve
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE') or @securityService.canModifyStockExport(#id)")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody StockExportDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * Xóa StockExport - chỉ ADMIN và WAREHOUSE có quyền
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Lấy danh sách tất cả StockExports - chỉ ADMIN và WAREHOUSE có quyền
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(service.getAll());
    }

    /**
     * Lấy danh sách StockExports đã được filter theo quyền của user hiện tại
     */
    @GetMapping("/filtered")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")
    public ResponseEntity<?> listFiltered() {
        return ResponseEntity.ok(service.getAllFiltered());
    }

    /**
     * Lấy danh sách StockExports được tạo bởi user hiện tại
     */
    @GetMapping("/my-records")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")
    public ResponseEntity<?> getMyRecords() {
        return ResponseEntity.ok(service.getMyRecords());
    }

    /**
     * Lấy StockExport theo ID - chỉ ADMIN và WAREHOUSE có quyền
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}

