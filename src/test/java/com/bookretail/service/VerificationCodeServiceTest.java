package com.bookretail.service;

import com.bookretail.enums.EVerificationKeyType;
import com.bookretail.factory.UserTestFactory;
import com.bookretail.factory.VerificationCodeFactory;
import com.bookretail.model.VerificationCode;
import com.bookretail.repository.VerificationCodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {VerificationCodeService.class})
@ExtendWith(SpringExtension.class)
class VerificationCodeServiceTest {
    @MockBean
    private VerificationCodeFactory verificationCodeFactory;

    @MockBean
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Test
    void Test_CreatePasswordResetCode() {
        //given
        VerificationCode verificationCode = new VerificationCode();
        var user = UserTestFactory.createSuccessTestUser_USER();

        //when
        when(verificationCodeRepository.save(any())).thenReturn(verificationCode);
        doNothing().when(verificationCodeRepository).deleteAll(any());
        when(verificationCodeFactory.createPasswordResetCode(any())).thenReturn(new VerificationCode());

        //then
        assertSame(verificationCode, verificationCodeService.createPasswordResetCode(user));
        verify(verificationCodeRepository).save(any());
        verify(verificationCodeRepository).deleteAll(any());
        verify(verificationCodeFactory).createPasswordResetCode(any());
    }

    @Test
    void Test_CreateActivationCode() {
        //given
        VerificationCode verificationCode = new VerificationCode();
        var user = UserTestFactory.createSuccessTestUser_USER();

        //when
        when(verificationCodeRepository.save(any())).thenReturn(verificationCode);
        doNothing().when(verificationCodeRepository).deleteAll(any());
        when(verificationCodeFactory.createActivationCode(any())).thenReturn(new VerificationCode());

        //then
        assertSame(verificationCode, verificationCodeService.createActivationCode(user));
        verify(verificationCodeRepository).save(any());
        verify(verificationCodeRepository).deleteAll(any());
        verify(verificationCodeFactory).createActivationCode(any());
    }

    @Test
    void Test_GetUsersVerificationCodes() {
        //given
        var user = UserTestFactory.createSuccessTestUser_USER();
        //when

        //then
        assertTrue(verificationCodeService.getUsersVerificationCodes(user, EVerificationKeyType.ACTIVATION).isEmpty());
    }
}