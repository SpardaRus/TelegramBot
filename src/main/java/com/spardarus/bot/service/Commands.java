package com.spardarus.bot.service;

import com.spardarus.bot.bot.ToolBot;
import com.spardarus.bot.dbo.Weather;
import com.spardarus.bot.service.yandex.YandexAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class Commands {
    private String idAndContact=" ";
    private static final Logger log = LoggerFactory.getLogger("com.spardarus.bot");
    private static Map<String,String> commands=new HashMap<>();
    static{
        commands.put("1","☀Погода");
        commands.put("2","\uD83D\uDE91Помощь");
        commands.put("3","Введите текст для перевода");
    }

    public void start(Update update){
        if(update.hasMessage()&& update.getMessage().hasText()) {
            SendMessage message = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setReplyMarkup(getKeyboard());
            idAndContact="id:"+message.getChatId()+", contact: "+update.getMessage().getChat().getFirstName()+", ";
            String text=update.getMessage().getText();
            if(text.equals(commands.get("1"))){
                message.setText(getCurrentTemperature());
            }else{
                if(text.equals(commands.get("2"))) {
                    message.setText(getListCommand());
                }else{
                    message.setText(getTranslateMessage(update.getMessage().getText()));
                }
            }
            try {
                if(message.getText() !=null)
                new ToolBot().execute(message);
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
        row.add(commands.get("1"));
        row.add(commands.get("2"));
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private String getListCommand() {
        log.debug(""+idAndContact+"getListCommand()");
        return "Список команд:\n"+commands;
    }

    private String getCurrentTemperature() {
        log.debug(""+idAndContact+"getCurrentTemperature()");
        Weather weather=new YandexAPI().getWeather();
        return "Погода в саратове сегодня \uD83D\uDD2E\n" +
                "Температура: "+weather.getTemp()+"℃"+
                "\nТемпература по ощущению: "+weather.getFeelsLike()+"℃"+
                "\nСейчас погода: "+weather.getCondition();
    }
    private String getTranslateMessage(String text){
        log.debug(""+idAndContact+"getTranslateMessage()");
        return ""+new YandexAPI().getTranslateText(text);
    }
}
