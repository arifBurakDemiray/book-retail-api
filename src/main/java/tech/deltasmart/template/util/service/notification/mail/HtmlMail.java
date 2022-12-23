package com.bookretail.util.service.notification.mail;

import java.util.Map;

public class HtmlMail extends Mail {
    private final Map<String, Object> variables;

    public HtmlMail(String recipient, String title, String template, Map<String, Object> variables) {
        super(recipient, title, template);
        this.variables = variables;
    }

    public Map<String, Object> getVariables() {
        return Map.copyOf(variables);
    }
}
