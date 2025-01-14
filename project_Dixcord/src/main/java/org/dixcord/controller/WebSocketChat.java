package org.dixcord.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class WebSocketChat extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketChat.class);

    // 방 번호 기반 세션 저장
    private final Map<String, List<WebSocketSession>> roomSession = new HashMap<>();

    // 새로운 클라이언트 연결 시 호출
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = "1";  // 고정된 방 번호 1

        logger.info("Session connected: " + session.getId() + " in room: " + roomId);

        roomSession.computeIfAbsent(roomId, k -> new ArrayList<>()).add(session);

        // 클라이언트가 채팅 기록을 요청하는 기능 (필요시 채팅 히스토리 추가)
        session.sendMessage(new TextMessage("Connected to room " + roomId));
    }

    // 메시지 처리 메서드
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomId = "1";
        String sender = "User";

        // 날짜 및 시간 포맷팅
        String date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("a h:mm"));

        String formattedMessage = String.format("%s in room %s: %s %s %s",
                sender, roomId, message.getPayload(), date, time);

        logger.info("Message from " + sender + ": " + message.getPayload());

        // 방 내 다른 사용자에게 메시지 전송
        sendMessageToRoom(formattedMessage, roomId, session);
    }

    // 클라이언트 연결 종료 시 호출
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("Session closed: " + session.getId());

        // 세션 종료 시 방 내 세션 제거
        roomSession.values().forEach(sessions -> sessions.remove(session));
    }

    // 에러 발생 시 호출
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("Session error", exception);
    }

    // 메시지를 방 내 다른 사용자에게 전달
    private void sendMessageToRoom(String message, String roomId, WebSocketSession senderSession) throws Exception {
        List<WebSocketSession> sessions = roomSession.getOrDefault(roomId, new ArrayList<>());

        for (WebSocketSession session : sessions) {
            if (!senderSession.getId().equals(session.getId())) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (Exception e) {
                    logger.error("Sending message failed", e);
                }
            }
        }
    }
}
