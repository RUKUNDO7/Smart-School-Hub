package com.smartschoolhub.controller;

import com.smartschoolhub.domain.TimetableEntry;
import com.smartschoolhub.service.TimetableService;
import com.smartschoolhub.service.dto.TimetableEntryRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
public class TimetableController {
    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<TimetableEntry> getAll() {
        return timetableService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public TimetableEntry getById(@PathVariable Long id) {
        return timetableService.getById(id);
    }

    @GetMapping("/class/{classId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<TimetableEntry> getByClass(@PathVariable Long classId) {
        return timetableService.getByClass(classId);
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<TimetableEntry> getByTeacher(@PathVariable Long teacherId) {
        return timetableService.getByTeacher(teacherId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<TimetableEntry> create(@Valid @RequestBody TimetableEntryRequest request) {
        return new ResponseEntity<>(timetableService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public TimetableEntry update(@PathVariable Long id, @Valid @RequestBody TimetableEntryRequest request) {
        return timetableService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timetableService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
