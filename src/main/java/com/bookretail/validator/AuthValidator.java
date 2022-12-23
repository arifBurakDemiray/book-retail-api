package com.bookretail.validator;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import com.bookretail.dto.auth.*;
import com.bookretail.enums.EClientId;
import com.bookretail.enums.EGrantType;
import com.bookretail.enums.ERole;
import com.bookretail.enums.EVerificationKeyType;
import com.bookretail.model.User;
import com.bookretail.repository.UserRepository;
import com.bookretail.repository.VerificationCodeRepository;
import com.bookretail.util.ReadingIsGoodRegex;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final EClientId eClientId;
    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final MessageSourceAccessor messageSource;

    private Map<String, String> roleAndClientIdTable = null;

    @PostConstruct
    protected void init() {
        roleAndClientIdTable = Map.of(
                ERole.USER, eClientId.getMobileClientId(),
                ERole.SYSADMIN, eClientId.getWebClientId()
        );
    }

    public ValidationResult validate(@NotNull LoginRequest dto) {
        if (dto.grant_type == EGrantType.password) {
            if (dto.username == null || dto.username.isBlank()) {
                return ValidationResult.failed(
                        messageSource.getMessage("login_request.username.empty")
                );
            }

            if (!dto.getUsername().matches(ReadingIsGoodRegex.EMAIL)) {
                return ValidationResult.failed(
                        messageSource.getMessage("validation.generic.email.unfit_regex")
                );
            }

            if (dto.password == null || dto.password.isBlank()) {
                return ValidationResult.failed(
                        messageSource.getMessage("login_request.password.empty")
                );
            }//TODO change here by your role implementation logic
            if (eClientId.getMobileClientId().equals(dto.client_id) &&
                    (dto.client_secret == null || dto.client_secret.isBlank())) {
                return ValidationResult.failed(
                        messageSource.getMessage("login_request.client_secret.empty")
                );

            }
        } else if (dto.grant_type == EGrantType.refresh_token &&
                (dto.refresh_token == null || dto.refresh_token.isBlank())) {
            return ValidationResult.failed(
                    messageSource.getMessage("login_request.refresh_token.empty")
            );

        }

        return ValidationResult.success();
    }

    public ValidationResult validate(@NotNull ForgotPasswordRequest dto) {
        var maybeUser = userRepository.findByEmail(dto.getEmail());

        if (maybeUser.isEmpty()) {
            return ValidationResult.failed(
                    messageSource.getMessage("forgot_password_request.user.not_found")
            );
        }

        var user = maybeUser.get();

        if (!user.isAccountNonLocked()) {
            return ValidationResult.failed(
                    messageSource.getMessage("forgot_password_request.user.unconfirmed")
            );
        }

        return ValidationResult.success();
    }

    public ValidationResult validate(@NotNull User user) {

        var maybeUser = userRepository.findByEmail(user.getUsername());

        if (maybeUser.isPresent()) {
            return ValidationResult.failed(
                    messageSource.getMessage("register_request.email.exist")
            );
        }

        if (user.getPhoneNumber() != null && userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            return ValidationResult.failed(
                    messageSource.getMessage("register_request.phone.exist")
            );
        }

        return ValidationResult.success();
    }

    public ValidationResult validate(@NotNull ResetPasswordRequest body) {
        var maybeVerificationCode = verificationCodeRepository.findByCode(body.getCode());

        if (maybeVerificationCode.isEmpty()) {
            return ValidationResult.failed(
                    messageSource.getMessage("reset_password_request.code.not_found")
            );
        }

        return ValidationResult.success();
    }

    public ValidationResult validate(@NotNull ResendActivationCodeRequest dto) {
        var maybeUser = userRepository.findByEmail(dto.getEmail());
        if (maybeUser.isEmpty()) {
            return ValidationResult.failed(
                    messageSource.getMessage("resend_activation_request.user.not_found")
            );
        }

        if (maybeUser.get().isAccountNonLocked()) {
            return ValidationResult.failed(
                    messageSource.getMessage("resend_activation_request.user.confirmed")
            );
        }

        return ValidationResult.success();
    }

    public ValidationResult validate(@NotNull AccountActivationRequest body) {
        var maybeVerificationCode = verificationCodeRepository.findByCode(body.getCode());

        if (maybeVerificationCode.isEmpty()) {
            return ValidationResult.failed(
                    messageSource.getMessage("activation.code.not_found")
            );
        }

        var verificationCode = maybeVerificationCode.get();

        if (!verificationCode.getType().equals(EVerificationKeyType.ACTIVATION)) {
            return ValidationResult.failed(
                    messageSource.getMessage("activation.code.wrong_type")
            );
        }

        if (verificationCode.isExpired()) {
            return ValidationResult.failed(
                    messageSource.getMessage("activation.code.expired")
            );
        }

        return ValidationResult.success();
    }

    public ValidationResult validate(@NotNull User user, @NotNull String clientId) {
        var role = ERole.stringValueOf(user.getRole());

        if (!clientId.equals(roleAndClientIdTable.getOrDefault(role, null))) {
            return ValidationResult.failed(
                    messageSource.getMessage("login_request.client_id.not_appropriate")
            );
        }

        return ValidationResult.success();
    }
}
