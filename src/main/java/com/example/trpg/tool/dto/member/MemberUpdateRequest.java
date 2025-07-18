package com.example.trpg.tool.dto.member;

import lombok.Data;

@Data
public class MemberUpdateRequest {
    private String userId;
    private String username;
    private String nickname;
    private String userAddress;
    private String userPhone;
}