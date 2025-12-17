package com.example.tgbot.service;

import com.example.tgbot.dto.response.MessageResponse;
import com.example.tgbot.dto.response.UserResponse;
import com.example.tgbot.entity.User;
import com.example.tgbot.response.Response;

import java.util.List;

public interface MessageService {

    Response<MessageResponse> saveMessage(String userName, String content, String type, String direction);


    Response<List<MessageResponse>> getMessagesByUser(String userName);

    void sendOtp(Long userId);


}
