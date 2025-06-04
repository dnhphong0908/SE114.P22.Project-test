package com.se114p12.backend.services.notification;

import com.se114p12.backend.dtos.nofitication.NotificationRequestDTO;
import com.se114p12.backend.dtos.nofitication.NotificationResponseDTO;
import com.se114p12.backend.entities.notification.Notification;
import com.se114p12.backend.vo.PageVO;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    NotificationResponseDTO pushNotification(NotificationRequestDTO request);
    PageVO<NotificationResponseDTO> getAll(Pageable pageable);
    PageVO<NotificationResponseDTO> getNotificationsByUserId(Long userId, Pageable pageable);
    void markAsRead(Long userId, Long notificationId);
    void markAllAsRead(Long userId);
    void deleteNotification(Long notificationId);
    long countUnreadByUserId(Long userId);
    NotificationResponseDTO toResponse(Notification notification, Long userId);
}
