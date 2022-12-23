package com.bookretail.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class StringUtil {
    public static boolean isValid(String value) {
        return value != null && !value.isEmpty();
    }

    public static String getMessageFromBindingResult(@NotNull BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }
}
