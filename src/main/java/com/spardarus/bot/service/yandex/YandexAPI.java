package com.spardarus.bot.service.yandex;


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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.spardarus.bot.config.Proxy.getProxyHttpHost;

public class YandexAPI {

    private static final String HOST_WEATHER="api.weather.yandex.ru/v1";
    private static final String PATH_WEATHER="/forecast";
    private static final String HOST_TRANSLATE="translate.yandex.net";
    private static final String PATH_TRANSLATE="/api/v1.5/tr.json/translate";

private List<NameValuePair> getWeatherParams(){
    List<NameValuePair> weatherParams=new ArrayList<>();
    weatherParams.add(new Parameter("lat", "51.533103"));
    weatherParams.add(new Parameter("lon", "46.034158"));
    weatherParams.add(new Parameter("lang", "\"ru_RU\""));
    weatherParams.add(new Parameter("limit", "1"));
    weatherParams.add(new Parameter("hours", "false"));
    weatherParams.add(new Parameter("extra", "false"));
    return weatherParams;
}

    private List<NameValuePair> getTranslateParams(){
        List<NameValuePair> weatherParams=new ArrayList<>();
        weatherParams.add(new Parameter("lang", "en-ru"));
        weatherParams.add(new Parameter("key", "trnsl.1.1.20190130T092751Z.120a53b75f8c4046.0a58915d6e790d4b83b4f05d45617f12cd2cd74f"));
        return weatherParams;
    }

private URI getURI(String host, String path, List<NameValuePair> params){
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
    return  uri;
}
    public Weather getWeather(){
        try {
            URI uri = getURI(HOST_WEATHER,PATH_WEATHER,getWeatherParams());
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setHeader("X-Yandex-API-Key", "802beaae-6082-4f25-ac28-e246e5a0d633");
            HttpClient client = HttpClientBuilder.create().setProxy(getProxyHttpHost()).build();//proxy
          //  HttpClient client = HttpClientBuilder.create().build();//without proxy
            HttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity=response.getEntity();
                if (entity != null){
                    Weather weather=new Weather();
                    String jsonString = EntityUtils.toString(entity);
                    JSONObject jsonResponse = new JSONObject(jsonString); //Convert String to JSON Object
                    JSONObject fact=jsonResponse.getJSONObject("fact");
                    weather.setTemp(fact.get("temp").toString());
                    weather.setFeelsLike(fact.get("feels_like").toString());
                    weather.setCondition(fact.getString("condition"));
                    weather.setIcon("https://yastatic.net/weather/i/icons/blueye/color/svg/"+
                            fact.getString("icon")+
                    ".svg");

                    return weather;
                }else{
                    System.out.println("Response entity is null");
                }
            }else
            {
                System.out.println("Status api is not 200");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ERROR CONNECTING FOR API");
        return null;
    }

    public String getTranslateText(String text){
        try {
            URI uri = getURI(HOST_TRANSLATE,PATH_TRANSLATE,getTranslateParams());
            HttpPost httpPost = new HttpPost(uri);

            StringEntity params =new StringEntity("text="+text);
            httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
            httpPost.addHeader("Accept", "*/*");
            httpPost.setEntity(params);
            HttpClient client = HttpClientBuilder.create().setProxy(getProxyHttpHost()).build();//proxy
            //  HttpClient client = HttpClientBuilder.create().build();//without proxy
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity=response.getEntity();
                if (entity != null){
                    String jsonString = EntityUtils.toString(entity);
                    JSONObject jsonResponse = new JSONObject(jsonString); //Convert String to JSON Object
                    JSONArray jsonArray=jsonResponse.getJSONArray("text");
                    return jsonArray.getString(0);
                }else{
                    System.out.println("Response entity is null");
                }
            }else
            {
                System.out.println("Status api is not 200");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ERROR CONNECTING FOR API");
        return "@BOT is not Translate your message";
    }

    class Parameter implements NameValuePair{
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
