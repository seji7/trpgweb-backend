package com.example.trpg.tool.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ms_id")
    private Long msId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_rno", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_mid", nullable = false)
    private Member sender;  // 메시지를 보낸 사용자

    @Column(name = "content", nullable = false)
    private String content;  // 메시지 내용

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 메시지 전송 시간

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}