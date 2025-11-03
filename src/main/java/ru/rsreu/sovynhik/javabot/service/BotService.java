package ru.rsreu.sovynhik.javabot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class BotService {

    private final TelegramClient telegramClient;

    public void sendMessage(Long chatId, String text) {
        try {
            telegramClient.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageWithKeyboard(Long chatId, String text, InlineKeyboardMarkup keyboard) {
        try {
            telegramClient.execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(keyboard)
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendRandomImage(Long chatId) {
        new Thread(() -> {
            try {
                URL url = new URL("https://picsum.photos/200");
                var inputStream = url.openStream();

                telegramClient.execute(SendPhoto.builder()
                        .chatId(chatId)
                        .photo(new InputFile(inputStream, "random.jpg"))
                        .caption("Here's your random image!")
                        .build());
            } catch (IOException | TelegramApiException e) {
                sendMessage(chatId, "Failed to load image 😢");
            }
        }).start();
    }

    public void sendRandomNumber(Long chatId) {
        int random = ThreadLocalRandom.current().nextInt(1000);
        sendMessage(chatId, "Your random number: " + random);
    }

    public void sendUserInfo(Long chatId, User user) {
        sendMessage(chatId, "👋 Hi, %s %s (@%s)!"
                .formatted(user.getFirstName(), user.getLastName(), user.getUserName()));
    }
}