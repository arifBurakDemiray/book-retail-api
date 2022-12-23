package com.bookretail.util.service.notification.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SESSIONS {

    protected static final Map<String, WebSocketSession> POOL = new ConcurrentHashMap<>();

}
