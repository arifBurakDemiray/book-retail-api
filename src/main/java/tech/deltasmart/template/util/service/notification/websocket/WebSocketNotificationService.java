package com.bookretail.util.service.notification.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.bookretail.util.service.notification.INotificationResult;
import com.bookretail.util.service.notification.INotificationService;
import com.bookretail.util.service.notification.NotificationException;
import com.bookretail.util.service.notification.websocket.message_handler.EventMessage;
import com.bookretail.util.service.notification.websocket.message_handler.IMessageHandler;
import com.bookretail.util.service.notification.websocket.message_handler.PingMessageHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WebSocketNotificationService extends TextWebSocketHandler implements INotificationService<WebSocketMessage> {

    @Getter
    private final ObjectWriter objectWriter;

    private final ObjectMapper objectMapper;

    private final Map<String, IMessageHandler> handlers = Map.of(
            "ping", new PingMessageHandler()
    );

    public WebSocketNotificationService(@NotNull ObjectMapper mapper) {
        objectWriter = mapper.writerWithDefaultPrettyPrinter();
        objectMapper = mapper;
    }

    @Override
    public INotificationResult send(WebSocketMessage notification) throws NotificationException {
        SESSIONS.POOL.forEach((key, value) -> {
            try {
                value.sendMessage(notification.getMessage());
                log.info("Send message to socketId: {}", key);

            } catch (IOException e) {
                log.error("WebSocketNotificationService at line 28: " + e.getMessage());
            }
        });

        return WebSocketNotificationResult.success();
    }

    @Override
    public List<INotificationResult> send(List<WebSocketMessage> notifications) throws NotificationException {

        TextMessage messages;

        try {
            messages = new TextMessage(objectWriter.writeValueAsString(notifications
                    .stream()
                    .map(WebSocketMessage::getMessage)
                    .collect(Collectors.toList())));
        } catch (JsonProcessingException e) {
            log.error("WebSocketNotificationService at line 45 for object write: " + e.getMessage());
            messages = new TextMessage("");
        }

        TextMessage finalMessages = messages;

        SESSIONS.POOL.forEach((key, value) -> {
            try {
                value.sendMessage(finalMessages);
                log.info("Send message to socketId: {}", key);

            } catch (IOException e) {
                log.error("WebSocketNotificationService at line 28: " + e.getMessage());
            }
        });

        return Collections.singletonList(WebSocketNotificationResult.success());
    }

    @Override
    public void handleTransportError(@NotNull WebSocketSession session, @NotNull Throwable throwable) {
        log.error("Error occured at sender " + session, throwable);
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        log.info(String.format("Session %s closed because of %s", session.getId(), status.getReason()));
        SESSIONS.POOL.remove(session.getId());
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        log.info(session.getId() + " connected with WebSocket");
        SESSIONS.POOL.put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) {

        try {
            EventMessage msg = objectMapper.readValue(message.getPayload(), EventMessage.class);

            var handler = handlers.get(msg.getEvent());

            if (handler != null) {
                session.sendMessage(new TextMessage(objectWriter.writeValueAsString(handler.invoke())));
            } else {
                throw new UnsupportedOperationException("This event is not supported: " + msg.getEvent());
            }

        } catch (Exception e) {
            log.warn("Incoming message: {} with error {}", message, e.getMessage());
        }
    }
}
