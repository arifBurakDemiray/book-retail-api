package com.bookretail.util.service.notification;

public interface INotification<T, V> {
    T getRecipient();

    V getMessage();
}
