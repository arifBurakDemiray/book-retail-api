package com.bookretail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.bookretail.model.OneSignalNotification;

public interface OneSignalNotificationRepository extends JpaRepository<OneSignalNotification, Long>,
        JpaSpecificationExecutor<OneSignalNotification> {


}
