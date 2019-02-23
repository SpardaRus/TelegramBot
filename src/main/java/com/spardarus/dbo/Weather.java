package com.spardarus.dbo;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Weather {
    private static Map<String,String> mapConditions=new HashMap<>();
    static{
        mapConditions.put("clear","ясно");
        mapConditions.put("partly-cloudy","малооблачно");
        mapConditions.put("cloudy","облачно с прояснениями");
        mapConditions.put("overcast","пасмурно");
        mapConditions.put("partly-cloudy-and-light-rain","небольшой дождь");
        mapConditions.put("partly-cloudy-and-rain","дождь");
        mapConditions.put("overcast-and-rain","сильный дождь");
        mapConditions.put("overcast-thunderstorms-with-rain","сильный дождь, гроза");
        mapConditions.put("cloudy-and-light-rain","небольшой дождь");
        mapConditions.put("overcast-and-light-rain","небольшой дождь");
        mapConditions.put("cloudy-and-rain","дождь");
        mapConditions.put("overcast-and-wet-snow","дождь со снегом");
        mapConditions.put("partly-cloudy-and-light-snow","небольшой снег");
        mapConditions.put("partly-cloudy-and-snow","снег");
        mapConditions.put("overcast-and-snow","снегопад");
        mapConditions.put("cloudy-and-light-snow","небольшой снег");
        mapConditions.put("overcast-and-light-snow","небольшой снег");
        mapConditions.put("cloudy-and-snow","снег");

    }
    private String temp;
    private String feelsLike;
    private String condition;

    public String getCondition(){
        return mapConditions.get(condition);
    }
}