package com.example.trpg.tool.socket;

import com.example.trpg.tool.entity.Member;
import com.example.trpg.tool.entity.Message;
import com.example.trpg.tool.entity.Room;
import com.example.trpg.tool.repository.MemberRepository;
import com.example.trpg.tool.repository.RoomRepository;
import com.example.trpg.tool.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RoomMessageHandler extends TextWebSocketHandler {

    // 방번호별 연결된 사용자 세션들
    private final Map<String, List<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    // DB 저장을 위한 매퍼, 서비스, 리포지토리.
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MessageService messageService;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

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

        try {
            Map<String, Object> payload = objectMapper.readValue(raw, Map.class);
            Long senderId = Long.valueOf(payload.get("senderId").toString());
            String content = payload.get("content").toString();

            // 🔥 DB 저장
            Room room = roomRepository.findById(Long.valueOf(roomId)).orElseThrow();
            Member sender = memberRepository.findById(senderId).orElseThrow();
            Message newMessage = Message.builder()
                    .room(room)
                    .sender(sender)
                    .content(content)
                    .build();
            messageService.saveMessage(newMessage);

            // 🔁 브로드캐스트용 JSON 재작성
            String formatted = objectMapper.writeValueAsString(Map.of(
                    "senderId", sender.getMid(),
                    "senderUsername", sender.getUsername(),
                    "content", content,
                    "createdAt", newMessage.getCreatedAt().toString()
            ));

            for (WebSocketSession s : sessions) {
                if (s.isOpen()) s.sendMessage(new TextMessage(formatted));
            }

        } catch (Exception e) {
            System.err.println("❌ WebSocket 메시지 처리 중 오류: " + raw);
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