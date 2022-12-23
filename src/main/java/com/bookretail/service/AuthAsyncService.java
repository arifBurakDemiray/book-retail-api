package com.bookretail.service;

import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bookretail.dto.auth.ForgotPasswordRequest;
import com.bookretail.event.ForgotPasswordEvent;
import com.bookretail.repository.UserRepository;

import java.util.Locale;

@Component
@AllArgsConstructor
public class AuthAsyncService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Transactional
    public void forgotPasswordSuccess(ForgotPasswordRequest body, Locale locale) {
        logger.info("Reset password requested for user: {}", body.getEmail());

        try {
            userRepository.findByEmail(body.getEmail()).ifPresent(user -> {
                Hibernate.initialize(user.getVerificationCodes());

                eventPublisher.publishEvent(
                        new ForgotPasswordEvent(this, user, locale)
                );
            });
        } catch (Exception e) {
            logger.error("An error occurred in async method forgotPasswordSuccess.", e);
        }
    }
}
