package com.bookretail.event.handler;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import com.bookretail.enums.ERole;
import com.bookretail.event.ForgotPasswordEvent;
import com.bookretail.event.RegistrationCompleteEvent;
import com.bookretail.service.VerificationCodeService;
import com.bookretail.util.service.notification.INotificationService;
import com.bookretail.util.service.notification.mail.LocalizedHtmlMail;
import com.bookretail.util.service.notification.mail.Mail;

import java.util.Map;

@Component
@AllArgsConstructor
public class VerificationCodeEventsListener {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final INotificationService<Mail> mailService;
    private final VerificationCodeService verificationCodeService;
    private final MessageSourceAccessor messageSource;

    @EventListener
    public void onForgotPassword(@NotNull ForgotPasswordEvent event) {
        var user = event.getUser();
        var verificationCode = verificationCodeService.createPasswordResetCode(user);

        try {
            Mail mail = new LocalizedHtmlMail(
                    user.getEmail(),
                    "template.email.password-reset.subject",
                    "emails/password-reset.html",
                    Map.of(
                            "code", verificationCode.getCode(),
                            "lang", event.getLocale().getLanguage()
                    )
            );

            mailService.send(mail);
        } catch (Exception exception) {
            logger.error(String.format("Could not sent password reset email for user: %s.", user), exception);
        }
    }

    @EventListener
    public void onRegistrationComplete(@NotNull RegistrationCompleteEvent event) {
        var user = event.getUser();
        var verificationCode = verificationCodeService.createActivationCode(user);

        try {
            Mail mail = new LocalizedHtmlMail(
                    user.getEmail(),
                    "template.email.registration-confirmation.subject",
                    "emails/registration-confirmation.html",
                    Map.of(
                            "code", verificationCode.getCode(),
                            "name", user.getName() == null ?
                                    messageSource.getMessage("template.email.registration-confirmation.fellow") :
                                    user.getName(),
                            "lang", event.getLocale().getLanguage(),
                            "type", ERole.isWebUser(ERole.stringValueOf(user.getRole())) ? "web" : "mobile"
                    )
            );

            mailService.send(mail);
        } catch (Exception exception) {
            logger.error(String.format("Could not sent password reset email for user: %s.", user), exception);
        }
    }
}
