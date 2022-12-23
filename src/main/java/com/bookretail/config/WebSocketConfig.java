package com.bookretail.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.bookretail.config.security.JwtUtil;
import com.bookretail.util.service.notification.websocket.WebSocketHandShakeInterceptor;
import com.bookretail.util.service.notification.websocket.WebSocketNotificationService;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    @Value("${server.websocket.baseUrl}")
    private String wsBaseUrl;

    private final JwtUtil jwtUtil;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry
                .addHandler(webSocketHandler(), wsBaseUrl + "/connect")
                .setAllowedOrigins("*")
                .addInterceptors(webSocketHandShakeInterceptor());
    }

    private WebSocketHandShakeInterceptor webSocketHandShakeInterceptor() {
        return new WebSocketHandShakeInterceptor(jwtUtil);
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketNotificationService(new ObjectMapper());
    }

}
