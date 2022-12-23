package com.bookretail.util.service.notification.mail;

import com.bookretail.util.service.notification.INotification;

public abstract class Mail implements INotification<String, String> {
    private final String recipient;
    private final String subject;
    private final String message;

    public Mail(String recipient, String subject, String message) {
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
    }

    @Override
    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
