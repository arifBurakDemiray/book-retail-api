package com.bookretail.util.service.notification.onesignal.helper;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.bookretail.util.service.notification.INotificationResult;

import javax.annotation.Nullable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OneSignalCreateNotificationResult implements INotificationResult {
    @Nullable
    @JsonProperty("id")
    private String id;

    @Nullable
    @JsonProperty("recipients")
    private Integer recipients;

    @Nullable
    @JsonProperty("external_id")
    private String externalId;

    @Nullable
    @JsonProperty("errors")
    private Object errors;

    @Nullable
    @JsonProperty("warnings")
    private Object warnings;
}
