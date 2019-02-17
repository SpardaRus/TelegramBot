package com.spardarus.bot.service.yandex;

import com.spardarus.bot.config.Properties;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.spardarus.bot.config.Proxy.getProxyHttpHost;

public class ConnectionBuilder {

    private List<NameValuePair> getWeatherParams() {
        List<NameValuePair> weatherParams = new ArrayList<>();
        weatherParams.add(new Parameter("lat", "51.533103"));
        weatherParams.add(new Parameter("lon", "46.034158"));
        weatherParams.add(new Parameter("lang", "\"ru_RU\""));
        weatherParams.add(new Parameter("limit", "1"));
        weatherParams.add(new Parameter("hours", "false"));
        weatherParams.add(new Parameter("extra", "false"));
        return weatherParams;
    }

    private Map<String, String> getWeatherHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Yandex-API-Key", Properties.getProperty("X-Yandex-API-Key"));
        return headers;
    }

    private Map<String, String> getTranslateHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept", "*/*");
        return headers;
    }

    private List<NameValuePair> getDetectTranslateParams() {
        List<NameValuePair> translateParams = new ArrayList<>();
        translateParams.add(new Parameter("key", Properties.getProperty("X-Yandex-API-Key-Translate")));
        translateParams.add(new Parameter("hint", "ru,en"));
        return translateParams;
    }

    private List<NameValuePair> getTranslateParamsWithLang(String currentLang) {
        String lang = "ru-en";
        if (!currentLang.equals("ru") && !currentLang.equals("")) {
            lang = currentLang + "-ru";
        }
        List<NameValuePair> translateParams = new ArrayList<>();
        translateParams.add(new Parameter("lang", lang));
        translateParams.add(new Parameter("key", Properties.getProperty("X-Yandex-API-Key-Translate")));
        return translateParams;
    }

    class Parameter implements NameValuePair {
        private String name;
        private String value;

        private Parameter(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    private URI getURI(String host, String path, List<NameValuePair> params) {
        URI uri = null;
        try {
            uri = new URIBuilder()
                    .setScheme("https")
                    .setHost(host)
                    .setPath(path)
                    .setParameters(params)
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private HttpResponse getHttpResponseGet(String host, String path, List<NameValuePair> params, Map<String, String> headers) {
        HttpResponse response = null;
        URI uri = getURI(host, path, params);
        HttpGet httpGet = new HttpGet(uri);
        for (String key : headers.keySet()) {
            httpGet.setHeader(key, headers.get(key));
        }
        HttpClient client = HttpClientBuilder.create().setProxy(getProxyHttpHost()).build();//proxy
        //  HttpClient client = HttpClientBuilder.create().build();//without proxy
        try {
            response = client.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private HttpResponse getHttpResponsePost(String host, String path, List<NameValuePair> params, Map<String, String> headers, String body) {
        HttpResponse response = null;
        URI uri = getURI(host, path, params);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new StringEntity("text=" + body, "utf-8"));
        for (String key : headers.keySet()) {
            httpPost.setHeader(key, headers.get(key));
        }
        HttpClient client = HttpClientBuilder.create().setProxy(getProxyHttpHost()).build();//proxy
        //  HttpClient client = HttpClientBuilder.create().build();//without proxy
        try {
            response = client.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    protected HttpResponse getHttpResponseTranslate(String text) {
        return getHttpResponsePost(
                "translate.yandex.net",
                "/api/v1.5/tr.json/translate",
                getTranslateParamsWithLang(new YandexAPI().detectLang(text)),
                getTranslateHeaders(),
                text);
    }

    protected HttpResponse getHttpResponseDetect(String text) {
        return getHttpResponsePost(
                "translate.yandex.net",
                "/api/v1.5/tr.json/detect",
                getDetectTranslateParams(),
                getTranslateHeaders(),
                text);
    }

    protected HttpResponse getHttpResponseWeather() {
        return getHttpResponseGet(
                "api.weather.yandex.ru/v1",
                "/forecast",
                getWeatherParams(),
                getWeatherHeaders());
    }
}
