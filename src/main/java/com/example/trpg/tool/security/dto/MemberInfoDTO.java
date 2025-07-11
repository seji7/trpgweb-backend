package com.example.trpg.tool.security.dto;

import com.example.trpg.tool.entity.Member;
import lombok.Getter;

@Getter
public class MemberInfoDTO {
    private Long mid;
    private String userId;
    private String username;
    private String nickname;
    private String userRole;

    public MemberInfoDTO(Member member) {
        this.mid = member.getMid();
        this.userId = member.getUserId();
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.userRole = member.getUserRole();
    }
}