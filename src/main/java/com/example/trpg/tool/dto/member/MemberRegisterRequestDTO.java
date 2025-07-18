package com.example.trpg.tool.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRegisterRequestDTO {
    private String userId;
    private String password;
    private String username;
    private String nickname;
    private String userAddress;
    private String userPhone;
    private String userRole;
}