package com.spardarus.bot.bot;

import com.spardarus.bot.config.Properties;
import com.spardarus.bot.service.Commands;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.spardarus.bot.config.Proxy.buildBotOptions;

public class TrafficBot extends TelegramLongPollingBot {

    public TrafficBot() {
        super(buildBotOptions());
    }

    public void onUpdateReceived(Update update) {
        new Commands().start(update);
    }

    public String getBotUsername() {
        return Properties.getProperty("BotUsername");
    }

    public String getBotToken() {
        return Properties.getProperty("BotToken");
    }
}
