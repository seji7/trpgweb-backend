package com.example.trpg.tool.dto.room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class RoomRegisterRequestDTO {
    private String title;
    private String description;
    private Integer guestAccessLevel;
    private MultipartFile thumbnail;
}
