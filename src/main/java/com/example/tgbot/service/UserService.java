package com.example.tgbot.service;

import com.example.tgbot.dto.request.MessageSendRequest;
import com.example.tgbot.dto.request.UserLoginRequest;
import com.example.tgbot.dto.request.UserRegisterRequest;
import com.example.tgbot.dto.response.UserResponse;
import com.example.tgbot.entity.User;
import com.example.tgbot.response.Response;

public interface UserService {

    Response<UserResponse> registration(UserRegisterRequest userRegisterRequest);

    Response<UserResponse> login(UserLoginRequest userLoginRequest, String chatId);

    Response<UserResponse> verifyLoginToken(String username, String token, String chatId);

    Response<UserResponse>   findByUsername(String username);

    User getUserByUsername(String username);
}
