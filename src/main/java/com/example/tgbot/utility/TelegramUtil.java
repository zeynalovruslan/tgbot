package com.example.tgbot.utility;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TelegramUtil {

    private final TelegramBot bot;

    public void send(String chatId, String message) {
        SendMessage request = new SendMessage(chatId, message);
        SendResponse response = bot.execute(request);

        if (!response.isOk()) {
            System.out.printf("Failed to send message: " + response);

        }

    }

    public void sendPhoto(String chatId, String photoUrl, String caption) {
        SendPhoto sendPhoto = new SendPhoto(chatId, photoUrl).caption(caption);
        SendResponse response = bot.execute(sendPhoto);

        if (!response.isOk()) {
            System.out.printf("Failed to send photo:  " + response);
        }

    }


}







