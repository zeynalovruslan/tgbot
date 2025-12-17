package com.example.tgbot.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseStatus {

    private Integer code;
    private String message;

    public static ResponseStatus getSuccessMessage() {

        return new ResponseStatus(1, "Successfully Completed!!!");

    }
}
