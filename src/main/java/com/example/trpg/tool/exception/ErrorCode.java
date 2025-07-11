package com.example.trpg.tool.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 링크는 존재하지 않는 방입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자는 존재하지 않습니다."),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    ALREADY_JOINED(HttpStatus.CONFLICT, "이미 등록된 플레이어입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
