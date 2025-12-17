package com.example.tgbot.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarAddDto {

    private String title;
    private String price;
    private String imageUrl;
    private String adUrl;


}
