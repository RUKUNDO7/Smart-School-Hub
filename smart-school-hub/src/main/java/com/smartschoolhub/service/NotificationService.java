package com.smartschoolhub.service;

import com.smartschoolhub.domain.Notification;
import com.smartschoolhub.domain.UserAccount;
import com.smartschoolhub.repository.NotificationRepository;
import com.smartschoolhub.repository.UserAccountRepository;
import com.smartschoolhub.service.dto.NotificationRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserAccountRepository userAccountRepository;

    public NotificationService(NotificationRepository notificationRepository, UserAccountRepository userAccountRepository) {
        this.notificationRepository = notificationRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public List<Notification> getMyNotifications() {
        UserAccount currentUser = getCurrentUser();
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(currentUser);
    }

    public long getUnreadCount() {
        UserAccount currentUser = getCurrentUser();
        return notificationRepository.countByRecipientAndIsReadFalse(currentUser);
    }

    @Transactional
    public Notification sendNotification(NotificationRequest request) {
        UserAccount sender = getCurrentUser();
        UserAccount recipient = userAccountRepository.findById(request.getRecipientId())
            .orElseThrow(() -> new ResourceNotFoundException("Recipient not found: " + request.getRecipientId()));

        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setSender(sender);
        notification.setRecipient(recipient);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        return notificationRepository.save(notification);
    }

    @Transactional
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));

        UserAccount currentUser = getCurrentUser();
        if (!notification.getRecipient().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void delete(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));

        UserAccount currentUser = getCurrentUser();
        if (!notification.getRecipient().getId().equals(currentUser.getId()) &&
            !currentUser.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Access denied");
        }

        notificationRepository.delete(notification);
    }

    private UserAccount getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userAccountRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }
}
