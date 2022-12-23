package com.bookretail.util.service.notification.websocket.message_handler;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PingMessageHandler implements IMessageHandler {

    @Override
    public MessageHandlerResult invoke() {
        return new PongMessage();
    }
}
