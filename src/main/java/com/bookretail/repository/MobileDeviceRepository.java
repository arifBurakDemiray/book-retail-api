package com.bookretail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bookretail.model.MobileDevice;

import java.util.Optional;

@Repository
public interface MobileDeviceRepository extends JpaRepository<MobileDevice, Long> {


    Optional<MobileDevice> deleteByDeviceIdAndUserId(String deviceId, Long userId);

    Optional<MobileDevice> findByDeviceId(String deviceId);

    Optional<MobileDevice> findByRefreshToken(String refreshToken);
}
