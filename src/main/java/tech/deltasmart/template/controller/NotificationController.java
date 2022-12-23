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
import com.bookretail.dto.notification.WebNotificationDto;
import com.bookretail.service.NotificationService;

@RestController
@Api(tags = NotificationController.tag)
@RequestMapping(NotificationController.tag)
@AllArgsConstructor
public class NotificationController {
    public static final String description = "Send notifications to users and all users";
    public static final String tag = "notifications";

    private final NotificationService notificationService;

    @PostMapping("all")
    @ApiOperation(value = "Send notification to all users")
    public ResponseEntity<Response<BasicResponse>> sendNotificationToAllUsers(
            @RequestParam String content,
            @RequestParam String heading,
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return notificationService.sendMessageToAll(content, heading, token).toResponseEntity();
    }


    @GetMapping
    @ApiOperation(value = "Get sent notifications")
    public ResponseEntity<Response<Page<WebNotificationDto>>> getNotifications(
            PageFilter pageFilter,
            @ApiParam(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return notificationService.getSentMessages(pageFilter, token).toResponseEntity();
    }
}
