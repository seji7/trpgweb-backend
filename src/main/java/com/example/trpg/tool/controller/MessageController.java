package com.example.trpg.tool.controller;

import com.example.trpg.tool.dto.common.ApiResponse;
import com.example.trpg.tool.dto.message.MessageShowDTO;
import com.example.trpg.tool.entity.Message;
import com.example.trpg.tool.entity.Member;
import com.example.trpg.tool.entity.Room;
import com.example.trpg.tool.service.MessageService;
import com.example.trpg.tool.service.RoomService;
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
    private final RoomService roomService;

    @PostMapping("/{roomId}")
    public ResponseEntity<?> sendMessage(
            @PathVariable Long roomId,
            @RequestBody String content,
            @AuthenticationPrincipal(expression = "member") Member sender
    ) {
        Room room = roomService.getRoomEntity(roomId);

        Message message = Message.builder()
                .room(room)
                .sender(sender)
                .content(content)
                .build();

        messageService.saveMessage(message);
        return ResponseEntity.ok(new ApiResponse<>(true, "메시지 전송 완료", null));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<List<MessageShowDTO>>> getMessages(@PathVariable Long roomId) {
        List<MessageShowDTO> messages = messageService.getMessagesByRoomId(roomId);
        return ResponseEntity.ok(new ApiResponse<>(true, "메시지 목록 조회 성공", messages));
    }
}