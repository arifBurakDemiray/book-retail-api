package com.bookretail.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.bookretail.event.NotificationEvent;

@Component
@RequiredArgsConstructor
public abstract class NotificationEventHandler {

    @EventListener
    public abstract void onNotificationSent(NotificationEvent event);
}
