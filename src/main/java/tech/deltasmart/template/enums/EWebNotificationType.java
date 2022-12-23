package com.bookretail.enums;

import org.springframework.data.util.Pair;


public enum EWebNotificationType {
    ALL(Pair.of(true, true)),
    RESERVATION(Pair.of(false, false)),
    FAVORITE(Pair.of(false, true));

    //For "is null" check
    private final Pair<Boolean, Boolean> value;

    EWebNotificationType(Pair<Boolean, Boolean> type) {
        value = type;
    }

    public Boolean getParking() {
        return value.getFirst();
    }

    public Boolean getParkingSection() {
        return value.getSecond();
    }
}
