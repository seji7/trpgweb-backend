package com.example.trpg.tool.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "room")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rno")
    private Long rno;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 100)
    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "account_level", nullable = false)
    private AccountLevel accountLevel = AccountLevel.NORMAL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_mid", nullable = false)
    private Member owner;

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomPlayer> roomPlayers = new HashSet<>();

    // 0: 게스트 자유, 1: 보기만, 2: 차단
    @Column(name = "guest_access_level", nullable = false)
    private Integer guestAccessLevel;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_used_at", nullable = false)
    private LocalDateTime lastUsedAt;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastUsedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUsedAt = LocalDateTime.now();
    }
}