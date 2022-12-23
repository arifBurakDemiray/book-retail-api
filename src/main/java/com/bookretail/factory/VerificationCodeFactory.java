package com.bookretail.factory;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import com.bookretail.config.VerificationKeyConfig;
import com.bookretail.enums.EVerificationKeyType;
import com.bookretail.model.User;
import com.bookretail.model.VerificationCode;
import com.bookretail.util.RandomUtil;

import java.time.Instant;
import java.util.Date;

@Component
@AllArgsConstructor
public class VerificationCodeFactory {
    private final VerificationKeyConfig verificationKeyConfig;

    public VerificationCode createActivationCode(@NotNull User user) {
        return new VerificationCode(
                RandomUtil.generate(),
                user,
                getExpirationDate(verificationKeyConfig.getActivationKeyExpirationTime()),
                EVerificationKeyType.ACTIVATION
        );
    }

    public VerificationCode createPasswordResetCode(@NotNull User user) {
        return new VerificationCode(
                RandomUtil.generate(),
                user,
                getExpirationDate(verificationKeyConfig.getPasswordResetKeyExpirationTime()),
                EVerificationKeyType.PASSWORD_RESET
        );
    }

    private Date getExpirationDate(int nextSeconds) {
        return Date.from(Instant.now().plusSeconds(nextSeconds));
    }
}
