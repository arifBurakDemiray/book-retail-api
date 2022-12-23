package com.bookretail.util.service.notification.onesignal.helper;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.bookretail.enums.ELocale;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MultiLanguageText {
    @JsonAnyGetter
    private final Map<String, String> contents;

    public MultiLanguageText(String messageKey) {
        contents = Arrays
                .stream(ELocale.values())
                .collect(Collectors.toMap(
                        locale -> locale.getLocale().getLanguage(),
                        locale -> messageKey
                ));
    }

    public MultiLanguageText(Map<ELocale, String> contents) {
        this.contents = contents.entrySet().stream().collect(
                Collectors.toMap(
                        e -> e.getKey().getLocale().getLanguage(),
                        Map.Entry::getValue
                )
        );
    }

    @JsonIgnore
    public Map<String, String> getContents() {
        return Map.copyOf(contents);
    }
}
