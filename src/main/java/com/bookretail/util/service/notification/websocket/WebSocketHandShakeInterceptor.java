package com.bookretail.util.service.notification.websocket;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import com.bookretail.config.security.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@AllArgsConstructor
public class WebSocketHandShakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest serverHttpRequest,
                                   @NotNull ServerHttpResponse serverHttpResponse,
                                   @NotNull WebSocketHandler webSocketHandler,
                                   @NotNull Map<String, Object> map) {
        ServletServerHttpRequest ssreq = (ServletServerHttpRequest) serverHttpRequest;
        ServletServerHttpResponse ssres = (ServletServerHttpResponse) serverHttpResponse;
        HttpServletRequest req = ssreq.getServletRequest();
        HttpServletResponse res = ssres.getServletResponse();

        try {

            var token = req.getParameter("access_token");

            if (token != null) {
                if (!jwtUtil.validateToken(token)) {
                    throw new Exception("Token Invalid");
                }
            }
        } catch (Exception e) {

            res.setStatus(HttpServletResponse.SC_FORBIDDEN);

            return false;
        }

        return true;
    }

    @Override
    public void afterHandshake(@NotNull ServerHttpRequest serverHttpRequest,
                               @NotNull ServerHttpResponse serverHttpResponse,
                               @NotNull WebSocketHandler webSocketHandler, Exception e) {

    }
}
