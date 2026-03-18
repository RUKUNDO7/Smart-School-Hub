package com.smartschoolhub.service;

import com.smartschoolhub.domain.AuditLog;
import com.smartschoolhub.repository.AuditLogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String action, String entity, Long entityId, String details) {
        String username = SecurityContextHolder.getContext().getAuthentication() != null ?
            SecurityContextHolder.getContext().getAuthentication().getName() : "SYSTEM";

        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntity(entity);
        log.setEntityId(entityId);
        log.setPerformedBy(username);
        log.setTimestamp(LocalDateTime.now());
        log.setDetails(details);

        auditLogRepository.save(log);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }

    public List<AuditLog> getLogsByUser(String username) {
        return auditLogRepository.findByPerformedByOrderByTimestampDesc(username);
    }

    public List<AuditLog> getLogsByEntity(String entity, Long entityId) {
        return auditLogRepository.findByEntityAndEntityIdOrderByTimestampDesc(entity, entityId);
    }
}
