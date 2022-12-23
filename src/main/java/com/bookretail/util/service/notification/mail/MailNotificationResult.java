package com.bookretail.util.service.notification.mail;

import lombok.NoArgsConstructor;
import com.bookretail.util.service.notification.INotificationResult;

@NoArgsConstructor
public class MailNotificationResult implements INotificationResult {

    public static MailNotificationResult success() {
        return new MailNotificationResult();
    }

}
