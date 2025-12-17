package com.example.tgbot.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegisterRequest {
    private String username;
    private String password;
    private String name;
    private String surname;
    private Integer age;
}
