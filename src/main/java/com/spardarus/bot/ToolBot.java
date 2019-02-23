package com.spardarus.bot;

import com.spardarus.config.Properties;
import com.spardarus.service.Commands;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.spardarus.config.Proxy.buildBotOptions;

public class ToolBot extends TelegramLongPollingBot {

    public ToolBot() {
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
