package com.example.tgbot.utility;

import com.example.tgbot.dto.request.UserLoginRequest;
import com.example.tgbot.dto.request.UserRegisterRequest;
import com.example.tgbot.exception.BotException;
import com.example.tgbot.service.ConversationService;
import com.example.tgbot.service.MessageService;
import com.example.tgbot.service.UserService;
import com.example.tgbot.session.SessionManager;
import com.example.tgbot.session.UserSession;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

@Component
public class AuthFlowUtil {


    private final SessionManager sessionManager;
    private final MessageService messageService;
    private final TelegramUtil telegramUtil;
    private final ConversationService conversationService;
    private final UserService userService;

    public AuthFlowUtil(SessionManager sessionManager, MessageService messageService, TelegramUtil telegramUtil, ConversationService conversationService, UserService userService) {
        this.sessionManager = sessionManager;
        this.messageService = messageService;
        this.telegramUtil = telegramUtil;
        this.conversationService = conversationService;
        this.userService = userService;
    }

    public void handleSimpleMessage(String chatId, String text) {
        UserSession userSession = sessionManager.getUserSession(chatId);
        String username = userSession.getUsername();

        try {
            if (username != null) {

                messageService.saveMessage(username, text, "TEXT", "INCOMING");


                String botReply = conversationService.reply(text);
                messageService.saveMessage(username, botReply, "TEXT", "OUTGOING");
                telegramUtil.send(chatId, botReply);
            } else {
                telegramUtil.send(chatId, "Please login or register to start chatting.");
            }
        } catch (BotException e) {
            telegramUtil.send(chatId, "Could not save message: " + e.getMessage());
        }
    }

    public void handleOtpVerification(String chatId, String otp) {
        UserSession userSession = sessionManager.getUserSession(chatId);

        if (userSession == null || userSession.getUsername() == null) {
            telegramUtil.send(chatId, "Session expired. Please /login again.");
            sessionManager.clear(chatId);
            return;
        }

        try {


            var response = userService.verifyLoginToken(userSession.getUsername(), otp, chatId);
            userSession.setOtpAttempts(0);
            userSession.setStep("post_login_choice");
            telegramUtil.send(chatId, "OTP verified! Welcome, " + response.getData().getName() + " " + response.getData().getSurname());

            telegramUtil.send(chatId, "What do you want to do?\n" +
                    "1. Chat\n" +
                    "2. Car selection\n"+
                    "3. Youtube");

        } catch (BotException e) {

            userSession.setOtpAttempts(userSession.getOtpAttempts() + 1);
            String msg = e.getMessage().toLowerCase();
            if (msg.contains("expired")) {
                telegramUtil.send(chatId, "OTP is expired. Please try again.");
                sessionManager.clear(chatId);
                return;
            }


            if (userSession.getOtpAttempts() >= UserSession.MAX_OTP_ATTEMPTS) {
                telegramUtil.send(chatId, "You entered the wrong OTP 3 times. Please try again. ");
                sessionManager.clear(chatId);
            } else {
                telegramUtil.send(chatId, "OTP is wrong . Please try again (" + userSession.getOtpAttempts() + "/" + UserSession.MAX_OTP_ATTEMPTS + ")");
                userSession.setStep("login_otp");
            }


        }
    }

    public void handleRegisterFlow(String chatId, String messageText, UserSession userSession) {
        switch (userSession.getStep()) {
            case "register_name":
                userSession.setName(messageText);
                userSession.setStep("register_surname");
                telegramUtil.send(chatId, "Please enter your surname : ");
                break;

            case "register_surname":
                userSession.setSurname(messageText);
                userSession.setStep("register_age");
                telegramUtil.send(chatId, "Please enter your age : ");
                break;

            case "register_age":
                try {
                    userSession.setAge(Integer.parseInt(messageText));
                } catch (Exception e) {
                    telegramUtil.send(chatId, "Please enter your age correctly (it must be a number) ");
                    return;
                }
                userSession.setStep("register_username");
                telegramUtil.send(chatId, "Please enter your username : ");
                break;

            case "register_username":
                userSession.setUsername(messageText);
                userSession.setStep("register_password");
                telegramUtil.send(chatId, "Please enter your password : ");
                break;
            case "register_password":
                userSession.setPassword(messageText);
                completeRegistration(chatId, userSession);
                break;
        }


    }


    public void handleLoginFlow(String chatId, String messageText, UserSession userSession) {
        switch (userSession.getStep()) {
            case "login_username":
                userSession.setUsername(messageText);
                userSession.setStep("login_password");
                telegramUtil.send(chatId, "Please enter your password : ");
                break;
            case "login_password":
                userSession.setPassword(messageText);
                completeLogin(chatId, userSession);
                break;
            case "login_otp":
                handleOtpVerification(chatId, messageText);
                break;
            default:
                telegramUtil.send(chatId, "Unexpected login state . Please enter /login again");
                sessionManager.clear(chatId);
        }
    }

    public void completeLogin(String chatId, UserSession userSession) {
        try {
            UserLoginRequest request = UserLoginRequest.builder().username(userSession.getUsername()).password(userSession.getPassword()).build();

            var resp = userService.login(request, chatId);
            String otp = resp.getData().getLoginToken();
            telegramUtil.send(chatId, "Your OTP code: " + otp + "\nThis code will expire in 3 minutes.");
            userSession.setStep("login_otp");
        } catch (BotException e) {
            telegramUtil.send(chatId, "Login failed: " + e.getMessage());
            sessionManager.clear(chatId);
        }
    }

    public void completeRegistration(String chatId, UserSession userSession) {
        try {
            UserRegisterRequest request = UserRegisterRequest.builder().name(userSession.getName()).surname(userSession.getSurname()).age(userSession.getAge()).username(userSession.getUsername()).password(userSession.getPassword()).build();

            var resp = userService.registration(request);
            telegramUtil.send(chatId, "Registration completed: \n" + "Please enter /login ");
            sessionManager.clear(chatId);
        } catch (BotException e) {
            telegramUtil.send(chatId, "Registration failed: " + e.getMessage());
        }
    }


    public void startRegister(String chatId) {
        UserSession userSession = sessionManager.getUserSession(chatId);
        userSession.setStep("register_name");
        telegramUtil.send(chatId, "Please enter your name : ");
    }

    public void startLogin(String chatId) {
        UserSession userSession = sessionManager.getUserSession(chatId);
        userSession.setStep("login_username");
        telegramUtil.send(chatId, "Please enter your username : ");
    }

}

