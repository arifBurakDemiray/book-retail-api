package com.bookretail.service;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bookretail.enums.EVerificationKeyType;
import com.bookretail.factory.VerificationCodeFactory;
import com.bookretail.model.User;
import com.bookretail.model.VerificationCode;
import com.bookretail.repository.VerificationCodeRepository;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class VerificationCodeService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final VerificationCodeFactory verificationCodeFactory;

    @Transactional
    public VerificationCode createPasswordResetCode(User user) {
        verificationCodeRepository.deleteAll(
                getUsersVerificationCodes(user, EVerificationKeyType.PASSWORD_RESET)
        );
        return verificationCodeRepository.save(verificationCodeFactory.createPasswordResetCode(user));
    }

    @Transactional
    public VerificationCode createActivationCode(User user) {
        verificationCodeRepository.deleteAll(
                getUsersVerificationCodes(user, EVerificationKeyType.ACTIVATION)
        );

        return verificationCodeRepository.save(verificationCodeFactory.createActivationCode(user));
    }

    protected List<VerificationCode> getUsersVerificationCodes(@NotNull User user, EVerificationKeyType type) {
        return user.getVerificationCodes()
                .stream()
                .filter(verificationCode -> verificationCode.getType().equals(type))
                .collect(Collectors.toList());
    }
}
