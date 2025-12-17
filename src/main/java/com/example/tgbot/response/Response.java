package com.example.tgbot.response;

import lombok.Data;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
public class Response<T> {
    private T data;
    private ResponseStatus status;
}
