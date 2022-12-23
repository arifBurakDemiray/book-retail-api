package com.bookretail.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import com.bookretail.enums.ELocale;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Component
public class ReadingIsGoodLocaleResolver extends SessionLocaleResolver {
    @NotNull
    @Override
    public Locale resolveLocale(@NotNull HttpServletRequest request) {
        return ELocale.getProperLocale(request.getLocale());
    }
}
