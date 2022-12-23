package com.bookretail.util.service.notification;

import java.util.List;

public interface INotificationService<T extends INotification<?, ?>> {
    INotificationResult send(T notification) throws NotificationException;

    List<INotificationResult> send(List<T> notifications) throws NotificationException;
}
