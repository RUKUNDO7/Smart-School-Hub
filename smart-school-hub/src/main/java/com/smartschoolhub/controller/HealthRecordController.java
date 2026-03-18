package com.smartschoolhub.controller;

import com.smartschoolhub.domain.HealthRecord;
import com.smartschoolhub.service.HealthRecordService;
import com.smartschoolhub.service.dto.HealthRecordRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/health-records")
public class HealthRecordController {
    private final HealthRecordService healthRecordService;

    public HealthRecordController(HealthRecordService healthRecordService) {
        this.healthRecordService = healthRecordService;
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER', 'NURSE')")
    public List<HealthRecord> getByStudent(@PathVariable Long studentId) {
        return healthRecordService.getRecordsByStudent(studentId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    public ResponseEntity<HealthRecord> createRecord(@Valid @RequestBody HealthRecordRequest request) {
        return new ResponseEntity<>(healthRecordService.createRecord(request), HttpStatus.CREATED);
    }
}
