package com.example.trpg.tool.controller;

import com.example.trpg.tool.dto.MessageShowDTO;
import com.example.trpg.tool.entity.Message;
import com.example.trpg.tool.entity.Member;
import com.example.trpg.tool.entity.Room;
import com.example.trpg.tool.repository.MemberRepository;
import com.example.trpg.tool.repository.RoomRepository;
import com.example.trpg.tool.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    // 메시지 저장
    @PostMapping("/{roomId}")
    public ResponseEntity<?> sendMessage(
            @PathVariable Long roomId,
            @RequestBody String content,
            @AuthenticationPrincipal(expression = "member") Member sender
    ) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        Message message = Message.builder()
                .room(room)
                .sender(sender)
                .content(content)
                .build();

        messageService.saveMessage(message);
        return ResponseEntity.ok().build();
    }

    // 메시지 목록 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<List<MessageShowDTO>> getMessages(@PathVariable Long roomId) {
        List<MessageShowDTO> messages = messageService.getMessagesByRoomId(roomId);
        return ResponseEntity.ok(messages);
    }
}