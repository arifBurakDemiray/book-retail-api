package com.bookretail.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.bookretail.event.NotificationEvent;
import com.bookretail.service.OneSignalNotificationService;
import com.bookretail.util.LoggableFuture;

@Component
@RequiredArgsConstructor
public class OneSignalNotificationEventHandler extends NotificationEventHandler {

    private final OneSignalNotificationService oneSignalNotificationService;

    @Override
    public void onNotificationSent(NotificationEvent event) {
        LoggableFuture.runAsync(() -> oneSignalNotificationService
                .saveNotifications(event.getNotification(), event.getUsers(), event.getResult()));
    }
}
