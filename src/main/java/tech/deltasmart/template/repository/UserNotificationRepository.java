package com.bookretail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.bookretail.model.UserNotification;

public interface UserNotificationRepository extends
        JpaRepository<UserNotification, Long>,
        JpaSpecificationExecutor<UserNotification> {

    UserNotification getByUserIdAndOneSignalNotification_OneSignalId(Long userId, String oneSignalId);

}
