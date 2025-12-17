package com.example.tgbot.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse<T> {

    private boolean success;
    private String message;
    private T data;
}
