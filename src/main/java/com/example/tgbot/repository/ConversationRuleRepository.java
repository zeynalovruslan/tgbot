package com.example.tgbot.repository;

import com.example.tgbot.entity.ConversationRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRuleRepository extends JpaRepository<ConversationRule, Long> {
    List<ConversationRule> findByActiveTrueOrderByPriorityDesc();

}
