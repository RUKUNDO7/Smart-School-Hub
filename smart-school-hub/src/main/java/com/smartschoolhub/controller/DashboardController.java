package com.smartschoolhub.controller;

import com.smartschoolhub.service.DashboardService;
import com.smartschoolhub.service.dto.DashboardSummary;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public DashboardSummary getSummary() {
        return dashboardService.getSummary();
    }
}
