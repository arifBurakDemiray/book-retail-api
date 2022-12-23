package com.bookretail.util.service.notification.websocket;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.TextMessage;
import com.bookretail.util.service.notification.INotification;

@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage implements INotification<String, TextMessage> {

    private String recipient;
    private TextMessage message;

    @Override
    public String getRecipient() {
        return recipient;
    }

    @Override
    public TextMessage getMessage() {
        return message;
    }
}
