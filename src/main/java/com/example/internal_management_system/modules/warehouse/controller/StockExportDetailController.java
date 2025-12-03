package com.example.internal_management_system.modules.warehouse.controller;

import com.example.internal_management_system.modules.warehouse.dto.StockExportDetailDto;
import com.example.internal_management_system.modules.warehouse.service.StockExportDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-export-details")
@RequiredArgsConstructor
public class StockExportDetailController {

    private final StockExportDetailService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody StockExportDetailDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody StockExportDetailDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }
}
