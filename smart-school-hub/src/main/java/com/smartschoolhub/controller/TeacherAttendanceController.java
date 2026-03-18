package com.smartschoolhub.controller;

import com.smartschoolhub.domain.TeacherAttendance;
import com.smartschoolhub.service.TeacherAttendanceService;
import com.smartschoolhub.service.dto.TeacherAttendanceRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/teacher-attendance")
public class TeacherAttendanceController {
    private final TeacherAttendanceService teacherAttendanceService;

    public TeacherAttendanceController(TeacherAttendanceService teacherAttendanceService) {
        this.teacherAttendanceService = teacherAttendanceService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<TeacherAttendance> getAll() {
        return teacherAttendanceService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public TeacherAttendance getById(@PathVariable Long id) {
        return teacherAttendanceService.getById(id);
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<TeacherAttendance> getByTeacher(@PathVariable Long teacherId) {
        return teacherAttendanceService.getByTeacher(teacherId);
    }

    @GetMapping("/date/{date}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<TeacherAttendance> getByDate(@PathVariable LocalDate date) {
        return teacherAttendanceService.getByDate(date);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<TeacherAttendance> create(@Valid @RequestBody TeacherAttendanceRequest request) {
        return new ResponseEntity<>(teacherAttendanceService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public TeacherAttendance update(@PathVariable Long id, @Valid @RequestBody TeacherAttendanceRequest request) {
        return teacherAttendanceService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherAttendanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
