package org.dixcord.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.context.ApplicationContext;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private ApplicationContext context;
    
    @Bean   // 빈 등록 추가
    public WebSocketHandler webSocketHandler() {
        return context.getBean(WebSocketChatHandler.class);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        try {
            WebSocketHandler webSocketHandler = context.getBean(WebSocketChatHandler.class);
            registry.addHandler(webSocketHandler, "/chat").setAllowedOrigins("*");
            registry.addHandler(webSocketHandler, "/socket.io").setAllowedOrigins("*");
        } catch (Exception e) {
            System.out.println("WebSocketHandler Bean 생성 오류: " + e.getMessage());
        }
    }
}
