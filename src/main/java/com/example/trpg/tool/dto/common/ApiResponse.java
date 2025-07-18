package com.example.trpg.tool.dto.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    // 사용 예: new ApiResponse<>(true, "로그인 성공", new TokenResponse(...))
}