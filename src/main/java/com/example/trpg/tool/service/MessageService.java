package com.example.trpg.tool.service;

import com.example.trpg.tool.dto.MessageShowDTO;
import com.example.trpg.tool.entity.Message;

import java.util.List;

public interface MessageService {
    void saveMessage(Message message);
    List<MessageShowDTO> getMessagesByRoomId(Long roomId);
}