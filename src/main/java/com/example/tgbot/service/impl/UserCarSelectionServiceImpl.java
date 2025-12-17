package com.example.tgbot.service.impl;

import com.example.tgbot.entity.User;
import com.example.tgbot.entity.UserCarSelection;
import com.example.tgbot.exception.BotException;
import com.example.tgbot.exception.BotExceptionType;
import com.example.tgbot.repository.UserCarSelectionRepository;
import com.example.tgbot.response.Response;
import com.example.tgbot.service.UserCarSelectionService;
import com.example.tgbot.session.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserCarSelectionServiceImpl implements UserCarSelectionService {
    private final UserCarSelectionRepository userCarSelectionRepository;


    @Override
    public Response<UserCarSelection> saveCarSelectionToDb(User user, UserSession userSession) {

        Response<UserCarSelection> response = new Response<>();

        userCarSelectionRepository.findAllByUserUsername(user.getUsername());

        if (user.getUsername() == null || userSession == null) {
            throw new BotException(BotExceptionType.INVALID_REQUEST_DATA, "user or user session is null");
        }
        UserCarSelection userCarSelection = new UserCarSelection();
        userCarSelection.setUser(user);
        userCarSelection.setBrand(userSession.getSelectedCarBrand());
        userCarSelection.setModel(userSession.getSelectedCarModel());
        userCarSelection.setYear(userSession.getSelectedCarYear());
        userCarSelection.setEngine(userSession.getSelectedCarEngine());
        userCarSelection.setMinPrice(userSession.getSelectedCarMinPrice());
        userCarSelection.setMaxPrice(userSession.getSelectedCarMaxPrice());
        userCarSelection.setCreatedAt(LocalDateTime.now());

        userCarSelectionRepository.save(userCarSelection);
        response.setData(userCarSelection);

        return response;
    }



}
