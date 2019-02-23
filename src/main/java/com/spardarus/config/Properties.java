package com.spardarus.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Properties {
    private final static Logger log = LoggerFactory.getLogger("com.spardarus.bot");
    private static Map<String, String> properties = new HashMap();

    static {
        try (FileReader application = new FileReader("config/application.properties")) {
            Scanner scan = new Scanner(application);
            while (scan.hasNext()) {
                StringBuilder line = new StringBuilder(scan.nextLine());
                properties.put(
                        String.valueOf(new StringBuilder(line).delete(new StringBuilder(line).indexOf("="), line.capacity())),
                        String.valueOf(new StringBuilder(line).delete(0, new StringBuilder(line).indexOf("=") + 1))

                );
            }
        } catch (IOException e) {
            log.error("File properties is missing");
        }
    }

    public static String getProperty(String key) {
        return properties.get(key);
    }
}
