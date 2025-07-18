package com.example.trpg.tool.service;

import com.example.trpg.tool.dto.room.AddPlayerRequestDTO;
import com.example.trpg.tool.dto.room.RoomRegisterRequestDTO;
import com.example.trpg.tool.dto.room.RoomResponseDTO;
import com.example.trpg.tool.entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoomService {
    RoomResponseDTO createRoom(RoomRegisterRequestDTO dto, Long ownerMid);

    Page<RoomResponseDTO> getRoomPage(int page, int size);

    RoomResponseDTO getRoomDetail(Long rno);

    void addPlayerToRoom(Long rno, AddPlayerRequestDTO dto);

    void deleteRoom(Long rno, Long ownerMid);

    public List<RoomResponseDTO> getAllRooms();

    Room getRoomEntity(Long rno);
}
