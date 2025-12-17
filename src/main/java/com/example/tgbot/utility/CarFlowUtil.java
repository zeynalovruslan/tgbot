package com.example.tgbot.utility;

import com.example.tgbot.dto.response.CarAddDto;
import com.example.tgbot.entity.User;
import com.example.tgbot.service.TurboAzSearchService;
import com.example.tgbot.service.UserCarSelectionService;
import com.example.tgbot.service.UserService;
import com.example.tgbot.session.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CarFlowUtil {

    private static final Map<String, List<String>> brandModels = Map.of(
            "BMW", List.of("F30", "F10", "X5"),
            "Mercedes", List.of("C-Class", "E-Class", "S-Class"),
            "KIA", List.of("Sportage", "Optima", "Sorento"),
            "Hyundai", List.of("Elantra", "Tucson", "Sonata")
    );
    private final TurboAzSearchService turboAzSearchService;
    private final UserCarSelectionService userCarSelectionService;
    private final TelegramUtil telegramUtil;
    private final UserService userService;


    public void startCarFlow(String chatId, UserSession userSession) {
        userSession.setStep("car_brand");
        telegramUtil.send(chatId, "Please enter your brand :\n" + "1. BMW\n" + "2. Mercedes\n" + "3. KIA\n" + "4. Hyundai");


    }

    public void handleCarFlow(String chatId, String messageText, UserSession userSession) {
        switch (userSession.getStep()) {
            case "car_brand":
                handleCarBrand(chatId, messageText, userSession);
                break;
            case "car_model":
                handleCarModel(chatId, messageText, userSession);
                break;
            case "car_year":
                handleCarYear(chatId, messageText, userSession);
                break;
            case "car_engine":
                handleCarEngine(chatId, messageText, userSession);
                break;
            case "car_maxPrice":
                handleCarPrice(chatId, messageText, userSession);
                break;
            case "car_confirm":
                handleCarConfirm(chatId, messageText, userSession);
                break;

            default:
                telegramUtil.send(chatId, "Please enter a valid car type.");
        }
    }


    public void handleCarBrand(String chatId, String messageText, UserSession userSession) {
        switch (messageText) {
            case "1":
                userSession.setSelectedCarBrand("BMW");
                break;
            case "2":
                userSession.setSelectedCarBrand("Mercedes");
                break;
            case "3":
                userSession.setSelectedCarBrand("KIA");
                break;
            case "4":
                userSession.setSelectedCarBrand("Hyundai");
                break;

            default:
                telegramUtil.send(chatId, "Wrong selection . Please select 1-4 ");
                return;

        }

        userSession.setStep("car_model");
        List<String> models = brandModels.get(userSession.getSelectedCarBrand());
        StringBuilder modelList = new StringBuilder();
        for (int i = 0; i < models.size(); i++) {
            modelList.append(i + 1).append(". ").append(models.get(i)).append("\n");
        }

        telegramUtil.send(chatId, "Select model for " + userSession.getSelectedCarBrand() + "\n" + modelList);


    }

    public void handleCarModel(String chatId, String messageText, UserSession userSession) {
        List<String> models = brandModels.get(userSession.getSelectedCarBrand());
        if (models == null) {
            telegramUtil.send(chatId, "Brand models not found : ");
            return;
        }
        int index;
        try {
            index = Integer.parseInt(messageText) - 1;
        } catch (NumberFormatException e) {
            telegramUtil.send(chatId, "Wrong selection . Please select number ");
            return;
        }

        if (index < 0 || index > models.size()) {
            telegramUtil.send(chatId, "Wrong selection, please select 1-  " + models.size());
            return;

        }

        userSession.setSelectedCarModel(models.get(index));
        userSession.setStep("car_year");
        telegramUtil.send(chatId, "Select year for " + userSession.getSelectedCarBrand() + "  " + userSession.getSelectedCarModel() + " (e.g., 2018):");


    }


    public void handleCarYear(String chatId, String messageText, UserSession userSession) {
        int year;

        try {

            year = Integer.parseInt(messageText);

        } catch (Exception e) {
            telegramUtil.send(chatId, "Enter right type (for example - 2015) ");
            return;
        }

        if (year < 2000 || year > 2025) {
            telegramUtil.send(chatId, "Enter valid year (1990-2025)");
            return;
        }
        userSession.setSelectedCarYear(year);
        userSession.setStep("car_engine");

        telegramUtil.send(chatId, "Which engine do you want? ");
    }

    public void handleCarEngine(String chatId, String messageText, UserSession userSession) {

        double engine;
        try {
            engine = Double.parseDouble(messageText.replace(",", "."));

        } catch (NumberFormatException e) {
            telegramUtil.send(chatId, "Please enter valid engine (for example - 2.0 , 2.4) ");
            return;
        }

        if (engine <= 1) {
            telegramUtil.send(chatId, "Engine cannot zero or negative ");
            return;
        }

        userSession.setSelectedCarEngine(String.valueOf(engine));
        userSession.setStep("car_maxPrice");
        telegramUtil.send(chatId, "Please enter your MIN price : ");
    }

    public void handleCarPrice(String chatId, String messageText, UserSession userSession) {
        int price;
        try {
            price = Integer.parseInt(messageText);
        } catch (NumberFormatException e) {
            telegramUtil.send(chatId, "Please enter the value in numbers. For example: 20000");
            return;
        }

        if (userSession.getSelectedCarMinPrice() == null) {
            if (price < 1000) {
                telegramUtil.send(chatId, "Min price must be greater than 1000");
                return;
            }

            userSession.setSelectedCarMinPrice(price);
            telegramUtil.send(chatId, "Please enter your MAX  price : ");
            return;

        }

        Integer minPrice = userSession.getSelectedCarMinPrice();

        if (price <= minPrice) {
            telegramUtil.send(chatId, "Max price must be greater than min price (" + minPrice + ")");
            return;
        }


        userSession.setSelectedCarMaxPrice(price);
        userSession.setStep("car_confirm");

        telegramUtil.send(chatId,
                "Your choices:\n" +
                        "Brand: " + userSession.getSelectedCarBrand() + "\n" +
                        "Model: " + userSession.getSelectedCarModel() + "\n" +
                        "Year: " + userSession.getSelectedCarYear() + "\n" +
                        "Engine: " + userSession.getSelectedCarEngine() + "\n" +
                        "Price: " + minPrice + " - " + userSession.getSelectedCarMaxPrice() + " AZN\n\n" +
                        "Do you confirm?\n1. Yes\n2. No"
        );
    }

    public void handleCarConfirm(String chatId, String messageText, UserSession userSession) {
        if ("1".equals(messageText)) {

            User user = userService.getUserByUsername(userSession.getUsername());
            userCarSelectionService.saveCarSelectionToDb(user, userSession);


            telegramUtil.send(chatId, " Please wait... Starting search  ");
            sendCarResults(userSession, chatId);
            userSession.setStep("chatting");
            return;
        }

        if ("2".equals(messageText)) {
            telegramUtil.send(chatId, "Your choice is canceled . You can try again with /car ");
            userSession.setStep("chatting");
            return;
        }
        telegramUtil.send(chatId, " Wrong selection . Please select 1 or 2");
    }

    private void sendCarResults(UserSession userSession, String chatId) {
        List<CarAddDto> cars = turboAzSearchService.searchCars(userSession);

        if (cars.isEmpty()) {
            telegramUtil.send(chatId, "No cars found for your selection.");
            return;
        }

        for (CarAddDto car : cars) {
            String caption =
                    car.getTitle() + "\n" +
                            car.getPrice() + "\n" +
                            car.getAdUrl();

            if (car.getImageUrl() != null && !car.getImageUrl().isBlank()) {
                telegramUtil.sendPhoto(
                        chatId,
                        car.getImageUrl(),
                        caption
                );

            } else {
                telegramUtil.send(chatId, "Image not found :\n " + caption);
            }
        }
    }

}
