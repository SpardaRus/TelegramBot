package com.spardarus.bot.service.yandex;

import org.json.JSONObject;

public interface Parser {
    Object getTagFromJSON(JSONObject jsonResponse);
}
