package com.example.trpg.tool.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String username; // userId에 대응 (Spring Security는 username으로 받음)
    private String password;
}