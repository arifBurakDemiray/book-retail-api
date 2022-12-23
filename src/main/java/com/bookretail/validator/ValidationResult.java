package com.bookretail.validator;

import org.jetbrains.annotations.NotNull;

public class ValidationResult {

    private final String message;
    private final boolean isValid;

    protected ValidationResult(String message, boolean isValid) {
        this.isValid = isValid;
        this.message = message;
    }

    @NotNull
    public static ValidationResult success() {
        return new ValidationResult(null, true);
    }

    @NotNull
    public static ValidationResult failed(@NotNull String message) {
        return new ValidationResult(message, false);
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isNotValid() {
        return !isValid();
    }

    public String getMessage() {
        return message;
    }
}
