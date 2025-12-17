package com.example.tgbot.service.impl;

import com.example.tgbot.entity.ConversationRule;
import com.example.tgbot.repository.ConversationRuleRepository;
import com.example.tgbot.service.ConversationRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationRuleServiceImpl implements ConversationRuleService {

    private final ConversationRuleRepository conversationRuleRepository;


    @Override
    public String findResponse(String message) {

        List<ConversationRule> rules = conversationRuleRepository.findByActiveTrueOrderByPriorityDesc();

        for (ConversationRule rule : rules) {
            if (message.toLowerCase().matches(rule.getPattern())) {
                return rule.getResponse();
            }

        }


        return "I dont understand you ,sorry :(  " ;
    }
}
