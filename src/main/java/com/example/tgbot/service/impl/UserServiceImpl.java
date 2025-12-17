package com.example.tgbot.service.impl;

import com.example.tgbot.dto.request.UserLoginRequest;
import com.example.tgbot.dto.request.UserRegisterRequest;
import com.example.tgbot.dto.response.UserResponse;
import com.example.tgbot.entity.User;
import com.example.tgbot.enums.TokenValue;
import com.example.tgbot.exception.BotException;
import com.example.tgbot.exception.BotExceptionType;
import com.example.tgbot.repository.UserRepository;
import com.example.tgbot.response.Response;
import com.example.tgbot.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Response<UserResponse> registration(UserRegisterRequest userRegisterRequest) {

        log.info("Registration attempt for username: " + userRegisterRequest.getUsername());
        Response<UserResponse> response = new Response<>();


        if (userRepository.existsByUsername(userRegisterRequest.getUsername())) {
            log.warn("Registration failed username: " + userRegisterRequest.getUsername());
            throw new BotException(BotExceptionType.USER_ALREADY_EXISTS, "User with username " + userRegisterRequest.getUsername() + " already exists");
        }

        User user = User.builder()
                .username(userRegisterRequest.getUsername())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .name(userRegisterRequest.getName())
                .surname(userRegisterRequest.getSurname())
                .age(userRegisterRequest.getAge())
                .createdDate(LocalDateTime.now())


                .build();

        user = userRepository.save(user);
        log.info("Registration successfully with id: " + user.getId());


        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .createdDate(user.getCreatedDate())

                .build();


        response.setData(userResponse);


        return response;
    }

    @Override
    public Response<UserResponse> login(UserLoginRequest userLoginRequest, String chatId) {

        Response<UserResponse> response = new Response<>();


        if (userLoginRequest.getUsername() == null || userLoginRequest.getPassword() == null) {
            throw new BotException(BotExceptionType.INVALID_REQUEST_DATA, "Username or password is incorrect");
        }

        User user = userRepository.findByUsername(userLoginRequest.getUsername());


        if (user == null) {
            throw new BotException(BotExceptionType.USER_NOT_FOUND, "User with username " + userLoginRequest.getUsername() + " not found");
        }

        if (!passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {

            throw new BotException(BotExceptionType.INVALID_REQUEST_DATA, "Incorrect password");

        }

        String token = String.valueOf((int) (Math.random() * 900000) + 100000);
        user.setLoginToken(token);
        user.setTokenExpireTime(LocalDateTime.now().plusMinutes(TokenValue.ThreeMinute.value));
        user.setTelegramChatId(chatId);
        user = userRepository.save(user);


        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .createdDate(user.getCreatedDate())
                .loginToken(user.getLoginToken())

                .build();


        response.setData(userResponse);


        return response;
    }


    public Response<UserResponse> verifyLoginToken(String username, String token, String chatId) {

        Response<UserResponse> response = new Response<>();

        if (token == null) {
            throw new BotException(BotExceptionType.INVALID_REQUEST_DATA, "Invalid token");
        }

        User user = userRepository.findByUsernameAndLoginTokenAndTelegramChatId(username, token, chatId);
        if (user == null) {
            throw new BotException(BotExceptionType.INVALID_REQUEST_DATA, "OTP is invalid");
        }

        if (user.getTokenExpireTime().isBefore(LocalDateTime.now())) {
            throw new BotException(BotExceptionType.INVALID_REQUEST_DATA, "Expired token");
        }

        user.setLoginToken("Token is Expired");
        user.setTokenExpireTime(null);
        user.setTelegramChatId(null);
        user = userRepository.save(user);


        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .createdDate(user.getCreatedDate())
                .loginToken(user.getLoginToken())

                .build();

        response.setData(userResponse);

        return response;


    }

    @Override
    public Response<UserResponse> findByUsername(String username) {

        Response<UserResponse> response = new Response<>();

        if (username == null || username.isEmpty()) {
            throw new BotException(BotExceptionType.INVALID_REQUEST_DATA, "Username is incorrect");
        }

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new BotException(BotExceptionType.USER_NOT_FOUND, "User with username " + username + " not found");
        }

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .age(user.getAge())
                .createdDate(user.getCreatedDate())
                .loginToken(user.getLoginToken())

                .build();

        response.setData(userResponse);

        return response;
    }

    @Override
    public User getUserByUsername(String username) throws BotException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new BotException(BotExceptionType.USER_NOT_FOUND, "User not found");
        }
        return user;
    }

}
