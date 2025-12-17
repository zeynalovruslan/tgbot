package com.example.tgbot.session;

import lombok.Data;

@Data
public class UserSession {

    public static final int MAX_OTP_ATTEMPTS = 3;
    private String step;
    private String name;
    private String surname;
    private Integer age;
    private String username;
    private String password;
    private String selectedCarBrand;
    private String selectedCarModel;
    private Integer selectedCarYear;
    private String selectedCarEngine;
    private Integer selectedCarMinPrice;
    private Integer selectedCarMaxPrice;
    private int otpAttempts = 0;

}
