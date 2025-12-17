package com.example.tgbot.service;

import com.example.tgbot.entity.User;
import com.example.tgbot.entity.UserCarSelection;
import com.example.tgbot.response.Response;
import com.example.tgbot.session.UserSession;

public interface UserCarSelectionService {

   Response<UserCarSelection> saveCarSelectionToDb(User user , UserSession userSession);


}
