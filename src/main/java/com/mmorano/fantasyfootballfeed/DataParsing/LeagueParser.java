package com.mmorano.fantasyfootballfeed.DataParsing;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LeagueParser {
    public String parseOwnerId(String json){
        String id = "";
        if(!json.isEmpty()) {
            JsonObject jObject = JsonParser.parseString(json).getAsJsonObject();
            if (jObject.has("user_id")) {
                id = jObject.get("user_id").getAsString();
            }
        }
        return id;
    }
}
