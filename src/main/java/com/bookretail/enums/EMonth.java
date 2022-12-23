package com.bookretail.enums;

import lombok.Getter;

public enum EMonth {
    JANUARY("01"),
    FEBRUARY("02"),
    MARCH("03"),
    APRIL("04"),
    MAY("05"),
    JUNE("06"),
    JULY("07"),
    AUGUST("08"),
    SEPTEMBER("09"),
    OCTOBER("10"),
    NOVEMBER("11"),
    DECEMBER("12");

    @Getter
    private final String value;

    EMonth(String value) {
        this.value = value;
    }
    
}
