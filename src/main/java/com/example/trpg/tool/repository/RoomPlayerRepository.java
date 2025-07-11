package com.example.trpg.tool.repository;

import com.example.trpg.tool.entity.RoomPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomPlayerRepository extends JpaRepository<RoomPlayer, Long> {
}
