package com.bookretail.factory;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import com.bookretail.dto.notification.UserNotificationDto;
import com.bookretail.model.UserNotification;

@Component
@NoArgsConstructor
public class UserNotificationFactory {

    public UserNotificationDto createUserNotificationDto(UserNotification userNotification) {
        return new UserNotificationDto(
                userNotification.getId(),
                userNotification.getOneSignalNotification().getCreatedOn(),
                userNotification.getOneSignalNotification().getContent(),
                userNotification.getOneSignalNotification().getHeading(),
                userNotification.getOneSignalNotification().getUser().getEmail(),
                userNotification.isRead()
        );
    }
}
