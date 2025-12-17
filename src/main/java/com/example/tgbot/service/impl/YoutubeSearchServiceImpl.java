package com.example.tgbot.service.impl;

import com.example.tgbot.dto.response.YoutubeAddToDto;
import com.example.tgbot.service.YoutubeSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class YoutubeSearchServiceImpl implements YoutubeSearchService {

    private final RestTemplate restTemplate;

    @Value("${youtube.api.key}")
    private String apiKey;


    @Override
    public List<YoutubeAddToDto> searchYoutube(String query) {

        try {
            String url = "https://www.googleapis.com/youtube/v3/search" +
                    "?part=snippet" +
                    "&type=video" +
                    "&maxResults=10" +
                    "&q=" + URLEncoder.encode(query, StandardCharsets.UTF_8) +
                    "&key=" + apiKey;

            Map response = restTemplate.getForObject(url, Map.class);

            List<Map> items = (List<Map>) response.get("items");
            List<YoutubeAddToDto> results = new ArrayList<>();

            for (Map item : items) {
                Map id = (Map) item.get("id");
                Map snippet = (Map) item.get("snippet");

                String videoId = (String) id.get("videoId");
                String title = (String) snippet.get("title");
                String videoUrl = "https://www.youtube.com/watch?v=" + videoId;

                results.add(new YoutubeAddToDto(title, videoUrl));
            }

            return results;

        } catch (Exception e) {
            throw new RuntimeException("YouTube search failed", e);
        }
    }



}



