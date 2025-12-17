package com.example.tgbot.repository;

import com.example.tgbot.entity.UserCarSelection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCarSelectionRepository extends JpaRepository<UserCarSelection, Long> {
    List<UserCarSelection> findAllByUserUsername(String username);
}
