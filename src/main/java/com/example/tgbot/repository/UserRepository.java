package com.example.tgbot.repository;

import com.example.tgbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {


    User findByUsername(String username);

    boolean existsByUsername(String username);

    User findByLoginToken(String token);




    User findByUsernameAndLoginTokenAndTelegramChatId(
            String username,
            String loginToken,
            String telegramChatId
    );


}
