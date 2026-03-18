package com.smartschoolhub.controller;

import com.smartschoolhub.domain.Fee;
import com.smartschoolhub.service.FeeService;
import com.smartschoolhub.service.dto.FeeRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/fees")
public class FeeController {
    private final FeeService feeService;

    public FeeController(FeeService feeService) {
        this.feeService = feeService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','PARENT')")
    public Page<Fee> getAll(Pageable pageable) {
        return feeService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Fee getById(@PathVariable Long id) {
        return feeService.getById(id);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Fee> getByStudent(@PathVariable Long studentId) {
        return feeService.getByStudent(studentId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Fee> create(@Valid @RequestBody FeeRequest request) {
        return new ResponseEntity<>(feeService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Fee update(@PathVariable Long id, @Valid @RequestBody FeeRequest request) {
        return feeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        feeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
