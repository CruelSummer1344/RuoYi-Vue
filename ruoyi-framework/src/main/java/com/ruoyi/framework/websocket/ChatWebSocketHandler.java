package com.ruoyi.framework.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(ChatWebSocketHandler.class);
    private static final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, WebSocketSession> staffSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        String role = (String) session.getAttributes().get("role");

        if ("staff".equals(role)) {
            staffSessions.put(userId, session);
            log.info("客服上线: {}", userId);
        } else {
            sessions.put(userId, session);
            log.info("用户连接: {}", userId);
        }
        broadcastStaffStatus();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String userId = (String) session.getAttributes().get("userId");
        String role = (String) session.getAttributes().get("role");

        if ("user".equals(role)) {
            // 用户发送消息给所有客服
            sendMessageToStaff(userId, payload);
        } else if ("staff".equals(role) && payload.startsWith("to:")) {
            // 客服回复特定用户，格式 "to:userId:内容"
            String[] parts = payload.split(":", 3);
            if (parts.length == 3) {
                String targetUserId = parts[1];
                String content = parts[2];
                sendMessageToUser(targetUserId, "客服回复: " + content);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        String role = (String) session.getAttributes().get("role");

        if ("staff".equals(role)) {
            staffSessions.remove(userId);
            log.info("客服下线: {}", userId);
        } else {
            sessions.remove(userId);
            log.info("用户断开: {}", userId);
        }
        broadcastStaffStatus();
    }

    private void sendMessageToUser(String userId, String message) throws IOException {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }

    private void sendMessageToStaff(String userId, String message) throws IOException {
        for (WebSocketSession staffSession : staffSessions.values()) {
            if (staffSession.isOpen()) {
                staffSession.sendMessage(new TextMessage("用户 " + userId + ": " + message));
            }
        }
    }

    private void broadcastStaffStatus() throws IOException {
        boolean online = !staffSessions.isEmpty();
        TextMessage statusMessage = new TextMessage("staffStatus:" + online);
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                session.sendMessage(statusMessage);
            }
        }
    }
}