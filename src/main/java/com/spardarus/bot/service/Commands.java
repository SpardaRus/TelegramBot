package com.spardarus.bot.service;

import com.spardarus.bot.bot.TrafficBot;
import com.spardarus.bot.dbo.Weather;
import com.spardarus.bot.service.yandex.YandexAPI;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class Commands {
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
            System.out.print("id:"+message.getChatId()+", contact: "+update.getMessage().getChat().getFirstName()+", ");
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
                new TrafficBot().execute(message);
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

    private static String getListCommand() {
        System.out.println("getListCommand() ");
        return "Список команд:\n"+commands;
    }

    private String getCurrentTemperature() {
        System.out.println("getCurrentTemperature()");
        Weather weather=new YandexAPI().getWeather();
        return "Погода в саратове сегодня \uD83D\uDD2E\n" +
                "Температура: "+weather.getTemp()+"℃"+
                "\nТемпература по ощущению: "+weather.getFeelsLike()+"℃"+
                "\nСейчас погода: "+weather.getCondition();
    }
    private String getTranslateMessage(String text){
        System.out.println("getTranslateMessage()");
        return ""+new YandexAPI().getTranslateText(text);
//                +
//                "\nПереведено сервисом «Яндекс.Переводчик»\n"+
//                "http://translate.yandex.ru/";
    }
}
