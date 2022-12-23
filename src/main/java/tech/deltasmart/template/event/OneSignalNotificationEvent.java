package com.bookretail.event;

import com.bookretail.model.Notification;
import com.bookretail.model.User;
import com.bookretail.util.service.notification.onesignal.helper.OneSignalCreateNotificationResult;

import java.util.Set;

public class OneSignalNotificationEvent extends NotificationEvent {

    public OneSignalNotificationEvent(Object source, Set<User> users, Notification notification,
                                      OneSignalCreateNotificationResult oneSignalCreateNotificationResult) {
        super(source, users, notification, oneSignalCreateNotificationResult);
    }
}
