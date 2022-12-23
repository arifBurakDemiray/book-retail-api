package com.bookretail.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import com.bookretail.model.User;

import java.util.Locale;

@Getter
public class ForgotPasswordEvent extends ApplicationEvent {
    private final User user;
    private final Locale locale;

    public ForgotPasswordEvent(Object source, User user, Locale locale) {
        super(source);
        this.user = user;
        this.locale = locale;
    }
}
