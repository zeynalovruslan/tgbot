package com.example.tgbot.telegram;

import com.example.tgbot.dto.response.YoutubeAddToDto;
import com.example.tgbot.service.YoutubeSearchService;
import com.example.tgbot.session.SessionManager;
import com.example.tgbot.session.UserSession;
import com.example.tgbot.utility.AuthFlowUtil;
import com.example.tgbot.utility.CarFlowUtil;
import com.example.tgbot.utility.TelegramUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class MyTelegramBot extends TelegramLongPollingBot {

    private final YoutubeSearchService youtubeSearch;
    private final TelegramUtil telegramUtil;
    private final AuthFlowUtil authFlowUtil;
    private final SessionManager sessionManager;
    private final CarFlowUtil carFlowUtil;
    @Value("${telegram.bot.username}")
    private String botUserName;
    @Value("${telegram.bot.token}")
    private String botToken;

    public MyTelegramBot(YoutubeSearchService youtubeSearch, TelegramUtil telegramUtil, AuthFlowUtil authFlowUtil, SessionManager sessionManager, CarFlowUtil carFlowUtil) {
        this.youtubeSearch = youtubeSearch;
        this.telegramUtil = telegramUtil;

        this.authFlowUtil = authFlowUtil;
        this.sessionManager = sessionManager;
        this.carFlowUtil = carFlowUtil;
    }


    @Override
    public String getBotUsername() {
        return botUserName;
    }


    @Override
    public String getBotToken() {
        return botToken;
    }


    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            String messageText = update.getMessage().getText();
            String chatId = update.getMessage().getChatId().toString();
            UserSession userSession = sessionManager.getUserSession(chatId);


            if (messageText.equals("/reset")) {
                sessionManager.clear(chatId);
                return;
            }

            if (messageText.equals("/register")) {
                sessionManager.clear(chatId);
                authFlowUtil.startRegister(chatId);
                return;
            }


            if (userSession.getStep() != null && userSession.getStep().startsWith("register_")) {
                authFlowUtil.handleRegisterFlow(chatId, messageText, userSession);
                return;
            }

            if (messageText.equals("/car")) {
                if (userSession.getUsername() == null) {
                    telegramUtil.send(chatId, "Please login first . ");
                    return;
                }
                carFlowUtil.startCarFlow(chatId, userSession);
                return;
            }
            if (userSession.getStep() != null && userSession.getStep().startsWith("car_")) {
                carFlowUtil.handleCarFlow(chatId, messageText, userSession);
                return;
            }


            if (messageText.startsWith("/youtube ")) {
                String query = messageText.replace("/youtube ", "").trim();

                if (query.isEmpty()) {
                    telegramUtil.send(chatId, "Please provide a search term. Example:\n/youtube java tutorial");
                    return;
                }
                List<YoutubeAddToDto> videos = youtubeSearch.searchYoutube(query);

                if (videos.isEmpty()) {
                    telegramUtil.send(chatId, "No videos found for: " + query);
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (YoutubeAddToDto video : videos) {
                        sb.append(video.getTitle())
                                .append("\n")
                                .append(video.getVideoId())
                                .append("\n\n");
                    }
                    telegramUtil.send(chatId, sb.toString());
                }
                return;
            }

            if ("youtube_query".equals(userSession.getStep())) {
                String query = messageText.trim();
                if (!query.isEmpty()) {
                    List<YoutubeAddToDto> videos = youtubeSearch.searchYoutube(query);
                    if (videos.isEmpty()) {
                        telegramUtil.send(chatId, "No videos found for: " + query);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (YoutubeAddToDto video : videos) {
                            sb.append(video.getTitle())
                                    .append("\n")
                                    .append(video.getVideoId())
                                    .append("\n\n");
                        }
                        telegramUtil.send(chatId, sb.toString());
                    }
                } else {
                    telegramUtil.send(chatId, "Query cannot be empty. Try again:");
                    return;
                }
                userSession.setStep("post_login_choice");
                return;}


                if (messageText.equals("/login")) {
                    sessionManager.clear(chatId);
                    authFlowUtil.startLogin(chatId);
                    return;
                }


                if (userSession.getStep() != null && userSession.getStep().startsWith("login_")) {
                    authFlowUtil.handleLoginFlow(chatId, messageText, userSession);
                    return;
                }

                if ("post_login_choice".equals(userSession.getStep())) {
                    if ("1".equals(messageText)) {
                        userSession.setStep("chatting");
                    } else if ("2".equals(messageText)) {
                        carFlowUtil.startCarFlow(chatId, userSession);

                    } else if ("3".equals(messageText)) {
                        userSession.setStep("youtube_query");
                        telegramUtil.send(chatId, "Please enter your YouTube search query:");

                    } else {
                        telegramUtil.send(chatId, "Wrong choice , please select 1 , 2 or 3 ");
                    }
                    return;
                }

                if (userSession.getStep() != null && userSession.getStep().equals("chatting")) {
                    authFlowUtil.handleSimpleMessage(chatId, messageText);
                    return;
                }


                authFlowUtil.handleSimpleMessage(chatId, messageText);


            }


        }


    }








