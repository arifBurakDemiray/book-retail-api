package com.bookretail.controller;

import com.bookretail.dto.PageFilter;
import com.bookretail.dto.Response;
import com.bookretail.dto.message.BasicResponse;
import com.bookretail.dto.notification.WebNotificationDto;
import com.bookretail.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = NotificationController.tag, description = NotificationController.description)
@RequestMapping(NotificationController.tag)
@AllArgsConstructor
public class NotificationController {
    public static final String description = "Send notifications to users and all users";
    public static final String tag = "notifications";

    private final NotificationService notificationService;

    @PostMapping("all")
    @Operation(summary = "Send notification to all users")
    public ResponseEntity<Response<BasicResponse>> sendNotificationToAllUsers(
            @RequestParam String content,
            @RequestParam String heading,
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return notificationService.sendMessageToAll(content, heading, token).toResponseEntity();
    }


    @GetMapping
    @Operation(summary = "Get sent notifications")
    public ResponseEntity<Response<Page<WebNotificationDto>>> getNotifications(
            PageFilter pageFilter,
            @Parameter(hidden = true) @RequestHeader(name = HttpHeaders.AUTHORIZATION, required = false) String token
    ) {
        return notificationService.getSentMessages(pageFilter, token).toResponseEntity();
    }
}
