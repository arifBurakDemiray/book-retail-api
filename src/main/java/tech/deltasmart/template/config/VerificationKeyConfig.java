package com.bookretail.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class VerificationKeyConfig {
    @Value("#{T(java.lang.Integer).parseInt('${application.verification_key.expiration.activation}')}")
    private int activationExpiration;

    @Value("#{T(java.lang.Integer).parseInt('${application.verification_key.expiration.password_reset}')}")
    private int passwordReset;


    public int getActivationKeyExpirationTime() {
        return activationExpiration;
    }

    public int getPasswordResetKeyExpirationTime() {
        return passwordReset;
    }
}
