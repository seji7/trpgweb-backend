package com.example.trpg.tool.controller;

import com.example.trpg.tool.dto.AddPlayerRequestDTO;
import com.example.trpg.tool.dto.RoomRegisterRequestDTO;
import com.example.trpg.tool.dto.RoomResponseDTO;
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

    // ===== API (JSON) methods =====

    @PostMapping("/register-room")
    @ResponseBody
    public ResponseEntity<RoomResponseDTO> registerRoom(
            @ModelAttribute RoomRegisterRequestDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long ownerMid = userDetails.getMember().getMid(); // 로그인된 사용자
        RoomResponseDTO response = roomService.createRoom(dto, ownerMid);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list-data")
    @ResponseBody
    public ResponseEntity<Page<RoomResponseDTO>> getRoomList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "40") int size
    ) {
        log.info("----입장-----");
        System.out.println("요청 들어옴");
        Page<RoomResponseDTO> roomPage = roomService.getRoomPage(page, size);
        return ResponseEntity.ok(roomPage);
    }

    @GetMapping("/detail-data/{rno}")
    @ResponseBody
    public ResponseEntity<RoomResponseDTO> getRoomDetail(@PathVariable Long rno) {
        RoomResponseDTO room = roomService.getRoomDetail(rno);
        return ResponseEntity.ok(room);
    }

    @PostMapping("/{rno}/add-player")
    @ResponseBody
    public ResponseEntity<String> addPlayer(
            @PathVariable Long rno,
            @RequestBody AddPlayerRequestDTO dto
    ) {
        roomService.addPlayerToRoom(rno, dto);
        return ResponseEntity.ok("플레이어 추가 완료");
    }

    @DeleteMapping("/delete/{rno}")
    @ResponseBody
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long rno,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberMid = userDetails.getMember().getMid();
        roomService.deleteRoom(rno, memberMid);
        return ResponseEntity.ok().build();
    }
}