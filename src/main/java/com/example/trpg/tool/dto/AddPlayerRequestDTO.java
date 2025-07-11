package com.example.trpg.tool.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPlayerRequestDTO {
    private String username;  // 초대할 유저의 아이디
    private Long ownerMid;    // 요청 주체(owner의 mid)
}