package com.example.tgbot.exception;

import com.example.tgbot.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BotException.class)
    public ResponseEntity<ExceptionResponse> handleBotException(BotException e) {
        ExceptionResponse<String> response = new ExceptionResponse<>();
        response.setSuccess(false);
        response.setMessage(e.getMessage());
        response.setData(null);

        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
                .body(response);

    }


}
