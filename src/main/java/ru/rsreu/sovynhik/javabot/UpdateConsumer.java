package ru.rsreu.sovynhik.javabot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    @Override
    public void consume(Update update) {
        System.out.printf(
                "Пришло сообщение %s от %s%n",
                update.getMessage().getText(),
                update.getMessage().getChatId()
        );

        var chatID = update.getMessage().getChatId();
        SendMessage message = SendMessage.builder()
                .text("Hello")
                .chatId(chatID)
                .build();

        telegramClient.execute(message);
    }
}