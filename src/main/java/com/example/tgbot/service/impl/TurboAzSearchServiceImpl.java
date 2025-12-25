package com.example.tgbot.service.impl;

import com.example.tgbot.dto.response.CarAddDto;
import com.example.tgbot.service.TurboAzSearchService;
import com.example.tgbot.session.UserSession;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TurboAzSearchServiceImpl implements TurboAzSearchService {


    private static final Map<String, String> makeMap = Map.of(
            "BMW", "3",
            "Mercedes", "4",
            "KIA", "8",
            "Hyundai", "1"
    );

    private static final Map<String, Map<String, String>> modelMap = Map.of(
            "BMW", Map.of("F30", "group2", "F10", "group3", "X5", "60"),
            "Mercedes", Map.of("C-Class", "group6", "E-Class", "group7", "S-Class", "group21"),
            "KIA", Map.of("Sportage", "121", "Optima", "119", "Sorento", "122"),
            "Hyundai", Map.of("Elantra", "103", "Tucson", "4", "Sonata", "99")
    );


    @Override
    public List<CarAddDto> searchCars(UserSession session) {

        try {
            String url = buildSearchUrl(session);

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10_000)
                    .get();

            Elements ads = doc.select(".products-i"); 

            List<CarAddDto> result = new ArrayList<>();

            for (Element ad : ads) {

                String title = ad.select(".products-i__name").text();
                String price = ad.select(".products-i__price").text();

                String imageUrl = ad.select("img").attr("data-src");
                if (imageUrl.isEmpty()) {
                    imageUrl = ad.select("img").attr("src");
                }

                String adUrl = "https://turbo.az"
                        + ad.select("a").attr("href");

                result.add(new CarAddDto(title, price, imageUrl, adUrl));

                if (result.size() == 10) break;
            }

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Turbo.az scraping failed", e);
        }
    }


    @Override
    public String buildSearchUrl(UserSession userSession) {
        String brand = userSession.getSelectedCarBrand();
        String model = userSession.getSelectedCarModel();

        String makeId = makeMap.getOrDefault(brand, "");
        String modelId = modelMap.getOrDefault(brand, Map.of()).getOrDefault(model, "");

        StringBuilder url = new StringBuilder("https://turbo.az/autos?");

        if (!makeId.isEmpty()) {
            url.append("q[make][]=").append(makeId).append("&");
        }
        if (!modelId.isEmpty()) {
            url.append("q[model][]=").append(modelId).append("&");
        }
        if (userSession.getSelectedCarYear() != null) {
            url.append("q[year_from]=").append(userSession.getSelectedCarYear()).append("&");
        }
        if (userSession.getSelectedCarEngine() != null) {
            url.append("q[engine_volume_from]=").append(userSession.getSelectedCarEngine()).append("&");
        }
        if (userSession.getSelectedCarMinPrice() != null) {
            url.append("q[price_from]=").append(userSession.getSelectedCarMinPrice()).append("&");
        }
        if (userSession.getSelectedCarMaxPrice() != null) {
            url.append("q[price_to]=").append(userSession.getSelectedCarMaxPrice()).append("&");
        }

        url.append("q[currency]=azn");

        return url.toString();
    }

}
