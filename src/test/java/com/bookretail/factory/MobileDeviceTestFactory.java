package com.bookretail.factory;

import com.bookretail.model.MobileDevice;
import com.bookretail.model.User;

public class MobileDeviceTestFactory {
    private MobileDeviceTestFactory() {
    }

    public static MobileDevice createMobileDevice(String token) {
        MobileDevice mobileDevice = new MobileDevice();
        mobileDevice.setRefreshToken(token);
        return mobileDevice;
    }

    public static MobileDevice createMobileDeviceInvalidRefresh() {
        return new MobileDevice(new User(), "1", "ABC123");
    }
}
