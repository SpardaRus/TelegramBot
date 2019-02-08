package com.spardarus.bot.service.yandex;


import com.spardarus.bot.config.Properties;
import com.spardarus.bot.dbo.Weather;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.spardarus.bot.config.Proxy.getProxyHttpHost;

public class YandexAPI {

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

    private HttpResponse getHttpResponseGet(String host, String path, List<NameValuePair> params, Map<String,String> headers) {
        HttpResponse response=null;
        URI uri = getURI(host, path, params);
        HttpGet httpGet = new HttpGet(uri);
        for(String key:headers.keySet()){
            httpGet.setHeader(key,headers.get(key));
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

    private HttpResponse getHttpResponsePost(String host, String path, List<NameValuePair> params, Map<String,String> headers,String body) {
        HttpResponse response=null;
        URI uri = getURI(host, path, params);
        HttpPost httpPost = new HttpPost(uri);
        try {
            httpPost.setEntity(new StringEntity("text=" + body));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for(String key:headers.keySet()){
            httpPost.setHeader(key,headers.get(key));
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

    public Weather getWeather() {
        try {
            Map<String,String> headers=new HashMap<>();
            headers.put("X-Yandex-API-Key",Properties.getProperty("X-Yandex-API-Key"));
            HttpResponse response = getHttpResponseGet("api.weather.yandex.ru/v1", "/forecast", getWeatherParams(),headers);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    Weather weather = new Weather();
                    String jsonString = EntityUtils.toString(entity);
                    JSONObject jsonResponse = new JSONObject(jsonString); //Convert String to JSON Object
                    JSONObject fact = jsonResponse.getJSONObject("fact");
                    weather.setTemp(fact.get("temp").toString());
                    weather.setFeelsLike(fact.get("feels_like").toString());
                    weather.setCondition(fact.getString("condition"));
                    weather.setIcon("https://yastatic.net/weather/i/icons/blueye/color/svg/" +
                            fact.getString("icon") +
                            ".svg");
                    return weather;
                } else {
                    System.out.println("Response entity is null");
                }
            } else {
                System.out.println("Status api is not 200");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ERROR CONNECTING FOR API");
        return null;
    }

    private String detectLang(String text) {
        try {
            Map<String,String> headers=new HashMap<>();
            headers.put("content-type","application/x-www-form-urlencoded");
            headers.put("Accept","*/*");
            HttpResponse response = getHttpResponsePost("translate.yandex.net","/api/v1.5/tr.json/detect", getDetectTranslateParams(),headers,text);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String jsonString = EntityUtils.toString(entity);
                    JSONObject jsonResponse = new JSONObject(jsonString); //Convert String to JSON Object
                    return jsonResponse.getString("lang");
                } else {
                    System.out.println("Response entity is null");
                }
            } else {
                System.out.println("Status api is not 200");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ERROR CONNECTING FOR API");
        return "@BOT is not Translate your message";

    }

    public String getTranslateText(String text) {
        try {
            Map<String,String> headers=new HashMap<>();
            headers.put("content-type","application/x-www-form-urlencoded");
            headers.put("Accept","*/*");
            HttpResponse response = getHttpResponsePost("translate.yandex.net","/api/v1.5/tr.json/translate", getTranslateParamsWithLang(detectLang(text)),headers,text);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String jsonString = EntityUtils.toString(entity);
                    JSONObject jsonResponse = new JSONObject(jsonString); //Convert String to JSON Object
                    JSONArray jsonArray = jsonResponse.getJSONArray("text");
                    return jsonArray.getString(0);
                } else {
                    System.out.println("Response entity is null");
                }
            } else {
                System.out.println("Status api is not 200");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ERROR CONNECTING FOR API");
        return "@BOT is not Translate your message";
    }

    class Parameter implements NameValuePair {
        private String name;
        private String value;

        public Parameter(String name, String value) {
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
}
