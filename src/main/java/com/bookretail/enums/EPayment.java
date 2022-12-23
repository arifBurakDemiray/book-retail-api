package com.bookretail.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum EPayment {
    /**
     * DISCLAIMER: The value MUST be multiplier of 2.
     * This class behaves like chmod.
     */
    CASH(1),
    CREDIT_CARD(2);

    private final int value;

    EPayment(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static List<EPayment> from(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("value must be positive!");
        }

        return Arrays.stream(EPayment.values())
                .filter(payment -> (payment.getValue() | value) == value)
                .collect(Collectors.toList());
    }

    public static int to(List<EPayment> values) {
        return values.stream()
                .mapToInt(EPayment::getValue)
                .reduce(0, (a, b) -> a | b);
    }
}
