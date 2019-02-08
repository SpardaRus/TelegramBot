package com.spardarus.bot.config;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Properties {
private static Map<String,String> properties =new HashMap();
static {
    try (FileReader application = new FileReader("src/main/resources/application.properties")) {
        Scanner scan = new Scanner(application);
        while (scan.hasNext()){
            StringBuilder line=new StringBuilder(scan.nextLine());
            properties.put(
                    String.valueOf(new StringBuilder(line).delete(new StringBuilder(line).indexOf("="),line.capacity())),
                    String.valueOf(new StringBuilder(line).delete(0,new StringBuilder(line).indexOf("=")+1))

            );
        }
    } catch (IOException e) {
        System.err.println("ОШИБКА: Файл свойств отсуствует!");
    }
}

    public static String getProperty(String key) {
        return properties.get(key);
    }
}
