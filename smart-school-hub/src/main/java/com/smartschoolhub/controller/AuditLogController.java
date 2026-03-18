package com.smartschoolhub.controller;

import com.smartschoolhub.domain.AuditLog;
import com.smartschoolhub.service.AuditLogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {
    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLog> getAll() {
        return auditLogService.getAllLogs();
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLog> getByUser(@PathVariable String username) {
        return auditLogService.getLogsByUser(username);
    }

    @GetMapping("/entity/{entity}/{entityId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLog> getByEntity(@PathVariable String entity, @PathVariable Long entityId) {
        return auditLogService.getLogsByEntity(entity, entityId);
    }
}
