package com.example.trpg.tool.controller;

import com.example.trpg.tool.dto.common.ApiResponse;
import com.example.trpg.tool.dto.room.RoomResponseDTO;
import com.example.trpg.tool.entity.Room;
import com.example.trpg.tool.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room/public")
public class PublicRoomController {

    private final RoomRepository roomRepository;

    @GetMapping("/{rno}")
    public ResponseEntity<ApiResponse<RoomResponseDTO>> viewRoomAsGuest(@PathVariable Long rno) {
        Room room = roomRepository.findById(rno)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 방입니다."));

        if (room.getGuestAccessLevel() == 2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, "게스트 접근이 허용되지 않은 방입니다.", null));
        }

        RoomResponseDTO dto = RoomResponseDTO.from(room);  // ← 서비스 거치지 않고 엔티티 직접 DTO 변환
        return ResponseEntity.ok(new ApiResponse<>(true, "게스트용 방 조회 성공", dto));
    }
}