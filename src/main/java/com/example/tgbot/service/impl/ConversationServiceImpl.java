package com.example.tgbot.service.impl;

import com.example.tgbot.service.ConversationRuleService;
import com.example.tgbot.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRuleService conversationRuleService;

    @Override
    public String reply(String message) {
        String response = conversationRuleService.findResponse(message);

        if (response!= null) {
            return response;
        }


        return "I don't understand you. Write differently. ";
    }
}
