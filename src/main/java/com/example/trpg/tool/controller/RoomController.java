package com.example.trpg.tool.controller;

import com.example.trpg.tool.dto.common.ApiResponse;
import com.example.trpg.tool.dto.room.AddPlayerRequestDTO;
import com.example.trpg.tool.dto.room.RoomRegisterRequestDTO;
import com.example.trpg.tool.dto.room.RoomResponseDTO;
import com.example.trpg.tool.security.CustomUserDetails;
import com.example.trpg.tool.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/register-room")
    public ResponseEntity<ApiResponse<RoomResponseDTO>> registerRoom(
            @ModelAttribute RoomRegisterRequestDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long ownerMid = userDetails.getMember().getMid();
        RoomResponseDTO response = roomService.createRoom(dto, ownerMid);
        return ResponseEntity.ok(new ApiResponse<>(true, "방 생성 완료", response));
    }

    @GetMapping("/list-data")
    public ResponseEntity<ApiResponse<Page<RoomResponseDTO>>> getRoomList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "40") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        String username = (userDetails != null)
                ? userDetails.getMember().getUserId()
                : "anonymous";

        log.info("방 목록 조회 요청 - 요청자: {}", username);

        Page<RoomResponseDTO> roomPage = roomService.getRoomPage(page, size);
        return ResponseEntity.ok(new ApiResponse<>(true, "방 목록 조회 성공", roomPage));
    }

    @GetMapping("/detail-data/{rno}")
    public ResponseEntity<ApiResponse<RoomResponseDTO>> getRoomDetail(@PathVariable Long rno) {
        RoomResponseDTO room = roomService.getRoomDetail(rno);
        return ResponseEntity.ok(new ApiResponse<>(true, "방 상세 조회 성공", room));
    }

    @PostMapping("/{rno}/add-player")
    public ResponseEntity<ApiResponse<Void>> addPlayer(
            @PathVariable Long rno,
            @RequestBody AddPlayerRequestDTO dto
    ) {
        roomService.addPlayerToRoom(rno, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "플레이어 추가 완료", null));
    }

    @DeleteMapping("/delete/{rno}")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(
            @PathVariable Long rno,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberMid = userDetails.getMember().getMid();
        roomService.deleteRoom(rno, memberMid);
        return ResponseEntity.ok(new ApiResponse<>(true, "방 삭제 완료", null));
    }
}