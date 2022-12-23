package com.bookretail.controller;

import com.bookretail.dto.PageFilter;
import com.bookretail.dto.Response;
import com.bookretail.dto.message.BasicResponse;
import com.bookretail.dto.notification.UserNotificationDto;
import com.bookretail.enums.ERole;
import com.bookretail.service.UserNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@Tag(name = UserNotificationController.tag, description = UserNotificationController.description)
@RequestMapping(NotificationController.tag)
@AllArgsConstructor
@RolesAllowed(ERole.USER)
public class UserNotificationController {

    public static final String description = "Get user notifications and crud operations";
    public static final String tag = "user-notifications";
    private final UserNotificationService userNotificationService;

    @GetMapping("me")
    @Operation(summary = "Get user notifications")
    public ResponseEntity<Response<Page<UserNotificationDto>>> getUserNotifications(
            PageFilter pageFilter,
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return userNotificationService.getUserMessages(pageFilter, token).toResponseEntity();
    }

    @GetMapping("me/{id}")
    @Operation(summary = "Get user notification by one signal id")
    public ResponseEntity<Response<UserNotificationDto>> getUserNotification(
            @PathVariable String id,
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return userNotificationService.getUserNotification(id, token).toResponseEntity();
    }

    @GetMapping("me/unread")
    @Operation(summary = "Get users unread notification count")
    public ResponseEntity<Response<Long>> getUserUnreadNotificationCount(
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return userNotificationService.getUserUnreadNotificationCount(token).toResponseEntity();
    }

    @PatchMapping("me/{id}")
    @Operation(summary = "Read user notification")
    public ResponseEntity<Response<BasicResponse>> readNotification(
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable Long id
    ) {
        return userNotificationService.readNotification(id, token).toResponseEntity();
    }

    @DeleteMapping("me/{id}")
    @Operation(summary = "Delete user notification")
    public ResponseEntity<Response<BasicResponse>> deleteNotification(
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable Long id
    ) {
        return userNotificationService.deleteNotification(id, token).toResponseEntity();
    }
}
