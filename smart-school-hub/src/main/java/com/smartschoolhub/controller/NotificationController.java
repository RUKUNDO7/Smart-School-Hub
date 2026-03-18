package com.smartschoolhub.controller;

import com.smartschoolhub.domain.Notification;
import com.smartschoolhub.service.NotificationService;
import com.smartschoolhub.service.dto.NotificationRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<Notification> getMyNotifications() {
        return notificationService.getMyNotifications();
    }

    @GetMapping("/unread-count")
    @PreAuthorize("isAuthenticated()")
    public long getUnreadCount() {
        return notificationService.getUnreadCount();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<Notification> sendNotification(@Valid @RequestBody NotificationRequest request) {
        return new ResponseEntity<>(notificationService.sendNotification(request), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
