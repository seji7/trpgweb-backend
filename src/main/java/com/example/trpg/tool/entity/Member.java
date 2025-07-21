package com.example.trpg.tool.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "member")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mid")
    private Long mid;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "user_address")
    private String userAddress;

    @Column(name = "user_phone", unique = true)
    private String userPhone;

    @Builder.Default
    @Column(name = "user_role")
    private String userRole = "User";

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomPlayer> joinedRooms = new HashSet<>();

    @Enumerated(EnumType.ORDINAL) // int로 저장 (0:NORMAL, 1:SPECIAL, 2:VIP)
    @Column(name = "premium_level", nullable = false)
    private AccountLevel accountLevel = AccountLevel.NORMAL;

    public enum UserRole {
        ROLE_USER, ROLE_ADMIN;
    }
}