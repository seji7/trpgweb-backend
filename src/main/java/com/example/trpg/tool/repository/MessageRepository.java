package com.example.trpg.tool.repository;

import com.example.trpg.tool.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByRoom_RnoOrderByCreatedAtAsc(Long roomId);
}
