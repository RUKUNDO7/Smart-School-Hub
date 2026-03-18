package com.smartschoolhub.controller;

import com.smartschoolhub.domain.SchoolClass;
import com.smartschoolhub.service.SchoolClassService;
import com.smartschoolhub.service.dto.SchoolClassRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class SchoolClassController {
    private final SchoolClassService schoolClassService;

    public SchoolClassController(SchoolClassService schoolClassService) {
        this.schoolClassService = schoolClassService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<SchoolClass> getAll() {
        return schoolClassService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public SchoolClass getById(@PathVariable Long id) {
        return schoolClassService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<SchoolClass> create(@Valid @RequestBody SchoolClassRequest request) {
        return new ResponseEntity<>(schoolClassService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public SchoolClass update(@PathVariable Long id, @Valid @RequestBody SchoolClassRequest request) {
        return schoolClassService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        schoolClassService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
