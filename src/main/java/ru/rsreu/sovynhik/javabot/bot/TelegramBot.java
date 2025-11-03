package ru.rsreu.sovynhik.javabot.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import ru.rsreu.sovynhik.javabot.config.BotConfig;

@Component
public class TelegramBot implements SpringLongPollingBot {

    private final BotConfig botConfig;
    private final UpdateConsumer updateConsumer;

    public TelegramBot(BotConfig botConfig, UpdateConsumer updateConsumer) {
        this.botConfig = botConfig;
        this.updateConsumer = updateConsumer;
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateConsumer;
    }
}