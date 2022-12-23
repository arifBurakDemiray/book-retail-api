package com.bookretail.enums;

public enum ETimeGroup {
    DAY("%Y-%m-%d", 24),
    WEEK("%Y-%u", 24 * 7),
    MONTH("%Y-%m", 24 * 30);

    private final String value;
    private final int hour;

    ETimeGroup(String value, int hour) {

        this.value = value;
        this.hour = hour;
    }

    public String value() {
        return value;
    }

    public int hour() {
        return hour;
    }
}
