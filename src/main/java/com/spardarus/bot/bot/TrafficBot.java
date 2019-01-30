package com.spardarus.bot.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static com.spardarus.bot.config.Proxy.buildBotOptions;
import static com.spardarus.bot.service.Commands.*;

public class TrafficBot extends TelegramLongPollingBot {

    public TrafficBot(){
        super(buildBotOptions());
    }

    public void onUpdateReceived(Update update) {
        if(update.hasMessage()&& update.getMessage().hasText()) {
            SendMessage message = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setReplyMarkup(getKeyboard());
            System.out.print("id:"+message.getChatId()+" ");
            switch (update.getMessage().getText()) {
                case "☀Погода":
                    message.setText(getCurrentTemperature());
                    break;
                case "\uD83D\uDE91Помощь":
                    message.setText(getListCommand());
                    break;
                default:
                    message.setText(getTranslateMessage(update.getMessage().getText()));
                    break;
            }
            try {
                execute(message);
            } catch (TelegramApiException te) {
                te.printStackTrace();
            }
        }
    }

    private ReplyKeyboardMarkup getKeyboard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("☀Погода");
        row.add("\uD83D\uDE91Помощь");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public String getBotUsername() {
        return "SarTraffic_bot";
    }

    public String getBotToken() {
        return "723009987:AAHIzQeyRPQh2D-Qi1d3mED49Z8gBe7HVyQ";
    }
}
