package com.bookretail.enums;

public enum EDetail {
    LESS, MORE;

    public static boolean isLess(EDetail detail) {
        return detail.equals(LESS);
    }

    public static boolean isMore(EDetail detail) {
        return detail.equals(MORE);
    }
}
