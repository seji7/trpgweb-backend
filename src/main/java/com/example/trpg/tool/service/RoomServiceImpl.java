package com.example.trpg.tool.service;

import com.example.trpg.tool.dto.AddPlayerRequestDTO;
import com.example.trpg.tool.dto.RoomRegisterRequestDTO;
import com.example.trpg.tool.dto.RoomResponseDTO;
import com.example.trpg.tool.entity.Member;
import com.example.trpg.tool.entity.Room;
import com.example.trpg.tool.entity.RoomPlayer;
import com.example.trpg.tool.exception.CustomException;
import com.example.trpg.tool.exception.ErrorCode;
import com.example.trpg.tool.repository.MemberRepository;
import com.example.trpg.tool.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    public RoomResponseDTO createRoom(RoomRegisterRequestDTO dto, Long ownerMid) {
        String fileName = null;

        try {
            if (dto.getThumbnail() != null && !dto.getThumbnail().isEmpty()) {
                String uuid = UUID.randomUUID().toString();
                fileName = uuid + "_" + dto.getThumbnail().getOriginalFilename();

                String uploadDir = System.getProperty("user.dir") + File.separator + "upload-dir";
                Path savePath = Paths.get(uploadDir, fileName);

                Files.createDirectories(savePath.getParent());
                dto.getThumbnail().transferTo(savePath.toFile());
            }
        } catch (IOException | IllegalStateException e) {
            log.error("썸네일 파일 업로드 실패", e);
            throw new RuntimeException("썸네일 업로드 중 오류 발생", e);
        }

        Member owner = memberRepository.findById(ownerMid)
                .orElseThrow(() -> new IllegalArgumentException("소유자 정보가 올바르지 않습니다."));

        Room room = Room.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .owner(owner)
                .guestAccessLevel(dto.getGuestAccessLevel())
                .accountLevel(owner.getAccountLevel())
                .thumbnailUrl(fileName != null ? "/uploads/" + fileName : null)
                .build();

        roomRepository.save(room);
        return RoomResponseDTO.fromEntity(room);
    }

        @Override
        public Page<RoomResponseDTO> getRoomPage(int page, int size) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

            return roomRepository.findAll(pageable)
                    .map(RoomResponseDTO::fromEntity);
        }

    @Override
    public RoomResponseDTO getRoomDetail(Long rno) {
        Room room = roomRepository.findById(rno)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
        return RoomResponseDTO.fromEntity(room);
    }

    @Override
    public void addPlayerToRoom(Long rno, AddPlayerRequestDTO dto) {
        Room room = roomRepository.findById(rno)
                .orElseThrow(() -> new IllegalArgumentException("해당 링크는 존재하지 않는 방입니다."));

        if (!room.getOwner().getMid().equals(dto.getOwnerMid())) {
            throw new IllegalArgumentException("초대 권한이 없습니다.");
        }

        Member player = memberRepository.findByUserId(dto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다."));

        // 중복 등록 방지
        boolean alreadyJoined = room.getRoomPlayers().stream()
                .anyMatch(rp -> rp.getMember().getMid().equals(player.getMid()));
        if (alreadyJoined) {
            throw new IllegalArgumentException("이미 등록된 플레이어입니다.");
        }

        RoomPlayer roomPlayer = RoomPlayer.builder()
                .room(room)
                .member(player)
                .build();

        room.getRoomPlayers().add(roomPlayer);
        roomRepository.save(room);
    }

    @Override
    public List<RoomResponseDTO> getAllRooms() {
        List<Room> roomList = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "rno")); // 최근 생성된 순

        return roomList.stream()
                .map(room -> RoomResponseDTO.builder()
                        .rno(room.getRno())
                        .title(room.getTitle())
                        .description(room.getDescription())
                        .guestAccessLevel(room.getGuestAccessLevel())
                        .accountLevel(room.getAccountLevel().getCode())
                        .thumbnailUrl(room.getThumbnailUrl())
                        .build())
                .toList();
    }

    @Override
    @Transactional
    public void deleteRoom(Long rno, Long requesterMid) {
        Room room = roomRepository.findById(rno)
                .orElseThrow(() -> new CustomException(ErrorCode.ROOM_NOT_FOUND));

        if (!room.getOwner().getMid().equals(requesterMid)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        roomRepository.delete(room);
    }
}
