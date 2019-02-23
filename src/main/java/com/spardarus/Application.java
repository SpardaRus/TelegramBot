package com.spardarus;


import com.spardarus.bot.ToolBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Application {

    private static final Logger log = LoggerFactory.getLogger("Application");

    public static void main(String[] args) {
        log.debug("BOT start");
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            log.debug("RegisterBot Start");
            botsApi.registerBot(new ToolBot());
            log.debug("RegisterBot OK");
        } catch (TelegramApiException te) {
            log.error("RegisterBot FAIL");
            te.printStackTrace();
        }
        log.debug("BOT start OK");
    }
}
