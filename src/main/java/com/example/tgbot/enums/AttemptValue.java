package com.example.tgbot.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum AttemptValue {
    ZeroAttempt(0) ,
    ThreeAttempts (3);

    public int value;

}
