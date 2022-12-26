package com.bookretail.service;


import com.bookretail.config.security.JwtUtil;
import com.bookretail.dto.Response;
import com.bookretail.dto.auth.*;
import com.bookretail.enums.EErrorCode;
import com.bookretail.enums.EGrantType;
import com.bookretail.enums.ERole;
import com.bookretail.enums.EVerificationKeyType;
import com.bookretail.factory.MobileDeviceTestFactory;
import com.bookretail.factory.UserFactory;
import com.bookretail.factory.UserTestFactory;
import com.bookretail.model.MobileDevice;
import com.bookretail.model.User;
import com.bookretail.model.VerificationCode;
import com.bookretail.repository.MobileDeviceRepository;
import com.bookretail.repository.UserRepository;
import com.bookretail.repository.VerificationCodeRepository;
import com.bookretail.validator.AuthValidator;
import com.bookretail.validator.ValidationResult;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {AuthService.class, BCryptPasswordEncoder.class})
@ExtendWith(SpringExtension.class)
class AuthServiceTest {


    @MockBean
    private AuthAsyncService authAsyncService;

    @Autowired
    private AuthService authService;

    @MockBean
    private AuthValidator authValidator;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private MessageSourceAccessor messageSourceAccessor;

    @MockBean
    private MobileDeviceRepository mobileDeviceRepository;

    @MockBean
    private UserFactory userFactory;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private VerificationCodeRepository verificationCodeRepository;

    @Nested
    class Login_Method_Test_Cases {

        @Test
        void Login_Fails_AccountExpired_Grant_Password() throws AuthenticationException {
            //given
            var request = UserTestFactory.createLoginRequest(EGrantType.password);
            //when
            when(authenticationManager.authenticate(any())).thenThrow(new AccountExpiredException("Msg"));
            when(authValidator.validate((LoginRequest) any())).thenReturn(ValidationResult.success());
            var loginResponse = authService.login(request);

            //then
            assertFalse(loginResponse.isSuccess());
            assertNull(loginResponse.refreshToken);
            assertEquals("Msg", loginResponse.message);
            assertEquals(EErrorCode.UNAUTHORIZED, loginResponse.code);
            assertNull(loginResponse.accessToken);

            verify(authenticationManager).authenticate(any());
            verify(authValidator).validate((LoginRequest) any());
        }

        @Test
        void Login_Fails_AccountExpired_Grant_RefreshToken() throws AuthenticationException {
            //given
            var request = UserTestFactory.createLoginRequest(EGrantType.refresh_token);
            //when
            when(authenticationManager.authenticate(any())).thenThrow(new AccountExpiredException("Msg"));
            when(authValidator.validate((LoginRequest) any())).thenReturn(ValidationResult.success());
            var loginResponse = authService.login(request);

            //then
            assertFalse(loginResponse.isSuccess());
            assertNull(loginResponse.message);
            assertEquals(EErrorCode.ACCESS_DENIED, loginResponse.code);
            assertNull(loginResponse.accessToken);

            verify(authValidator).validate((LoginRequest) any());
        }


        @Test
        void Login_Fails_ValidationFails() throws AuthenticationException {
            //given
            var validationResult = mock(ValidationResult.class);
            var request = UserTestFactory.createLoginRequest(EGrantType.password);
            //when
            when(validationResult.getMessage()).thenReturn("Validation failed");
            when(validationResult.isNotValid()).thenReturn(true);
            when(authValidator.validate((LoginRequest) any())).thenReturn(validationResult);
            var loginResponse = authService.login(request);

            //then
            assertFalse(loginResponse.isSuccess());
            assertNull(loginResponse.refreshToken);
            assertEquals("Validation failed", loginResponse.message);
            assertEquals(EErrorCode.UNAUTHORIZED, loginResponse.code);
            assertNull(loginResponse.accessToken);

            verify(authValidator).validate((LoginRequest) any());
            verify(validationResult).isNotValid();
            verify(validationResult).getMessage();
        }

    }

    @Nested
    class LoginWithPassword_Method_Test_Cases {

        @Test
        void LoginWithPassword_Success() throws AuthenticationException {
            //given
            var mobileDevice = MobileDeviceTestFactory.createMobileDevice("ABC123");
            var mobileDevice1 = MobileDeviceTestFactory.createMobileDevice("ABC123");
            var user = UserTestFactory.createSuccessTestUser_USER();
            var token = UserTestFactory.token_USER;
            var request = UserTestFactory.createLoginRequest(EGrantType.password);
            Optional<MobileDevice> result = Optional.of(mobileDevice1);

            //when
            doNothing().when(mobileDeviceRepository).delete(any());
            when(mobileDeviceRepository.save(any())).thenReturn(mobileDevice);
            when(mobileDeviceRepository.findByDeviceId(any())).thenReturn(result);
            when(jwtUtil.generateAccessToken(any(), any())).thenReturn(token);
            when(authenticationManager.authenticate(any())).thenReturn(new TestingAuthenticationToken(user, "Credentials"));

            var loginWithPasswordResponse = authService.loginWithPassword(request);

            //then
            assertTrue(loginWithPasswordResponse.isSuccess());
            assertNull(loginWithPasswordResponse.message);
            assertNull(loginWithPasswordResponse.code);
            assertEquals(token, loginWithPasswordResponse.accessToken);

            verify(mobileDeviceRepository).save(any());
            verify(mobileDeviceRepository).findByDeviceId(any());
            verify(mobileDeviceRepository).delete(any());
            verify(jwtUtil).generateAccessToken(any(), any());
            verify(authenticationManager).authenticate(any());
        }

        @Test
        void LoginWithPassword_Success_SecretNull() throws AuthenticationException {
            //given
            var mobileDevice = MobileDeviceTestFactory.createMobileDevice("ABC123");
            var mobileDevice1 = MobileDeviceTestFactory.createMobileDevice("ABC123");
            var request = UserTestFactory.createLoginRequestSecretNull(EGrantType.password);
            var token = UserTestFactory.token_USER;
            Optional<MobileDevice> result = Optional.of(mobileDevice1);
            User user = mock(User.class);
            TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, "Credentials");

            //when
            doNothing().when(mobileDeviceRepository).delete(any());
            when(mobileDeviceRepository.save(any())).thenReturn(mobileDevice);
            when(mobileDeviceRepository.findByDeviceId(any())).thenReturn(result);
            when(jwtUtil.generateAccessToken(any(), any())).thenReturn(token);
            when(user.getRole()).thenReturn(ERole.valueOf(ERole.USER));
            when(authenticationManager.authenticate(any())).thenReturn(testingAuthenticationToken);
            LoginResponse loginWithPasswordResponse = authService.loginWithPassword(request);

            //then
            assertTrue(loginWithPasswordResponse.isSuccess());
            assertNull(loginWithPasswordResponse.message);
            assertNull(loginWithPasswordResponse.code);
            assertEquals(token, loginWithPasswordResponse.accessToken);

            verify(mobileDeviceRepository).save(any());
            verify(mobileDeviceRepository).findByDeviceId(any());
            verify(mobileDeviceRepository).delete(any());
            verify(jwtUtil).generateAccessToken(any(), any());
            verify(authenticationManager).authenticate(any());
            verify(user, times(2)).getRole();
        }

    }

    @Nested
    class LoginWithRefreshToken_Method_Test_Cases {

        @Test
        void LoginWithRefreshToken_Success_InvalidRefreshBecauseWebUser() {
            //given
            var mobileDevice = MobileDeviceTestFactory.createMobileDeviceInvalidRefresh();
            var mobileDevice1 = MobileDeviceTestFactory.createMobileDevice("ABC123");
            var request = UserTestFactory.createLoginRequestSecretNull(EGrantType.refresh_token);
            var token = UserTestFactory.token_USER;
            Optional<MobileDevice> result = Optional.of(mobileDevice);

            //when
            when(mobileDeviceRepository.save(any())).thenReturn(mobileDevice1);
            when(mobileDeviceRepository.findByRefreshToken(any())).thenReturn(result);
            when(jwtUtil.generateAccessToken(any())).thenReturn(token);
            var loginWithRefreshTokenResponse = authService.loginWithRefreshToken(request);

            //then
            assertTrue(loginWithRefreshTokenResponse.isSuccess());
            assertEquals("AAAAAAAAAAAAAAAAAAAAAAAAAAA", loginWithRefreshTokenResponse.refreshToken);
            assertNull(loginWithRefreshTokenResponse.message);
            assertNull(loginWithRefreshTokenResponse.code);
            assertEquals(token, loginWithRefreshTokenResponse.accessToken);

            verify(mobileDeviceRepository).save(any());
            verify(mobileDeviceRepository).findByRefreshToken(any());
            verify(jwtUtil).generateAccessToken(any());
        }

        @Test
        void LoginWithRefreshToken_Fails_DeviceEmpty() {
            //given
            var mobileDevice = MobileDeviceTestFactory.createMobileDevice("ABC123");
            var request = UserTestFactory.createLoginRequestSecretNull(EGrantType.refresh_token);

            //when
            when(mobileDeviceRepository.save(any())).thenReturn(mobileDevice);
            when(jwtUtil.generateAccessToken(any())).thenReturn("ABC123");
            LoginResponse loginWithRefreshTokenResponse = authService.loginWithRefreshToken(request);
            when(mobileDeviceRepository.findByRefreshToken(any())).thenReturn(null);

            //then
            assertFalse(loginWithRefreshTokenResponse.isSuccess());
            assertNull(loginWithRefreshTokenResponse.refreshToken);
            assertNull(loginWithRefreshTokenResponse.message);
            assertEquals(EErrorCode.ACCESS_DENIED, loginWithRefreshTokenResponse.code);
            assertNull(loginWithRefreshTokenResponse.accessToken);

            verify(mobileDeviceRepository).findByRefreshToken(any());
        }
    }

    @Nested
    class Register_Method_Test_Cases {

        @Test
        void Register_Fails_ValidationFails() {
            //given
            var user = UserTestFactory.createSuccessTestUser_USER();
            var user1 = UserTestFactory.createSuccessTestUser_USER();

            //when
            when(userRepository.save(any())).thenReturn(user);
            when(userFactory.createRegisterDto(any())).thenReturn(new RegisterDto());
            when(userFactory.createUser(any())).thenReturn(user1);
            when(authValidator.validate((User) any())).thenReturn(ValidationResult.failed("Validation failed"));
            Response<RegisterDto> registerResponse = authService
                    .register(new RegisterRequest("Name", "Surname", "example@hotmail.com", "password", "05555555555"));

            //then
            assertFalse(registerResponse.isOk());

            verify(userFactory).createUser(any());
            verify(authValidator).validate((User) any());
        }

        @Test
        void Register_Success() {
            //given
            var user = UserTestFactory.createSuccessTestUser_USER();
            var user1 = UserTestFactory.createSuccessTestUser_USER();


            //when
            when(userRepository.save(any())).thenReturn(user);
            when(userFactory.createRegisterDto(any())).thenReturn(new RegisterDto());
            when(userFactory.createUser(any())).thenReturn(user1);
            when(authValidator.validate((User) any())).thenReturn(ValidationResult.success());
            var registerResponse = authService.register(UserTestFactory.createRegisterRequest());

            //then
            assertTrue(registerResponse.isOk());
            assertNull(registerResponse.getData().name);

            verify(userRepository).save(any());
            verify(userFactory).createRegisterDto(any());
            verify(userFactory).createUser(any());
            verify(authValidator).validate((User) any());
        }
    }


    @Nested
    class ResendActivationCode_Method_Test_Cases {

        @Test
        void ResendActivationCode_Success() {
            //given
            var user = UserTestFactory.createSuccessTestUser_USER();

            Optional<User> result = Optional.of(user);

            //when
            when(userRepository.findByEmail((String) any())).thenReturn(result);
            when(messageSourceAccessor.getMessage((String) any())).thenReturn("Message");
            when(authValidator.validate((ResendActivationCodeRequest) any())).thenReturn(ValidationResult.success());
            Response<ResendActivationCodeDto> resendActivationCodeResponse = authService
                    .resendActivationCode(new ResendActivationCodeRequest("1", "example@hotmail.com"));

            //then
            assertTrue(resendActivationCodeResponse.isOk());
            assertEquals("Message", resendActivationCodeResponse.getData().getMessage());

            verify(userRepository).findByEmail(any());
            verify(messageSourceAccessor).getMessage((String) any());
            verify(authValidator).validate((ResendActivationCodeRequest) any());
        }

        @Test
        void ResendActivationCode_Fails_ValidationFails() {
            //given
            var validationResult = mock(ValidationResult.class);

            //when
            when(userRepository.findByEmail(any()))
                    .thenThrow(new AccountExpiredException("resend_activation.success"));
            when(messageSourceAccessor.getMessage((String) any())).thenReturn("Message");
            when(validationResult.getMessage()).thenReturn("Message");
            when(validationResult.isNotValid()).thenReturn(true);
            when(authValidator.validate((ResendActivationCodeRequest) any())).thenReturn(validationResult);
            Response<ResendActivationCodeDto> resendActivationCodeResponse = authService
                    .resendActivationCode(new ResendActivationCodeRequest("1", "example@hotmail.com"));

            //then
            assertNull(resendActivationCodeResponse.getData());
            assertFalse(resendActivationCodeResponse.isOk());

            verify(authValidator).validate((ResendActivationCodeRequest) any());
            verify(validationResult).isNotValid();
            verify(validationResult).getMessage();
        }
    }

    @Nested
    class ForgotPassword_Method_Test_Cases {

        @Test
        void ForgotPassword_Success_WithoutException() {
            //given

            //when
            when(messageSourceAccessor.getMessage((String) any())).thenReturn("Message");
            when(authValidator.validate((ForgotPasswordRequest) any())).thenReturn(ValidationResult.success());
            Response<ForgotPasswordDto> forgotPasswordResult = authService
                    .forgotPassword(new ForgotPasswordRequest("1", "example@hotmail.com"));

            //then
            assertTrue(forgotPasswordResult.isOk());
            assertEquals("Message", forgotPasswordResult.getData().getMessage());

            verify(messageSourceAccessor).getMessage((String) any());
            verify(authValidator).validate((ForgotPasswordRequest) any());
        }


        @Test
        void ForgotPassword_Fails_WithException() {
            //given

            //when
            when(messageSourceAccessor.getMessage((String) any()))
                    .thenThrow(new AccountExpiredException("forgot_password.success"));
            when(authValidator.validate((ForgotPasswordRequest) any())).thenReturn(ValidationResult.success());
            doNothing().when(authAsyncService)
                    .forgotPasswordSuccess((ForgotPasswordRequest) any(), (Locale) any());
            assertThrows(AccountExpiredException.class,
                    () -> authService.forgotPassword(new ForgotPasswordRequest("1", "example@hotmail.com")));

            //then
            verify(messageSourceAccessor).getMessage((String) any());
            verify(authValidator).validate((ForgotPasswordRequest) any());
        }


        @Test
        void ForgotPassword_Fails_ValidationFails() {
            //given

            //when
            when(messageSourceAccessor.getMessage((String) any()))
                    .thenThrow(new AccountExpiredException("forgot_password.success"));
            when(authValidator.validate((ForgotPasswordRequest) any()))
                    .thenReturn(ValidationResult.failed("Failed"));
            doNothing().when(authAsyncService)
                    .forgotPasswordSuccess((ForgotPasswordRequest) any(), (java.util.Locale) any());
            assertThrows(AccountExpiredException.class,
                    () -> authService.forgotPassword(new ForgotPasswordRequest("1", "example@hotmail.com")));

            //then
            verify(messageSourceAccessor).getMessage((String) any());
            verify(authValidator).validate((ForgotPasswordRequest) any());
        }
    }

    @Nested
    class ResetPassword_Method_Test_Cases {

        @Test
        void ResetPassword_Success() {
            //given
            LocalDateTime atStartOfDayResult = LocalDate.ofInstant((new Date()).toInstant(), ZoneId.systemDefault()).atStartOfDay();
            var user = UserTestFactory.createSuccessTestUser_USER();
            var user1 = UserTestFactory.createSuccessTestUser_USER();

            //when
            when(verificationCodeRepository.findByCode(any())).thenReturn(Optional.of(new VerificationCode("Code",
                    user, Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()), EVerificationKeyType.PASSWORD_RESET)));
            when(userRepository.save(any())).thenReturn(user1);
            when(messageSourceAccessor.getMessage((String) any())).thenReturn("Message");
            when(authValidator.validate((ResetPasswordRequest) any())).thenReturn(ValidationResult.success());
            Response<ResetPasswordDto> resetPasswordResponse = authService
                    .resetPassword(new ResetPasswordRequest("password", "Code"));

            //then
            assertTrue(resetPasswordResponse.isOk());
            assertEquals("Message", resetPasswordResponse.getData().getMessage());
            verify(verificationCodeRepository).findByCode(any());
            verify(verificationCodeRepository).delete(any());
            verify(userRepository).save(any());
            verify(messageSourceAccessor).getMessage((String) any());
            verify(authValidator).validate((ResetPasswordRequest) any());
        }

        @Test
        void ResetPassword_Fails_ValidationFails() {
            //given

            //when
            when(authValidator.validate((ResetPasswordRequest) any())).thenReturn(ValidationResult.failed("Validation failed"));
            Response<ResetPasswordDto> resetPasswordResponse = authService
                    .resetPassword(new ResetPasswordRequest("password", "Code"));

            //then
            assertFalse(resetPasswordResponse.isOk());
            verify(authValidator).validate((ResetPasswordRequest) any());
        }
    }


    @Nested
    class ActivateAccount_Method_Test_Cases {

        @Test
        void ActivateAccount_Success() {
            //given
            var user = UserTestFactory.createSuccessTestUser_USER();
            var verificationCode = mock(VerificationCode.class);
            Optional<VerificationCode> ofResult = Optional.of(verificationCode);

            //when
            when(verificationCode.getUser()).thenReturn(user);
            when(verificationCodeRepository.findByCode(any())).thenReturn(ofResult);
            when(messageSourceAccessor.getMessage((String) any())).thenReturn("Message");
            when(authValidator.validate((AccountActivationRequest) any())).thenReturn(ValidationResult.success());
            Response<AccountActivationDto> activateAccountResponse = authService
                    .activateAccount(new AccountActivationRequest("Code"));

            //then
            assertTrue(activateAccountResponse.isOk());
            verify(verificationCodeRepository).findByCode(any());
            verify(verificationCode).getUser();
            verify(userRepository).save(any());
            verify(authValidator).validate((AccountActivationRequest) any());
        }


        @Test
        void ActivateAccount_Fails_ValidationFails() {
            //given
            var user = UserTestFactory.createSuccessTestUser_USER();
            var verificationCode = mock(VerificationCode.class);
            Optional<VerificationCode> ofResult = Optional.of(verificationCode);

            //when
            when(verificationCode.getUser()).thenReturn(user);
            when(verificationCodeRepository.findByCode((String) any())).thenReturn(ofResult);
            when(messageSourceAccessor.getMessage((String) any())).thenReturn("Message");
            when(authValidator.validate((AccountActivationRequest) any())).thenReturn(ValidationResult.failed("Validation failed"));
            Response<AccountActivationDto> activateAccountResponse = authService
                    .activateAccount(new AccountActivationRequest("Code"));

            //then
            assertFalse(activateAccountResponse.isOk());
            verify(authValidator).validate((AccountActivationRequest) any());
        }

    }


    @Nested
    class Logout_Method_Test_Cases {

        @Test
        void Logout_Success() {
            //given
            var mobileDevice = MobileDeviceTestFactory.createMobileDeviceInvalidRefresh();
            var user = UserTestFactory.createSuccessTestUser_USER();
            var token = UserTestFactory.token_USER;
            Optional<MobileDevice> result = Optional.of(mobileDevice);

            //when
            when(mobileDeviceRepository.deleteByDeviceIdAndUserId(any(), any())).thenReturn(result);
            when(messageSourceAccessor.getMessage((String) any())).thenReturn("logout.success");
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            Response<LogoutResponse> logoutResponse = authService.logout(token, mobileDevice.getDeviceId());

            //then
            assertTrue(logoutResponse.isOk());
            assertEquals("logout.success", logoutResponse.getData().getMessage());
            verify(mobileDeviceRepository).deleteByDeviceIdAndUserId(any(), any());
            verify(messageSourceAccessor).getMessage((String) any());
            verify(jwtUtil).getUserId(any());
        }

        @Test
        void Logout_Fails() {
            //given
            var user = UserTestFactory.createSuccessTestUser_USER();
            var token = UserTestFactory.token_USER;
            var mobileDevice = MobileDeviceTestFactory.createMobileDeviceInvalidRefresh();
            //when
            when(mobileDeviceRepository.deleteByDeviceIdAndUserId(any(), any()))
                    .thenReturn(Optional.empty());
            when(messageSourceAccessor.getMessage((String) any())).thenReturn("Message");
            when(jwtUtil.getUserId(any())).thenReturn(user.getId());
            Response<LogoutResponse> logoutResponse = authService.logout(token, mobileDevice.getDeviceId());

            //then
            assertNull(logoutResponse.getData());
            assertFalse(logoutResponse.isOk());
            
            verify(mobileDeviceRepository).deleteByDeviceIdAndUserId(any(), any());
            verify(messageSourceAccessor).getMessage((String) any());
            verify(jwtUtil).getUserId(any());
        }

    }

}


