package com.bookretail.enums;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import com.bookretail.util.status.EStatusCode;

import java.util.Arrays;
import java.util.List;

public enum ESuccessCode implements EStatusCode {
    SUCCESSFUL("200"),
    CREATED("201");

    private final String code;

    ESuccessCode(String code) {
        this.code = code;
    }

    @NotNull
    @Contract(pure = true)
    public static List<ESuccessCode> successCodes() {
        return Arrays.asList(SUCCESSFUL, CREATED);
    }

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
