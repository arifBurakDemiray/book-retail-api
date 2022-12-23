package com.bookretail.util;

import java.security.SecureRandom;
import java.util.Base64;

public class RandomUtil {
    private static final SecureRandom random = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    public static String generate() {
        byte[] buffer = new byte[20];
        random.nextBytes(buffer);
        return encoder.encodeToString(buffer);
    }

    public static String generateInvalid() {
        return encoder.encodeToString(new byte[20]);
    }
}
