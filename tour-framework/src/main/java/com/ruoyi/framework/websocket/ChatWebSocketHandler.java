package com.ruoyi.framework.websocket;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = session.getId();
        String userType = (String) session.getAttributes().get("userType");

        if ("admin".equals(userType)) {
            staffSessions.put(userId, session);
            log.info("客服上线: {}", userId);
        } else if ("user".equals(userType)) {
            sessions.put(userId, session);
            log.info("用户连接: {}", userId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            String type = jsonNode.get("type").asText();

            if ("connect".equals(type)) {
                // 处理连接消息
                String userId = session.getId();
                String role = jsonNode.get("userType").asText(); // 使用 userType 作为 role
                session.getAttributes().put("userId", userId);
                session.getAttributes().put("role", role.equals("admin") ? "staff" : "user");

                if ("staff".equals(session.getAttributes().get("role"))) {
                    staffSessions.put(userId, session);
                    log.info("客服上线: {}", userId);
                } else {
                    sessions.put(userId, session);
                    log.info("用户连接: {}", userId);
                }
            } else if ("message".equals(type)) {
                // 处理普通消息
                String userId = session.getId();
                String role = jsonNode.get("userType").asText();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("time", jsonNode.get("time").asText());
                jsonObject.put("content", jsonNode.get("content").asText());

                if ("user".equals(role)) {
                    // 用户发送消息给所有客服
                    sendMessageToStaff(jsonObject.toString());
                } else if ("admin".equals(role)) {
                    sendMessageToUser(jsonObject.toString());
                }
            }
        } catch (Exception e) {
            log.error("处理消息失败: {}", e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = (String) session.getAttributes().get("userId");
        String role = (String) session.getAttributes().get("role");

        if (userId != null) {
            if ("staff".equals(role)) {
                staffSessions.remove(userId);
                log.info("客服下线: {}", userId);
            } else {
                sessions.remove(userId);
                log.info("用户断开: {}", userId);
            }
        }
    }

    private void sendMessageToUser(String message) throws IOException {
        for (WebSocketSession session : sessions.values()) {
            if (session != null && session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }

    private void sendMessageToStaff(String message) throws IOException {
        for (WebSocketSession staffSession : staffSessions.values()) {
            if (staffSession.isOpen()) {
                staffSession.sendMessage(new TextMessage(message));
            }
        }
    }

}