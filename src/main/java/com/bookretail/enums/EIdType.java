package com.bookretail.enums;

public enum EIdType {
    PARKING("parkingSection,parking,id"),
    SECTION("parkingSection,id");

    private final String value;

    EIdType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}