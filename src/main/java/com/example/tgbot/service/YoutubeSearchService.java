package com.example.tgbot.service;

import com.example.tgbot.dto.response.YoutubeAddToDto;

import java.util.List;

public interface YoutubeSearchService {

    List<YoutubeAddToDto> searchYoutube( String query);


}
