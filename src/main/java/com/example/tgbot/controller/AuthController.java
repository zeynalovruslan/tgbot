package com.example.tgbot.controller;

import com.example.tgbot.dto.request.UserLoginRequest;
import com.example.tgbot.dto.request.UserRegisterRequest;
import com.example.tgbot.dto.response.UserResponse;
import com.example.tgbot.response.Response;
import com.example.tgbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final UserService userService;

    @PostMapping("/registration")
    public Response<UserResponse> registration(@RequestBody UserRegisterRequest userRegisterRequest) {

        return userService.registration(userRegisterRequest);


    }

    @PostMapping("/login")
    public Response<UserResponse> login(@RequestBody UserLoginRequest loginRequest, String chatId) {

        return userService.login(loginRequest, chatId);
    }

    @PostMapping("/verify-otp")
    public Response<UserResponse> verifyLoginToken(@RequestParam String username, String token, String chatId) {
        return userService.verifyLoginToken(username, token, chatId);
    }

}
