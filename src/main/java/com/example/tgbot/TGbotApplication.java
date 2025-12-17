package com.example.tgbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class TGbotApplication {

    @Autowired
    private TelegramLongPollingBot telegramLongPollingBot;

    public static void main(String[] args) {
        SpringApplication.run(TGbotApplication.class, args);
    }

    @Bean
    public CommandLineRunner schedulingRunner() {
        return args -> {
            try {
                final TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                telegramBotsApi.registerBot(telegramLongPollingBot);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        };
    }


}
