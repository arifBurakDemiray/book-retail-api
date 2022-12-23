package com.bookretail.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ESegment {
    ACTIVE_USERS("Active Users"),
    SUBSCRIBED_USERS("Subscribed Users"),
    INACTIVE_USERS("Inactive Users"),
    ENGAGED_USERS("Engaged Users");

    private final String serialized;

    ESegment(String serialized) {
        this.serialized = serialized;
    }

    @JsonValue
    public String serialize() {
        return serialized;
    }
}
