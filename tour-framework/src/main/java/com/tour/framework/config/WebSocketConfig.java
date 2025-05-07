package com.tour.framework.config;

import com.tour.framework.websocket.ChatWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    static {
        System.out.println("----------------------------------");
        System.out.println("------   WebSocket服务启动   -------");
        System.out.println("----------------------------------");
    }

    @Bean
    public ServletServerContainerFactoryBean containerFactoryBean() {
        ServletServerContainerFactoryBean bean = new ServletServerContainerFactoryBean();
        bean.setMaxTextMessageBufferSize(1024 * 1024 * 10);
        bean.setMaxBinaryMessageBufferSize(1024 * 1024 * 10);
        bean.setMaxSessionIdleTimeout(30 * 1000L);
        return bean;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("注册 WebSocket 端点: /websocket/chat");
        registry.addHandler(new ChatWebSocketHandler(), "/websocket/chat")
                .setAllowedOrigins("*");
    }

    @Bean
    public ChatWebSocketHandler ChatWebSocketHandler() {
        return new ChatWebSocketHandler();
    }
}