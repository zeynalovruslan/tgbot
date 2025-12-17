package com.example.tgbot.dto.request;

import lombok.Data;

@Data
public class MessageSendRequest {
    private String username;
    private String content;
    private String type;
    private String direction;
}
