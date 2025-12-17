package com.example.tgbot.token;

import java.util.Random;

public class TokenUtil {

    public static String generatedToken() {


        Random random = new Random();
        int token = 100000 + random.nextInt(900000);

        return String.valueOf(token);


    }


}
