package com.example.trpg.tool.exception.handler;

import com.example.trpg.tool.exception.CustomException;
import com.example.trpg.tool.exception.dto.ErrorResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDTO> handleCustomException(CustomException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(new ErrorResponseDTO(
                        e.getErrorCode().name(),
                        e.getErrorCode().getMessage()
                ));
    }
}