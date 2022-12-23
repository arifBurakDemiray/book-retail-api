package com.bookretail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.bookretail.enums.ELocale;
import com.bookretail.enums.ESegment;
import com.bookretail.util.service.notification.NotificationException;
import com.bookretail.util.service.notification.onesignal.OneSignalService;
import com.bookretail.util.service.notification.onesignal.PersonalMobileNotification;
import com.bookretail.util.service.notification.onesignal.SegmentedMobileNotification;
import com.bookretail.util.service.notification.onesignal.helper.MultiLanguageText;

import java.util.Map;
import java.util.Set;

@SpringBootTest
public class OneSignalTests {
    @Autowired
    private OneSignalService oneSignalService;

    @Test
    public void PersonalMobileNotification() throws NotificationException {
        var heading = new MultiLanguageText(Map.of(ELocale.EN, "heading en", ELocale.TR, "heading tr"));
        var content = new MultiLanguageText(Map.of(ELocale.EN, "content en", ELocale.TR, "content tr"));

        var notification = new PersonalMobileNotification("5cea07ab-adbb-4057-9e42-d35cf217d916", content, heading);

        oneSignalService.send(notification);
    }

    @Test
    public void SegmentedMobileNotification() throws NotificationException {
        var heading = new MultiLanguageText(Map.of(ELocale.EN, "heading en", ELocale.TR, "heading tr"));
        var content = new MultiLanguageText(Map.of(ELocale.EN, "content en", ELocale.TR, "content tr"));

        var notification = new SegmentedMobileNotification(Set.of(ESegment.ACTIVE_USERS), content, heading);

        oneSignalService.send(notification);
    }
}
