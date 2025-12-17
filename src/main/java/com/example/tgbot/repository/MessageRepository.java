package com.example.tgbot.repository;

import com.example.tgbot.dto.response.UserResponse;
import com.example.tgbot.entity.User;
import com.example.tgbot.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByUser(User user);

}
