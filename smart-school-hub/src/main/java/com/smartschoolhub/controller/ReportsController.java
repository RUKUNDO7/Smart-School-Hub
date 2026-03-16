package com.smartschoolhub.controller;

import com.smartschoolhub.service.ReportsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {
    private final ReportsService reportsService;

    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @GetMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> studentReport() {
        return reportsService.studentReport();
    }

    @GetMapping("/classes")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> classReport() {
        return reportsService.classReport();
    }

    @GetMapping("/finances")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> financeReport() {
        return reportsService.financeReport();
    }

    @GetMapping("/attendance")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> attendanceReport() {
        return reportsService.attendanceReport();
    }

    @GetMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> teacherReport() {
        return reportsService.teacherReport();
    }
}
