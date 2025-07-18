package com.example.trpg.tool.service.impl;

import com.example.trpg.tool.dto.message.MessageShowDTO;
import com.example.trpg.tool.entity.Message;
import com.example.trpg.tool.repository.MessageRepository;
import com.example.trpg.tool.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    @Override
    public List<MessageShowDTO> getMessagesByRoomId(Long roomId) {
        return messageRepository.findByRoom_RnoOrderByCreatedAtAsc(roomId)
                .stream()
                .map(message -> new MessageShowDTO(
                        message.getRoom().getRno(),
                        message.getSender().getUsername(),
                        message.getSender().getMid(),
                        message.getContent(),
                        message.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}