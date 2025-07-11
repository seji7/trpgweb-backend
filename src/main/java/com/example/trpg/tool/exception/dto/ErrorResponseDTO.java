package com.example.trpg.tool.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDTO {
    private String code;  // ex: "ROOM_NOT_FOUND"
    private String message;
}