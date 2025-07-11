package com.example.trpg.tool.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomMessageHandler extends TextWebSocketHandler {

    // 방번호별 연결된 사용자 세션들
    private final Map<String, List<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = getRoomIdFromUri(session);
        System.out.println("✅ WebSocket 연결 수신됨: " + session.getUri());
        roomSessions.computeIfAbsent(roomId, k -> Collections.synchronizedList(new ArrayList<>())).add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomId = getRoomIdFromUri(session);
        List<WebSocketSession> sessions = roomSessions.getOrDefault(roomId, List.of());

        String raw = message.getPayload();

        // 일단 한번 파싱해서 확인 (Jackson 이용)
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> payload = mapper.readValue(raw, Map.class);

            // 예시로 senderId, content만 다시 포맷해서 JSON 문자열로 브로드캐스트
            String formatted = mapper.writeValueAsString(Map.of(
                    "senderId", payload.get("senderId"),
                    "content", payload.get("content")
            ));

            for (WebSocketSession s : sessions) {
                if (s.isOpen()) s.sendMessage(new TextMessage(formatted));
            }
        } catch (Exception e) {
            System.err.println("WebSocket 메시지 파싱 실패: " + raw);
            e.printStackTrace();
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = getRoomIdFromUri(session);
        roomSessions.getOrDefault(roomId, List.of()).remove(session);
    }

    private String getRoomIdFromUri(WebSocketSession session) {
        String path = Objects.requireNonNull(session.getUri()).getPath();  // 예: /ws/chat/5
        return path.substring(path.lastIndexOf("/") + 1);
    }
}