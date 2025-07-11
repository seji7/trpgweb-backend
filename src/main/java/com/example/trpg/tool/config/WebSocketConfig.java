package com.example.trpg.tool.config;

import com.example.trpg.tool.socket.RoomMessageHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final RoomMessageHandler roomMessageHandler;

    public WebSocketConfig(RoomMessageHandler roomMessageHandler) {
        this.roomMessageHandler = roomMessageHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(roomMessageHandler, "/ws/chat/**") // <- 이렇게 변경!
                .setAllowedOriginPatterns("*");                // allowCredentials 대응
        // .withSockJS(); // 필요 없으면 제거해도 됨
    }
}