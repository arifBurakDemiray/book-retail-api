package com.bookretail.util.service.notification.onesignal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import com.bookretail.util.service.notification.onesignal.helper.MultiLanguageText;

import java.util.List;
import java.util.Set;

public class PersonalMobileNotification extends MobileNotification {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("include_player_ids")
    private final Set<String> recipients;

    public PersonalMobileNotification(@NotNull String playerId,
                                      @NotNull MultiLanguageText contents,
                                      @NotNull MultiLanguageText headings
    ) {
        super(contents, headings);
        recipients = Set.of(playerId);
    }

    public PersonalMobileNotification(@NotNull List<String> playerIds,
                                      @NotNull MultiLanguageText contents,
                                      @NotNull MultiLanguageText headings
    ) {
        super(contents, headings);
        recipients = Set.copyOf(playerIds);
    }

    @Override
    @JsonIgnore
    public Set<String> getRecipient() {
        return recipients;
    }
}
