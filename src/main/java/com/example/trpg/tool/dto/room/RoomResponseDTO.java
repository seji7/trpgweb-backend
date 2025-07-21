package com.example.trpg.tool.dto.room;

import com.example.trpg.tool.entity.Room;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RoomResponseDTO {

    private Long rno;
    private String title;
    private String description;
    private String ownerNickname;
    private Long ownerMid;
    private Integer guestAccessLevel;
    private LocalDateTime createdAt;
    private LocalDateTime lastUsedAt;
    private String thumbnailUrl;
    private int accountLevel;

    public static RoomResponseDTO fromEntity(Room room) {
        return RoomResponseDTO.builder()
                .rno(room.getRno())
                .title(room.getTitle())
                .description(room.getDescription())
                .ownerNickname(room.getOwner().getNickname())
                .ownerMid(room.getOwner().getMid())
                .guestAccessLevel(room.getGuestAccessLevel())
                .createdAt(room.getCreatedAt())
                .lastUsedAt(room.getLastUsedAt())
                .thumbnailUrl(room.getThumbnailUrl())
                .accountLevel(room.getAccountLevel().getCode())
                .build();
    }

    public static RoomResponseDTO from(Room room) {
        return RoomResponseDTO.builder()
                .rno(room.getRno())
                .title(room.getTitle())
                .description(room.getDescription())
                .thumbnailUrl(room.getThumbnailUrl())
                .build();
    }
}
