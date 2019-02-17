package com.spardarus.bot.service.yandex;

import com.spardarus.bot.dbo.Weather;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class YandexAPI {

    private ConnectionBuilder connectionBuilder = new ConnectionBuilder();

    public Weather getWeather() {
        return (Weather) getProperty(connectionBuilder.getHttpResponseWeather(),
                (jsonResponse) -> {
                    JSONObject fact = jsonResponse.getJSONObject("fact");
                    Weather weather = new Weather();
                    weather.setTemp(fact.get("temp").toString());
                    weather.setFeelsLike(fact.get("feels_like").toString());
                    weather.setCondition(fact.getString("condition"));
                    return weather;
                });
    }

    public String detectLang(String text) {
        return (String) getProperty(connectionBuilder.getHttpResponseDetect(text),
                (jsonResponse) -> jsonResponse.getString("lang"));
    }

    public String getTranslateText(String text) {
        return (String) getProperty(connectionBuilder.getHttpResponseTranslate(text),
                (jsonResponse) -> {
                    JSONArray jsonArray = jsonResponse.getJSONArray("text");
                    return jsonArray.getString(0);
                });
    }

    private Object getProperty(HttpResponse response, Parser parser) {
        try {
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String jsonString = EntityUtils.toString(entity);
                    return parser.getTagFromJSON(new JSONObject(jsonString));
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


}
