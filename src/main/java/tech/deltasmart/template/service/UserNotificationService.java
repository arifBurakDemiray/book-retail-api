package com.bookretail.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bookretail.config.security.JwtUtil;
import com.bookretail.dto.PageFilter;
import com.bookretail.dto.Response;
import com.bookretail.dto.message.BasicResponse;
import com.bookretail.dto.notification.UserNotificationDto;
import com.bookretail.enums.EErrorCode;
import com.bookretail.factory.UserNotificationFactory;
import com.bookretail.repository.UserNotificationRepository;
import com.bookretail.validator.UserNotificationValidator;

import java.util.Date;

@Service
@AllArgsConstructor
@Slf4j
public class UserNotificationService {

    private final UserNotificationFactory userNotificationFactory;
    private final UserNotificationRepository userNotificationRepository;
    private final UserNotificationValidator userNotificationValidator;
    private final MessageSourceAccessor messageSource;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public Response<Page<UserNotificationDto>> getUserMessages(PageFilter pageFilter, String token) {

        var userId = jwtUtil.getUserId(token);

        var pagedMessages = userNotificationRepository
                .findAll((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(
                        root.get("user").get("id"), userId
                ), pageFilter.asPageable());

        return Response.ok(pagedMessages
                .map(userNotificationFactory::createUserNotificationDto));
    }

    @Transactional
    public Response<BasicResponse> readNotification(Long id, String token) {

        var userId = jwtUtil.getUserId(token);

        var validation = userNotificationValidator.validate(userId, id);

        if (validation.isNotValid()) {
            return Response.notOk(validation.getMessage(), EErrorCode.BAD_REQUEST);
        }

        var message = userNotificationRepository.getById(id);

        message.setRead(true);
        message.setReadAt(new Date());

        return Response.ok(new BasicResponse(messageSource
                .getMessage("user_notification.read.success")));

    }

    @Transactional
    public Response<BasicResponse> deleteNotification(Long id, String token) {

        var userId = jwtUtil.getUserId(token);

        var validation = userNotificationValidator.validate(userId, id);

        if (validation.isNotValid()) {
            return Response.notOk(validation.getMessage(), EErrorCode.BAD_REQUEST);
        }

        var message = userNotificationRepository.getById(id);

        userNotificationRepository.delete(message);

        return Response.ok(new BasicResponse(messageSource
                .getMessage("user_notification.delete.success")));
    }

    public Response<Long> getUserUnreadNotificationCount(String token) {

        var userId = jwtUtil.getUserId(token);

        var count = userNotificationRepository.count((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.and(criteriaBuilder.equal(root.get("user").get("id"), userId),
                        criteriaBuilder.isFalse(root.get("read"))));

        return Response.ok(count);
    }

    public Response<UserNotificationDto> getUserNotification(String id, String token) {

        var userId = jwtUtil.getUserId(token);

        var message = userNotificationRepository.
                getByUserIdAndOneSignalNotification_OneSignalId(userId, id);

        return Response.ok(userNotificationFactory.createUserNotificationDto(message));
    }
}
