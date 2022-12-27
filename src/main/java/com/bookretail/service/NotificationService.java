package com.bookretail.service;

import com.bookretail.config.security.JwtUtil;
import com.bookretail.dto.PageFilter;
import com.bookretail.dto.Response;
import com.bookretail.dto.message.BasicResponse;
import com.bookretail.dto.notification.WebNotificationDto;
import com.bookretail.enums.EErrorCode;
import com.bookretail.event.OneSignalNotificationEvent;
import com.bookretail.factory.NotificationFactory;
import com.bookretail.model.MobileDevice;
import com.bookretail.model.OneSignalNotification;
import com.bookretail.model.User;
import com.bookretail.repository.MobileDeviceRepository;
import com.bookretail.repository.OneSignalNotificationRepository;
import com.bookretail.repository.UserRepository;
import com.bookretail.util.service.notification.NotificationException;
import com.bookretail.util.service.notification.onesignal.OneSignalService;
import com.bookretail.util.service.notification.onesignal.helper.OneSignalCreateNotificationResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {

    private final OneSignalService oneSignalService;
    private final MessageSourceAccessor messageSource;
    private final NotificationFactory notificationFactory;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OneSignalNotificationRepository oneSignalNotificationRepository;
    private final MobileDeviceRepository mobileDeviceRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Response<BasicResponse> sendMessageToAll(String content, String heading, String token) {

        var user = userRepository.getById(jwtUtil.getUserId(token));

        var notification = notificationFactory
                .createWebNotification(content, heading, user);

        var allUsers = mobileDeviceRepository.findAll().stream()
                .map(MobileDevice::getUser).collect(Collectors.toSet());

        return handlePushNotification(notification, allUsers);
    }

    private Response<BasicResponse> handlePushNotification(OneSignalNotification notification, Set<User> recipents) {

        var mobileNotification = notificationFactory.createPersonalNotification(notification, recipents);

        if (mobileNotification == null) {
            return Response.notOk(messageSource.getMessage("push_notification.no_player_found"),
                    EErrorCode.BAD_REQUEST);
        }

        try {

            var notificationResult = (OneSignalCreateNotificationResult) oneSignalService.send(mobileNotification);

            applicationEventPublisher.publishEvent(
                    new OneSignalNotificationEvent(this, recipents, notification, notificationResult));

            return Response.ok(new BasicResponse(messageSource.getMessage("send_all.push_notification.success")));

        } catch (NotificationException e) {

            log.error("NotificationService line 84 " + e.getMessage() + " " + e.getCause());
            return Response.notOk(messageSource.getMessage("send_all.push_notification.fail"),
                    EErrorCode.BAD_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    public Response<Page<WebNotificationDto>> getSentMessages(@NotNull PageFilter pageFilter, String token) {

        var userId = jwtUtil.getUserId(token);

        var pagedMessages = oneSignalNotificationRepository
                .findAll((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(
                        root.get("user").get("id"), userId
                ), pageFilter.asPageable());

        return Response.ok(pagedMessages
                .map(notificationFactory::createWebNotificationDto));

    }
}
