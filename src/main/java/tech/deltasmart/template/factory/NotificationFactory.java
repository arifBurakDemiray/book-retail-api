package com.bookretail.factory;

import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import com.bookretail.dto.notification.WebNotificationDto;
import com.bookretail.model.*;
import com.bookretail.util.service.notification.onesignal.MobileNotification;
import com.bookretail.util.service.notification.onesignal.PersonalMobileNotification;
import com.bookretail.util.service.notification.onesignal.helper.MultiLanguageText;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@NoArgsConstructor
public class NotificationFactory {

    public MobileNotification createPersonalNotification(Notification notification, Set<User> recipents) {

        List<String> playerIds = new ArrayList<>();

        recipents.forEach(user -> {
            Hibernate.initialize(user.getMobileDevices());

            user.getMobileDevices()
                    .forEach(device -> playerIds.add(device.getDeviceId()));
        });
        
        if (playerIds.isEmpty()) {
            return null;
        }

        return new PersonalMobileNotification(playerIds, new MultiLanguageText(notification.getContent()),
                new MultiLanguageText(notification.getHeading()));
    }

    public WebNotificationDto createWebNotificationDto(OneSignalNotification oneSignalNotification) {
        return new WebNotificationDto(oneSignalNotification.getCreatedOn(), oneSignalNotification.getContent(),
                oneSignalNotification.getHeading(), oneSignalNotification.getUser().getEmail()
        );
    }

    public OneSignalNotification createWebNotification(String content, String heading, User user
                                                       ) {
        return new OneSignalNotification(user, heading, content);
    }
}
