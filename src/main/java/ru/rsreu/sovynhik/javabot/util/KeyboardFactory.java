package ru.rsreu.sovynhik.javabot.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.List;

public class KeyboardFactory {

    public static InlineKeyboardMarkup mainMenu() {
        var random = InlineKeyboardButton.builder().text("🎲 Random").callbackData("random").build();
        var image = InlineKeyboardButton.builder().text("🖼️ Image").callbackData("image").build();

        return new InlineKeyboardMarkup(List.of(
                new InlineKeyboardRow(random),
                new InlineKeyboardRow(image)
        ));
    }
}