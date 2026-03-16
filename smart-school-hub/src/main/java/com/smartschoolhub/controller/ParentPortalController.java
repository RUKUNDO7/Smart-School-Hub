package com.smartschoolhub.controller;

import com.smartschoolhub.service.ParentPortalService;
import com.smartschoolhub.service.dto.StudentProgress;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parent-portal")
public class ParentPortalController {
    private final ParentPortalService parentPortalService;

    public ParentPortalController(ParentPortalService parentPortalService) {
        this.parentPortalService = parentPortalService;
    }

    @GetMapping("/students/{studentId}/progress")
    @PreAuthorize("hasAnyRole('ADMIN','PARENT','STUDENT')")
    public StudentProgress getStudentProgress(@PathVariable Long studentId) {
        return parentPortalService.getStudentProgress(studentId);
    }
}
