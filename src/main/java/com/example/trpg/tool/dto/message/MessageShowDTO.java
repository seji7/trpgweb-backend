package com.example.trpg.tool.dto.message;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageShowDTO {
    private final Long roomId;
    private final String senderUsername;
    private final Long senderId;
    private final String content;
    private final LocalDateTime createdAt;

    public MessageShowDTO(Long roomId, String senderUsername, Long senderId, String content, LocalDateTime createdAt) {
        this.roomId = roomId;
        this.senderUsername = senderUsername;
        this.senderId = senderId;
        this.content = content;
        this.createdAt = createdAt;
    }
}