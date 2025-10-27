package ru.rsreu.sovynhik.javabot;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

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
        System.out.printf(
                "Пришло сообщение %s от %s%n",
                update.getMessage().getText(),
                update.getMessage().getChatId()
        );

        var chatID = update.getMessage().getChatId();
        SendMessage message = SendMessage.builder()
                .text("Hell! Your message: " + update.getMessage().getText())
                .chatId(chatID)
                .build();

        telegramClient.execute(message);
    }
}