package ru.rsreu.sovynhik.javabot;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

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
                SendMessage message = SendMessage.builder()
                        .text("I don't understand the message.")
                        .chatId(chatId)
                        .build();

                telegramClient.execute(message);
            }

        }
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