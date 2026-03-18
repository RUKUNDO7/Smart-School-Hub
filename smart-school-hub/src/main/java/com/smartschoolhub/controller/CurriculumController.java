package com.smartschoolhub.controller;

import com.smartschoolhub.domain.Curriculum;
import com.smartschoolhub.service.CurriculumService;
import com.smartschoolhub.service.dto.CurriculumRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curriculum")
public class CurriculumController {
    private final CurriculumService curriculumService;

    public CurriculumController(CurriculumService curriculumService) {
        this.curriculumService = curriculumService;
    }

    @GetMapping("/class/{classId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT','PARENT')")
    public List<Curriculum> getByClass(@PathVariable Long classId) {
        return curriculumService.getCurriculumByClass(classId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Curriculum> createOrUpdate(@Valid @RequestBody CurriculumRequest request) {
        return new ResponseEntity<>(curriculumService.createOrUpdateCurriculum(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        curriculumService.deleteCurriculum(id);
        return ResponseEntity.noContent().build();
    }
}
