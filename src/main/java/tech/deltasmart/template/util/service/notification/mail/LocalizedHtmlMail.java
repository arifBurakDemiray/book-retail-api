package com.bookretail.util.service.notification.mail;

import com.bookretail.util.service.notification.ILocalizedNotification;

import java.util.Map;

public class LocalizedHtmlMail extends HtmlMail implements ILocalizedNotification {
    public LocalizedHtmlMail(String recipient, String title, String template, Map<String, Object> variables) {
        super(recipient, title, template, variables);
    }
}
