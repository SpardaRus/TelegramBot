package com.spardarus;

import com.spardarus.service.yandex.YandexAPI;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestTranslate {
    private static YandexAPI yandexAPI;
    @BeforeClass
    public static void setYandexAPI() {
       yandexAPI = new YandexAPI();
    }

    @Test
    public void translateTest(){
        String text=yandexAPI.getTranslateText("Hello");
        String excpected="Привет";
        assertEquals(text,excpected);
    }
    @Test
    public void detectRuTest(){
        String text = yandexAPI.detectLang("Привет");
        String excpected="ru";
        assertEquals(text,excpected);
    }
    @Test
    public void detectEnTest(){
        String text = yandexAPI.detectLang("Hello");
        String excpected="en";
        assertEquals(text,excpected);
    }
}
