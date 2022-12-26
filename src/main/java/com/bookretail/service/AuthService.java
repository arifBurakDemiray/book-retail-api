package com.bookretail.service;

import com.bookretail.config.security.JwtUtil;
import com.bookretail.dto.Response;
import com.bookretail.dto.auth.*;
import com.bookretail.enums.EErrorCode;
import com.bookretail.enums.ERole;
import com.bookretail.event.RegistrationCompleteEvent;
import com.bookretail.factory.UserFactory;
import com.bookretail.model.MobileDevice;
import com.bookretail.model.User;
import com.bookretail.repository.MobileDeviceRepository;
import com.bookretail.repository.UserRepository;
import com.bookretail.repository.VerificationCodeRepository;
import com.bookretail.util.LoggableFuture;
import com.bookretail.util.RandomUtil;
import com.bookretail.validator.AuthValidator;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthValidator authValidator;
    private final VerificationCodeRepository verificationCodeRepository;
    private final UserFactory userFactory;
    private final BCryptPasswordEncoder encoder;
    private final MessageSourceAccessor messageSource;
    private final MobileDeviceRepository deviceRepository;
    private final AuthAsyncService authAsyncService;

    public LoginResponse login(@NotNull LoginRequest body) {
        var result = authValidator.validate(body);

        if (result.isNotValid()) {
            return LoginResponse.notOk(result.getMessage(), EErrorCode.UNAUTHORIZED);
        }

        switch (body.grant_type) {
            case password:
                return loginWithPassword(body);
            case refresh_token:
                return loginWithRefreshToken(body);
            default:
                return LoginResponse.notOk(messageSource
                        .getMessage("login_request.grant_type.unknown"), EErrorCode.BAD_REQUEST);
        }
    }

    @Transactional
    protected LoginResponse loginWithPassword(LoginRequest body) {
        try {
            var authentication = new UsernamePasswordAuthenticationToken(body.username, body.password);
            var authenticate = authenticationManager.authenticate(authentication);
            var user = (User) authenticate.getPrincipal();

            var refreshToken = generateRefreshToken(user);

            if (ERole.isMobileUser(ERole.stringValueOf(user.getRole()))) {
                var new_secret = "";
                if (body.client_secret == null) {
                    new_secret = "invalid-secret";
                } else {
                    new_secret = body.client_secret;
                }
                deviceRepository.findByDeviceId(new_secret).ifPresent(deviceRepository::delete);
                var device = new MobileDevice(user, new_secret, refreshToken);
                deviceRepository.save(device);
            }

            var accessToken = jwtUtil.generateAccessToken(user, body.client_id);

            return LoginResponse.ok(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            return LoginResponse.notOk(e.getMessage(), EErrorCode.UNAUTHORIZED);
        }
    }

    @Transactional
    protected LoginResponse loginWithRefreshToken(LoginRequest body) {
        var maybeDevice = deviceRepository.findByRefreshToken(body.refresh_token);

        if (maybeDevice.isEmpty()) {
            return LoginResponse.notOk(messageSource
                    .getMessage("login_request.refresh_token.device_not_found"), EErrorCode.ACCESS_DENIED);
        }

        var device = maybeDevice.get();

        Hibernate.initialize(device.getUser());
        var user = device.getUser();

        device.setRefreshToken(generateRefreshToken(user));
        deviceRepository.save(device);

        var accessToken = jwtUtil.generateAccessToken(user);
        var refreshToken = device.getRefreshToken();

        return LoginResponse.ok(accessToken, refreshToken);
    }

    private String generateRefreshToken(@NotNull User user) {
        var role = ERole.stringValueOf(user.getRole());

        if (ERole.isMobileUser(role)) {
            return RandomUtil.generate();
        }

        return RandomUtil.generateInvalid();
    }

    @Transactional
    public Response<RegisterDto> register(@NotNull RegisterRequest body) {
        var user = userFactory.createUser(body);

        var validationResult = authValidator.validate(user);

        if (validationResult.isNotValid()) {
            return Response.notOk(validationResult.getMessage(), EErrorCode.BAD_REQUEST);
        }

        user = userRepository.save(user);

        var locale = LocaleContextHolder.getLocale();

        eventPublisher.publishEvent(new RegistrationCompleteEvent(this, user, locale));

        return Response.ok(userFactory.createRegisterDto(user));
    }

    @Transactional
    public Response<ResendActivationCodeDto> resendActivationCode(ResendActivationCodeRequest body) {
        var locale = LocaleContextHolder.getLocale();

        var validationResult = authValidator.validate(body);

        if (validationResult.isNotValid()) {
            return Response.notOk(validationResult.getMessage(), EErrorCode.BAD_REQUEST);
        }

        userRepository.findByEmail(body.getEmail()).ifPresent(user -> {
            Hibernate.initialize(user.getVerificationCodes());

            eventPublisher.publishEvent(
                    new RegistrationCompleteEvent(this, user, locale)
            );
        });

        return Response.ok(
                new ResendActivationCodeDto(
                        messageSource.getMessage("resend_activation.success")
                )
        );
    }

    public Response<ForgotPasswordDto> forgotPassword(ForgotPasswordRequest body) {
        var locale = LocaleContextHolder.getLocale();

        var validationResult = authValidator.validate(body);

        if (validationResult.isValid()) {
            LoggableFuture.runAsync(() -> authAsyncService.forgotPasswordSuccess(body, locale));
        }

        return Response.ok(
                new ForgotPasswordDto(
                        messageSource.getMessage("forgot_password.success")
                )
        );
    }

    @Transactional
    public Response<ResetPasswordDto> resetPassword(ResetPasswordRequest body) {
        var validationResult = authValidator.validate(body);

        if (validationResult.isNotValid()) {
            return Response.notOk(validationResult.getMessage(), EErrorCode.BAD_REQUEST);
        }

        verificationCodeRepository.findByCode(body.getCode()).ifPresent(verificationCode -> {
            var user = verificationCode.getUser();

            user.setPassword(encoder.encode(body.getPassword()));
            verificationCodeRepository.delete(verificationCode);

            userRepository.save(user);
        });

        return Response.ok(new ResetPasswordDto(
                        messageSource.getMessage("reset_password.success")
                )
        );
    }

    @Transactional
    public Response<AccountActivationDto> activateAccount(AccountActivationRequest body) {
        var validationResult = authValidator.validate(body);

        if (validationResult.isNotValid()) {
            return Response.notOk(validationResult.getMessage(), EErrorCode.BAD_REQUEST);
        }

        var maybeVerificationCode = verificationCodeRepository.findByCode(body.getCode());

        maybeVerificationCode.ifPresent(verificationCode -> {
            var user = verificationCode.getUser();

            user.confirm();

            userRepository.save(user);
            verificationCodeRepository.delete(verificationCode);
        });

        return Response.ok(new AccountActivationDto(
                        messageSource.getMessage("activation.success")
                )
        );
    }

    @Transactional
    public Response<LogoutResponse> logout(String token, String deviceId) {
        var userId = jwtUtil.getUserId(token);

        var maybeDevice = deviceRepository.deleteByDeviceIdAndUserId(deviceId, userId);

        return maybeDevice.isEmpty() ?
                Response.notOk(messageSource.getMessage("logout.device_not_found"), EErrorCode.BAD_REQUEST) :
                Response.ok(new LogoutResponse(messageSource.getMessage("logout.success")));
    }
}
