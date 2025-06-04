package com.se114p12.backend.services.notification;

import com.se114p12.backend.dtos.nofitication.NotificationRequestDTO;
import com.se114p12.backend.dtos.nofitication.NotificationResponseDTO;
import com.se114p12.backend.entities.notification.Notification;
import com.se114p12.backend.entities.notification.NotificationUser;
import com.se114p12.backend.entities.notification.NotificationUserId;
import com.se114p12.backend.entities.user.User;
import com.se114p12.backend.mappers.notification.NotificationMapper;
import com.se114p12.backend.repositories.authentication.UserRepository;
import com.se114p12.backend.repositories.notification.EmitterRepository;
import com.se114p12.backend.repositories.notification.NotificationRepository;
import com.se114p12.backend.repositories.notification.NotificationUserRepository;
import com.se114p12.backend.vo.PageVO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmitterRepository emitterRepository;
    private final NotificationUserRepository notificationUserRepository;
    private final NotificationMapper mapper;

    @Override
    public NotificationResponseDTO pushNotification(NotificationRequestDTO request) {
        Notification notification = new Notification()
                .setType(request.getType())
                .setTitle(request.getTitle())
                .setMessage(request.getMessage())
                .setStatus(request.getStatus() != null ? request.getStatus() : 1);

        notification = notificationRepository.save(notification);

        for (Long userId : request.getUserIds()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            NotificationUser notificationUser = new NotificationUser();
            notificationUser.setNotification(notification);
            notificationUser.setUser(user);
            notification.getReceivers().add(notificationUser);

            Notification finalNotification = notification;

            emitterRepository.get(user.getUsername()).ifPresent(emitter -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name("notification")
                            .data(toResponse(finalNotification, user.getId())));
                } catch (IOException e) {
                    log.warn("Failed to send notification to {}", user.getUsername());
                    emitterRepository.remove(user.getUsername());
                }
            });
        }

        notificationRepository.save(notification); // Save with receivers

        return toResponse(notification, null);
    }

    @Override
    public PageVO<NotificationResponseDTO> getAll(Pageable pageable) {
        Page<Notification> page = notificationRepository.findAll(pageable);

        List<NotificationResponseDTO> content = page
                .map(notification -> toResponse(notification, null))
                .getContent();

        return PageVO.<NotificationResponseDTO>builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .numberOfElements(page.getNumberOfElements())
                .content(content)
                .build();
    }


    @Override
    public PageVO<NotificationResponseDTO> getNotificationsByUserId(Long userId, Pageable pageable) {
        List<Long> notificationIds = notificationUserRepository.findByUserId(userId)
                .stream()
                .map(nu -> nu.getNotification().getId())
                .toList();

        Page<Notification> page = notificationRepository.findByIdIn(notificationIds, pageable);

        List<NotificationResponseDTO> content = page
                .map(notification -> toResponse(notification, userId))
                .getContent();

        return PageVO.<NotificationResponseDTO>builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .numberOfElements(page.getNumberOfElements())
                .content(content)
                .build();
    }


    @Override
    public void markAsRead(Long userId, Long notificationId) {
        NotificationUser nu = notificationUserRepository.findById(new NotificationUserId(userId, notificationId))
                .orElseThrow(() -> new RuntimeException("Notification not found for user"));
        nu.setRead(true);
        notificationUserRepository.save(nu);
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<NotificationUser> notificationUsers = notificationUserRepository.findByUserId(userId);
        for (NotificationUser nu : notificationUsers) {
            if (!nu.isRead()) {
                nu.setRead(true);
            }
        }
        notificationUserRepository.saveAll(notificationUsers);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Override
    public long countUnreadByUserId(Long userId) {
        return notificationUserRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Override
    public NotificationResponseDTO toResponse(Notification notification, Long userId) {
        NotificationResponseDTO response = mapper.toDTO(notification);
        response.setUserId(userId);
        return response;
    }
}