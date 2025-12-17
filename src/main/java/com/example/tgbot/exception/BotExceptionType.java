package com.example.tgbot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BotExceptionType {


    public static final Integer USER_NOT_FOUND = 10001;
    public static final Integer USER_ALREADY_EXISTS = 10002;
    public static final Integer INVALID_REQUEST_DATA = 10003;


}
