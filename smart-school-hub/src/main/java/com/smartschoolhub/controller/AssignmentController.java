package com.smartschoolhub.controller;

import com.smartschoolhub.domain.Assignment;
import com.smartschoolhub.domain.Submission;
import com.smartschoolhub.service.AssignmentService;
import com.smartschoolhub.service.dto.AssignmentRequest;
import com.smartschoolhub.service.dto.SubmissionRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {
    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT','PARENT')")
    public List<Assignment> getAll() {
        return assignmentService.getAllAssignments();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<Assignment> create(@Valid @RequestBody AssignmentRequest request) {
        return new ResponseEntity<>(assignmentService.createAssignment(request), HttpStatus.CREATED);
    }

    @PostMapping("/submit")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<Submission> submit(@Valid @RequestBody SubmissionRequest request) {
        return new ResponseEntity<>(assignmentService.createSubmission(request), HttpStatus.CREATED);
    }

    @GetMapping("/{assignmentId}/submissions")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<Submission> getSubmissions(@PathVariable Long assignmentId) {
        return assignmentService.getSubmissionsByAssignment(assignmentId);
    }
}
