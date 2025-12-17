package com.example.tgbot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "conversation_rule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pattern;

    @Column(nullable = false)
    private String response;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private int priority;
}
