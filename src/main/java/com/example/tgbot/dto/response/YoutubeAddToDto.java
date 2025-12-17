package com.example.tgbot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class YoutubeAddToDto {
    private String videoId;
    private String title;


    public YoutubeAddToDto(String title, String videoUrl) {
        this.title = title;
        this.videoId = videoUrl;
    }
}
