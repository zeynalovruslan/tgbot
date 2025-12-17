package com.example.tgbot.service;

import com.example.tgbot.dto.response.CarAddDto;
import com.example.tgbot.session.UserSession;

import java.util.List;

public interface TurboAzSearchService {

    List<CarAddDto> searchCars(UserSession userSession);

    String buildSearchUrl (UserSession userSession);
}
