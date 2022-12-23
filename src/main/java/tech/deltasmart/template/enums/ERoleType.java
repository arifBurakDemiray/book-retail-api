package com.bookretail.enums;

import org.jetbrains.annotations.NotNull;

public enum ERoleType {
    SYSADMIN, USER;

    public static int getRoleValue(@NotNull ERoleType type) {
        return ERole.valueOf("ROLE_" + type.name());
    }

    public static String getRole(@NotNull ERoleType type) {
        return ERole.stringValueOf(getRoleValue(type));
    }

}
