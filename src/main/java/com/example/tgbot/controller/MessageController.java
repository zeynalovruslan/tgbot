package com.example.tgbot.controller;

import com.example.tgbot.dto.request.MessageSendRequest;
import com.example.tgbot.dto.response.MessageResponse;
import com.example.tgbot.response.Response;
import com.example.tgbot.service.MessageService;
import com.example.tgbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;


    @PostMapping("/send")
    public Response<MessageResponse> sendMessage(@RequestBody MessageSendRequest request) {
        return messageService.saveMessage(request.getUsername(), request.getContent(), request.getType(), request.getDirection()
        );
    }


    @GetMapping("/history")
    public Response<List<MessageResponse>> getHistory(@RequestParam String username) {
        return messageService.getMessagesByUser(username);
    }
}
