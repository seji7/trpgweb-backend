package com.example.trpg.tool.dto;

import com.example.trpg.tool.entity.Member;
import lombok.Getter;

@Getter
public class MemberInfoDTO {
    private Long mid;
    private String userId;
    private String nickname;
    private int accountLevel;

    public MemberInfoDTO(Member member) {
        this.mid = member.getMid();
        this.userId = member.getUserId();
        this.nickname = member.getNickname();
        this.accountLevel = member.getAccountLevel().getCode();
    }
}