package com.bookretail.util.service.notification.onesignal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import com.bookretail.util.service.notification.INotification;
import com.bookretail.util.service.notification.onesignal.helper.MultiLanguageText;
import com.bookretail.util.service.notification.onesignal.helper.NotificationButton;

import java.util.List;
import java.util.Set;

/**
 * OneSignal Notification Mapping
 * Documentation: https://documentation.onesignal.com/reference/create-notification
 */
public abstract class MobileNotification implements INotification<Set<String>, MultiLanguageText> {
    @JsonProperty("contents")
    private final MultiLanguageText contents;

    @JsonProperty("headings")
    private final MultiLanguageText headings;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name = null;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MultiLanguageText subtitle = null;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("data")
    private Object additionalData;

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<NotificationButton> buttons;

    public MobileNotification(@NotNull MultiLanguageText contents,
                              @NotNull MultiLanguageText headings
    ) {
        this.contents = contents;
        this.headings = headings;
    }
    
    @Override
    @JsonIgnore
    public MultiLanguageText getMessage() {
        return contents;
    }

    @JsonIgnore
    public MultiLanguageText getHeading() {
        return headings;
    }
}
