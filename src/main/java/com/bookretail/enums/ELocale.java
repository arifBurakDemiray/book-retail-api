package com.bookretail.enums;

import java.util.Arrays;
import java.util.Locale;

public enum ELocale {
    TR(new Locale("tr")),
    EN(new Locale("en"));

    private final Locale locale;

    ELocale(Locale locale) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public static Locale getProperLocale(Locale locale) {
        return Arrays
                .stream(values())
                .filter(e -> e.getLocale().getLanguage().equals(locale.getLanguage()))
                .findFirst()
                .orElse(EN)
                .getLocale();
    }
}
