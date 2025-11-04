package ru.rsreu.sovynhik.froggybot.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.rsreu.sovynhik.froggybot.service.BotService;
import ru.rsreu.sovynhik.froggybot.util.KeyboardFactory;

@Component
@RequiredArgsConstructor
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final BotService botService;

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            var message = update.getMessage();
            var chatId = message.getChatId();
            var text = message.getText();

            switch (text) {
                case "/start" -> botService.sendMessageWithKeyboard(chatId, "Welcome! Choose an option:", KeyboardFactory.mainMenu());
                case "Image" -> botService.sendRandomImage(chatId);
                case "Hello" -> botService.sendUserInfo(chatId, message.getFrom());
                default -> botService.sendMessage(chatId, "Unknown command 🤔");
            }
        } else if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        }
    }


    private void handleCallback(CallbackQuery query) {
        var chatId = query.getFrom().getId();
        switch (query.getData()) {
            case "random" -> botService.sendRandomNumber(chatId);
            case "image" -> botService.sendRandomImage(chatId);
            default -> botService.sendMessage(chatId, "Unknown button");
        }
    }
}