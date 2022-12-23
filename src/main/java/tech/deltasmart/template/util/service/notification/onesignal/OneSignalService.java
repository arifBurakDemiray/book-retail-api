package com.bookretail.util.service.notification.onesignal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;
import com.bookretail.config.OneSignalConfig;
import com.bookretail.util.service.notification.INotificationResult;
import com.bookretail.util.service.notification.INotificationService;
import com.bookretail.util.service.notification.NotificationException;
import com.bookretail.util.service.notification.onesignal.helper.OneSignalCreateNotificationResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OneSignalService implements INotificationService<MobileNotification> {
    private static final String url = "https://onesignal.com/api/v1/notifications";
    private final OneSignalConfig config;


    @Override
    public INotificationResult send(MobileNotification notification) throws NotificationException {
        try {
            var mapper = new ObjectMapper();
            var con = (HttpURLConnection) new URL(OneSignalService.url).openConnection();

            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic " + config.getApiKey());
            con.setRequestMethod("POST");
            OutputStream outputStream = con.getOutputStream();

            var body = new NotificationBody(notification);
            mapper.writeValue(outputStream, body);

            if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                var response = br.lines().collect(Collectors.joining());

                return mapper.readValue(
                        response,
                        OneSignalCreateNotificationResult.class
                );

            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                var response = br.lines().collect(Collectors.joining());
                throw new NotificationException(response);
            }
        } catch (JsonProcessingException e) {
            throw new NotificationException("Could not serialize notification data.", e);
        } catch (Exception e) {
            throw new NotificationException("An error occurred.", e);
        }
    }

    @Override
    public List<INotificationResult> send(List<MobileNotification> notifications) throws NotificationException {

        List<INotificationResult> results = new ArrayList<>();

        for (var notification : notifications) {
            var result = send(notification);

            results.add(result);
        }

        return results;
    }

    @Getter
    @AllArgsConstructor
    private class NotificationBody {
        @JsonProperty("app_id")
        private final String appId = config.getAppId();

        @JsonUnwrapped
        private final MobileNotification notification;
    }
}
