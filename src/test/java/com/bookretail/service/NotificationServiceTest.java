package com.bookretail.service;

import com.bookretail.config.security.JwtUtil;
import com.bookretail.dto.PageFilter;
import com.bookretail.factory.NotificationFactory;
import com.bookretail.factory.UserTestFactory;
import com.bookretail.model.OneSignalNotification;
import com.bookretail.repository.MobileDeviceRepository;
import com.bookretail.repository.OneSignalNotificationRepository;
import com.bookretail.repository.UserRepository;
import com.bookretail.util.service.notification.NotificationException;
import com.bookretail.util.service.notification.onesignal.MobileNotification;
import com.bookretail.util.service.notification.onesignal.OneSignalService;
import com.bookretail.util.service.notification.onesignal.PersonalMobileNotification;
import com.bookretail.util.service.notification.onesignal.helper.MultiLanguageText;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {NotificationService.class})
@ExtendWith(SpringExtension.class)
class NotificationServiceTest {

    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private MessageSourceAccessor messageSourceAccessor;

    @MockBean
    private MobileDeviceRepository mobileDeviceRepository;

    @MockBean
    private NotificationFactory notificationFactory;

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private OneSignalNotificationRepository oneSignalNotificationRepository;

    @MockBean
    private OneSignalService oneSignalService;

    @MockBean
    private UserRepository userRepository;

    @Nested
    class SendMessageToAll_Method_Test_Cases {
        
        @Test
        void SendMessageToAll_Success() throws NotificationException {
            //given
            var user = UserTestFactory.createSuccessTestUser_USER();
            var oneSignalNotification = new OneSignalNotification();
            oneSignalNotification.setOneSignalId("1");
            var contents = new MultiLanguageText("Message Key");

            //when
            when(jwtUtil.getUserId(any())).thenReturn(123L);
            when(userRepository.getById(any())).thenReturn(user);

            when(notificationFactory.createWebNotification(any(), any(), any()))
                    .thenReturn(oneSignalNotification);
            when(notificationFactory.createPersonalNotification(any(), any()))
                    .thenReturn(new PersonalMobileNotification("1", contents,
                            new MultiLanguageText("Message Key")));
            when(mobileDeviceRepository.findAll()).thenReturn(new ArrayList<>());
            var sendMessageToAllResponse = notificationService.sendMessageToAll
                    ("Content", "Heading", "ABC123");

            //then
            assertTrue(sendMessageToAllResponse.isOk());
            verify(userRepository).getById(any());
            verify(oneSignalService).send((MobileNotification) any());
            verify(notificationFactory).createWebNotification(any(), any(), any());
            verify(notificationFactory).createPersonalNotification(any(), any());
            verify(mobileDeviceRepository).findAll();
            verify(jwtUtil).getUserId(any());
        }
    }


    @Nested
    class GetSentMessages_Method_Test_Cases {

        @Test
        void GetSentMessages_Success() {
            //given

            //when
            when(oneSignalNotificationRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(new ArrayList<>()));

            var getSentMessagesResponse = notificationService
                    .getSentMessages(new PageFilter(3, 20, Sort.Direction.ASC, "Fields"), "ABC123");
            var data = getSentMessagesResponse.getData();

            //then
            assertTrue(data instanceof PageImpl);
            assertTrue(getSentMessagesResponse.isOk());
            verify(oneSignalNotificationRepository).findAll(any(Specification.class), any(Pageable.class));
        }
    }

}
