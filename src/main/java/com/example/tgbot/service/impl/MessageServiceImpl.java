package com.example.tgbot.service.impl;

import com.example.tgbot.dto.response.MessageResponse;
import com.example.tgbot.entity.Message;
import com.example.tgbot.entity.User;
import com.example.tgbot.enums.Direct;
import com.example.tgbot.enums.MessageType;
import com.example.tgbot.exception.BotException;
import com.example.tgbot.exception.BotExceptionType;
import com.example.tgbot.repository.MessageRepository;
import com.example.tgbot.repository.UserRepository;
import com.example.tgbot.response.Response;
import com.example.tgbot.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageServiceImpl implements MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public Response<MessageResponse> saveMessage(String userName, String content, String type, String direction) {

        log.info("Message saved to user: " + userName);
        Response<MessageResponse> response = new Response<>();

        if (userName == null) {
            log.warn("Cannot save message because user name is null");
            throw new BotException(BotExceptionType.INVALID_REQUEST_DATA, "Username cannot be empty");
        }

        if (content == null || content.isEmpty()) {
            log.warn("Cannot save message because content is null or empty");
            throw new BotException(BotExceptionType.INVALID_REQUEST_DATA, "Content cannot be empty");
        }

        User user = userRepository.findByUsername(userName);

        if (user == null) {
            log.warn("Cannot save message because user not found" + user);
            throw new BotException(BotExceptionType.USER_NOT_FOUND, "User not found");
        }

        Message message = Message.builder()
                .user(user)
                .content(content)
                .type(MessageType.valueOf(type.toUpperCase()))
                .direction(Direct.valueOf(direction.toUpperCase()))
                .createdAt(LocalDateTime.now())
                .build();

        message = messageRepository.save(message);
        log.info("Message saved to user: " + userName);

        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setId(message.getId());
        messageResponse.setContent(message.getContent());
        messageResponse.setType(message.getType().name());
        messageResponse.setDirection(message.getDirection().name());
        messageResponse.setCreatedAt(LocalDateTime.now());

        response.setData(messageResponse);

        return response;
    }

    @Override
    public Response<List<MessageResponse>> getMessagesByUser(String userName) {
        Response<List<MessageResponse>> response = new Response<>();

        if (userName == null) {
            throw new BotException(BotExceptionType.USER_NOT_FOUND, "User not found");
        }

        User user = userRepository.findByUsername(userName);

        if (user == null) {
            throw new BotException(BotExceptionType.INVALID_REQUEST_DATA, "Username not found");
        }

        List<Message> messageList = user.getMessages();

        if (messageList == null || messageList.isEmpty()) {
            throw new BotException(BotExceptionType.INVALID_REQUEST_DATA, "No messages found");
        }

        List<MessageResponse> responseList = messageList.stream().map(message -> {

            MessageResponse messageResponse = new MessageResponse();
            messageResponse.setId(message.getId());
            messageResponse.setContent(message.getContent());
            messageResponse.setType(message.getType().name());
            messageResponse.setDirection(message.getDirection().name());
            messageResponse.setCreatedAt(message.getCreatedAt());

            return messageResponse;


        }).toList();


        response.setData(responseList);


        return response;
    }

    @Override
    public void sendOtp(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new BotException(BotExceptionType.USER_NOT_FOUND, "User not found"));

        if (user.getLoginToken() == null) {
            throw new BotException(BotExceptionType.INVALID_REQUEST_DATA, "OTP not generated");
        }

        String message = "Your login OTP code : " + user.getLoginToken() +
                "\nThis code will expire in 3 minutes.";



    }
}
