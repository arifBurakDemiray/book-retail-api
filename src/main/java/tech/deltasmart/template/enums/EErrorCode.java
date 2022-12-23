package com.bookretail.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import com.bookretail.util.status.EStatusCode;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum EErrorCode implements EStatusCode {
    UNHANDLED("500"),
    NOT_FOUND("404"),
    UNAUTHORIZED("401"),
    ACCESS_DENIED("403"),
    BAD_REQUEST("400");

    private final String code;

    @NotNull
    @Contract(pure = true)
    public static List<EErrorCode> errorCodes() {
        return Arrays.asList(UNHANDLED, NOT_FOUND, UNAUTHORIZED, ACCESS_DENIED, BAD_REQUEST);
    }

    EErrorCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

    @JsonCreator
    public static EErrorCode decode(@NotNull String code) {
        return EErrorCode.valueOf(code.toUpperCase(Locale.ENGLISH));
    }

    @JsonValue
    @Override
    public String getStatusCode() {
        return code;
    }

    @NotNull
    @Contract(pure = true)
    @Override
    public String getStatusName() {
        return name();
    }

    @Override
    public int httpStatusCode() {
        return Integer.parseUnsignedInt(code);
    }
}
