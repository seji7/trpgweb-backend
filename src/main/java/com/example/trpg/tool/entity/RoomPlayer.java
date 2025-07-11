package com.example.trpg.tool.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "room_player")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class RoomPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_rno", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_mid", nullable = false)
    private Member member;

    // 추후 확장을 위한 자리
    // private LocalDateTime joinedAt;
    // private String role;  // "GM", "Player"
    // private boolean active;
}
