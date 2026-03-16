package com.smartschoolhub.controller;

import com.smartschoolhub.domain.TeacherEvaluation;
import com.smartschoolhub.service.TeacherEvaluationService;
import com.smartschoolhub.service.dto.TeacherEvaluationRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher-evaluations")
public class TeacherEvaluationController {
    private final TeacherEvaluationService teacherEvaluationService;

    public TeacherEvaluationController(TeacherEvaluationService teacherEvaluationService) {
        this.teacherEvaluationService = teacherEvaluationService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<TeacherEvaluation> getAll() {
        return teacherEvaluationService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TeacherEvaluation getById(@PathVariable Long id) {
        return teacherEvaluationService.getById(id);
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TeacherEvaluation> getByTeacher(@PathVariable Long teacherId) {
        return teacherEvaluationService.getByTeacher(teacherId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeacherEvaluation> create(@Valid @RequestBody TeacherEvaluationRequest request) {
        return new ResponseEntity<>(teacherEvaluationService.create(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public TeacherEvaluation update(@PathVariable Long id, @Valid @RequestBody TeacherEvaluationRequest request) {
        return teacherEvaluationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherEvaluationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
