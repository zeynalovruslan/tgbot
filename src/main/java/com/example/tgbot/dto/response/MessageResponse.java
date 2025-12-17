package com.example.tgbot.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageResponse {
    private Long id;
    private String content;
    private String type;
    private String direction;
    private LocalDateTime createdAt;

}
