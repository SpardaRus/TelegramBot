package com.spardarus.bot.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.telegram.telegrambots.bots.DefaultBotOptions;

public class Proxy {
    // https://hidemyna.me/ru/proxy-list/
      private static final String proxyHost="62.4.16.163";//62.4.16.163
      private static final int proxyPort=3128;

    public static HttpHost getProxyHttpHost(){
        return new HttpHost(proxyHost, proxyPort);
    }
    public static DefaultBotOptions buildBotOptions() {
        DefaultBotOptions options = new DefaultBotOptions();
        RequestConfig config = RequestConfig.custom().setProxy(getProxyHttpHost()).build();
        options.setRequestConfig(config);
        return options;
    }
}
