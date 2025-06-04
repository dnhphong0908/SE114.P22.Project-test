package com.se114p12.backend.controllers.notification;

import com.se114p12.backend.dtos.nofitication.NotificationRequestDTO;
import com.se114p12.backend.dtos.nofitication.NotificationResponseDTO;
import com.se114p12.backend.services.notification.NotificationService;
import com.se114p12.backend.util.JwtUtil;
import com.se114p12.backend.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;

    @PostMapping("/send")
    public ResponseEntity<NotificationResponseDTO> send(@RequestBody NotificationRequestDTO request) {
        return ResponseEntity.ok(notificationService.pushNotification(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageVO<NotificationResponseDTO>> getAllNotifications(Pageable pageable) {
        pageable = pageable.isPaged() ? pageable : Pageable.unpaged();
        PageVO<NotificationResponseDTO> notifications = notificationService.getAll(pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/me")
    public ResponseEntity<PageVO<NotificationResponseDTO>> getMyNotifications(Pageable pageable) {
        pageable = pageable.isPaged() ? pageable : Pageable.unpaged();
        Long userId = jwtUtil.getCurrentUserId();
        PageVO<NotificationResponseDTO> notifications = notificationService.getNotificationsByUserId(userId, pageable);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/me/read-all")
    public ResponseEntity<Void> markAllMyNotificationsAsRead() {
        Long userId = jwtUtil.getCurrentUserId();
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        Long userId = jwtUtil.getCurrentUserId();
        notificationService.markAsRead(userId, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/unread-count")
    public ResponseEntity<Long> countMyUnreadNotifications() {
        Long userId = jwtUtil.getCurrentUserId();
        return ResponseEntity.ok(notificationService.countUnreadByUserId(userId));
    }
}