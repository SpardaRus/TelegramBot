package com.spardarus.service.yandex;

import org.json.JSONObject;

@FunctionalInterface
public interface Parsable {
    Object getTagFromJSON(JSONObject jsonResponse);
}
