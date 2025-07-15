package com.example.trpg.tool.dto;

import lombok.Data;

@Data
public class MemberUpdateRequest {
    private String username;
    private String nickname;
    private String userAddress;
    private String userPhone;
}