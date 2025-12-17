package com.example.tgbot.session;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    private final ConcurrentHashMap<String, UserSession> sessions = new ConcurrentHashMap<>();

    public UserSession getUserSession(String chatId) {
        return sessions.computeIfAbsent(chatId, c -> new UserSession());
    }

    public void clear(String chatId) {
        sessions.remove(chatId);
    }
}
