package com.smartschoolhub.controller;

import com.smartschoolhub.domain.Disbursement;
import com.smartschoolhub.domain.Scholarship;
import com.smartschoolhub.service.ScholarshipService;
import com.smartschoolhub.service.dto.ScholarshipRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/scholarships")
public class ScholarshipController {
    private final ScholarshipService scholarshipService;

    public ScholarshipController(ScholarshipService scholarshipService) {
        this.scholarshipService = scholarshipService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Scholarship> create(@Valid @RequestBody ScholarshipRequest request) {
        return new ResponseEntity<>(scholarshipService.createScholarship(request), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PARENT','STUDENT')")
    public List<Scholarship> getAll() {
        return scholarshipService.getAllScholarships();
    }

    @PostMapping("/disburse")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Disbursement> disburse(@RequestParam Long scholarshipId, 
                                                 @RequestParam Long studentId, 
                                                 @RequestParam BigDecimal baseFee) {
        return new ResponseEntity<>(scholarshipService.disburseScholarship(scholarshipId, studentId, baseFee), HttpStatus.CREATED);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','PARENT','STUDENT')")
    public List<Disbursement> getStudentDisbursements(@PathVariable Long studentId) {
        return scholarshipService.getStudentDisbursements(studentId);
    }
}
