package com.spardarus.config;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.telegram.telegrambots.bots.DefaultBotOptions;

public class Proxy {
    private static final String proxyEnable=Properties.getProperty("ProxyEnable");
    private static final String proxyHost = Properties.getProperty("proxyHost");
    private static final int proxyPort = Integer.parseInt(Properties.getProperty("proxyPort"));

    private static HttpHost getProxyHttpHost() {
        return new HttpHost(proxyHost, proxyPort);
    }
    public static HttpClient getHttpClient() {
        HttpClient client;
        if (proxyEnable.equals("true")) {
            client = HttpClientBuilder.create().setProxy(Proxy.getProxyHttpHost()).build();
        } else {
            client = HttpClientBuilder.create().build();
        }
        return client;
    }
    public static DefaultBotOptions buildBotOptions() {
        DefaultBotOptions options = new DefaultBotOptions();
        RequestConfig config;
        if (proxyEnable.equals("true")) {
            config = RequestConfig.custom().setProxy(getProxyHttpHost()).build();
        } else {
            config = RequestConfig.custom().build();
        }
        options.setRequestConfig(config);
        return options;
    }
}
