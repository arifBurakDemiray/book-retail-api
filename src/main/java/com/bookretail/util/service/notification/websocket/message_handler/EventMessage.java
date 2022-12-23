package com.bookretail.util.service.notification.websocket.message_handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EventMessage {
    private String event;
}
