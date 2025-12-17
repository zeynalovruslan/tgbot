package com.example.tgbot.exception;

import lombok.Getter;

@Getter
public class BotException extends RuntimeException {

    private final int errorCode;

    public BotException(int code, String message) {
        super(message);

        this.errorCode = code;
    }
}
