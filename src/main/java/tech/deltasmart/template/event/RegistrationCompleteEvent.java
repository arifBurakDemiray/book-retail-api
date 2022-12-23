package com.bookretail.event;

import org.springframework.context.ApplicationEvent;
import com.bookretail.model.User;

import java.util.Locale;

public class RegistrationCompleteEvent extends ApplicationEvent {
    private final Locale locale;
    private final User user;

    public RegistrationCompleteEvent(Object source, User user, Locale locale) {
        super(source);

        this.user = user;
        this.locale = locale;
    }

    public User getUser() {
        return user;
    }

    public Locale getLocale() {
        return locale;
    }
}