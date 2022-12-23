package com.bookretail.util.service.notification.websocket.message_handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PongMessage extends MessageHandlerResult {

    private final String event = "pong";

}
