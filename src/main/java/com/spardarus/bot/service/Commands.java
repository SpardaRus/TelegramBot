package com.spardarus.bot.service;

import com.spardarus.bot.service.yandex.Weather;
import com.spardarus.bot.service.yandex.YandexAPI;

import java.util.ArrayList;
import java.util.List;

public class Commands {
    private static List<String> commands=new ArrayList<>();
    static{
        commands.add("☀Погода");
        commands.add("\uD83D\uDE91Помощь");
        commands.add("Введите текст для перевода");
    }

    public static String getListCommand() {
        System.out.println("getListCommand() ");
        return "Список команд:\n"+commands;
    }

    public static String getCurrentTemperature() {
        System.out.println("getCurrentTemperature()");
        Weather weather=new YandexAPI().getWeather();
        return "Погода в саратове сегодня \uD83D\uDD2E\n" +
                "Температура: "+weather.getTemp()+"℃"+
                "\nТемпература по ощущению: "+weather.getFeelsLike()+"℃"+
                "\nСейчас погода: "+weather.getCondition()
                ;//+"\n"+weather.getIcon();
    }
    public static String getTranslateMessage(String text){
        System.out.println("getTranslateMessage()");
        return new YandexAPI().getTranslateText(text);
    }
}
