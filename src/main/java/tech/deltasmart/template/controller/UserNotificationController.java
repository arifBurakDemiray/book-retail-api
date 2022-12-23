package com.bookretail.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bookretail.dto.PageFilter;
import com.bookretail.dto.Response;
import com.bookretail.dto.message.BasicResponse;
import com.bookretail.dto.notification.UserNotificationDto;
import com.bookretail.enums.ERole;
import com.bookretail.service.UserNotificationService;

import javax.annotation.security.RolesAllowed;

@RestController
@Api(tags = UserNotificationController.tag)
@RequestMapping(NotificationController.tag)
@AllArgsConstructor
@RolesAllowed(ERole.USER)
public class UserNotificationController {

    public static final String description = "Get user notifications and crud operations";
    public static final String tag = "user-notifications";
    private final UserNotificationService userNotificationService;

    @GetMapping("me")
    @ApiOperation(value = "Get user notifications")
    public ResponseEntity<Response<Page<UserNotificationDto>>> getUserNotifications(
            PageFilter pageFilter,
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return userNotificationService.getUserMessages(pageFilter, token).toResponseEntity();
    }

    @GetMapping("me/{id}")
    @ApiOperation(value = "Get user notification by one signal id")
    public ResponseEntity<Response<UserNotificationDto>> getUserNotification(
            @PathVariable String id,
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return userNotificationService.getUserNotification(id, token).toResponseEntity();
    }

    @GetMapping("me/unread")
    @ApiOperation(value = "Get users unread notification count")
    public ResponseEntity<Response<Long>> getUserUnreadNotificationCount(
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return userNotificationService.getUserUnreadNotificationCount(token).toResponseEntity();
    }

    @PatchMapping("me/{id}")
    @ApiOperation(value = "Read user notification")
    public ResponseEntity<Response<BasicResponse>> readNotification(
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable Long id
    ) {
        return userNotificationService.readNotification(id, token).toResponseEntity();
    }

    @DeleteMapping("me/{id}")
    @ApiOperation(value = "Delete user notification")
    public ResponseEntity<Response<BasicResponse>> deleteNotification(
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token,
            @PathVariable Long id
    ) {
        return userNotificationService.deleteNotification(id, token).toResponseEntity();
    }
}
