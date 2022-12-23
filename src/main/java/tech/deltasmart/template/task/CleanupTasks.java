package com.bookretail.task;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.bookretail.repository.VerificationCodeRepository;

@Component
@AllArgsConstructor
public class CleanupTasks {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final VerificationCodeRepository verificationCodeRepository;

    @Scheduled(cron = "${application.cron}", zone = "Europe/Istanbul")
    @Transactional
    public void deleteExpiredVerificationCodes() {
        long beforeCount = verificationCodeRepository.count();
        verificationCodeRepository.deleteExpired();
        long deletedCount = beforeCount - verificationCodeRepository.count();

        logger.info("{} expired codes removed.", deletedCount);
    }
}
