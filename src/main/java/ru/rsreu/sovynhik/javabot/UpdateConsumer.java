package ru.rsreu.sovynhik.javabot;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    public UpdateConsumer() {
        this.telegramClient = new OkHttpTelegramClient(
                "8464271312:AAHeP8VqcChMGcLoxNVZ6IQX3N7NoL_1AKk"
        );
    }

    @SneakyThrows
    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start")) {
                sendMainMenu(chatId);
            } else {
                sendMessage(chatId, "I don't understand you!");
            }
        } else if (update.hasCallbackQuery()) {
            handlerCallbackQuery(update.getCallbackQuery());
        }
    }

    private void handlerCallbackQuery(CallbackQuery callbackQuery) {
        var data = callbackQuery.getData();
        var chatId = callbackQuery.getFrom().getId();
        var user = callbackQuery.getFrom();
        switch (data) {
            case "my_name" -> sendMyName(chatId, user);
            case "random" -> sendRandomInt(chatId);
            case "long_download" -> sendImage(chatId);
            default -> sendMessage(chatId, "Unknown instruction");
        }
    }

    @SneakyThrows
    private void sendMessage(Long chatId, String messageText) {
        SendMessage message = SendMessage.builder()
                .text(messageText)
                .chatId(chatId)
                .build();

        telegramClient.execute(message);
    }

    private void sendImage(Long chatId) {
        sendMessage(chatId, "Start download image!");
        new Thread(() -> {
            var imageUrl = "https://picsum.photos/200";
            try {
                URL url = new URL(imageUrl);
                var inputStream = url.openStream();

                SendPhoto sendPhoto = SendPhoto.builder()
                        .chatId(chatId)
                        .photo(new InputFile(inputStream, "random.jpg"))
                        .caption("You're random picture:")
                        .build();

                telegramClient.execute(sendPhoto);

            } catch (TelegramApiException | IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void sendRandomInt(Long chatId) {
        var randomInt = ThreadLocalRandom.current().nextInt();
        sendMessage(chatId, "You're random number: " + randomInt);
    }

    private void sendMyName(Long chatId, User user) {
        var text = "Hi!\n\nYou're name: %s\nYou're nickname: @%s"
                .formatted(
                        user.getFirstName() + " " + user.getLastName(),
                        user.getUserName()
                );
        sendMessage(chatId, text);
    }

    @SneakyThrows
    private void sendMainMenu(Long chatId) {
        SendMessage message = SendMessage.builder()
                .text("Welcome to JavaBot! Chose what you want to do!")
                .chatId(chatId)
                .build();

        var buttonMyName = InlineKeyboardButton.builder()
                .text("What is my name?")
                .callbackData("my_name")
                .build();

        var buttonRandomInt = InlineKeyboardButton.builder()
                .text("Get random integer")
                .callbackData("random")
                .build();

        var buttonDrawLongDownload = InlineKeyboardButton.builder()
                .text("Long download...")
                .callbackData("long_download")
                .build();

        List<InlineKeyboardRow> keyboardRows = List.of(
                new InlineKeyboardRow(buttonMyName),
                new InlineKeyboardRow(buttonRandomInt),
                new InlineKeyboardRow(buttonDrawLongDownload)
        );

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(keyboardRows);

        message.setReplyMarkup(markup);

        telegramClient.execute(message);

    }
}