package com.bookretail.util.service.notification.mail;

import com.bookretail.util.service.notification.ILocalizedNotification;

public class LocalizedTextMail extends TextMail implements ILocalizedNotification {
    public LocalizedTextMail(String recipient, String subject, String message) {
        super(recipient, subject, message);
    }
}
