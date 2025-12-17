package com.example.tgbot.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private Integer age;
    private LocalDateTime createdDate;
    private String loginToken;
}
