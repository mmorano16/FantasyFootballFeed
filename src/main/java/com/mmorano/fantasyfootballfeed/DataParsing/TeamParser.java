package com.mmorano.fantasyfootballfeed.DataParsing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;

public class TeamParser extends DataParser {

    public HashMap<String, String> parseTeamIds(String json){
        HashMap<String, String> results = new HashMap<>();
        JsonArray jArray =  JsonParser.parseString(json).getAsJsonObject()
                .get("sports").getAsJsonArray().get(0).getAsJsonObject()
                .get("leagues").getAsJsonArray().get(0).getAsJsonObject()
                .get("teams").getAsJsonArray();
        for(JsonElement element : jArray) {
            JsonObject jObject = element.getAsJsonObject().get("team").getAsJsonObject();
            results.put(
                    jObject.get("id").getAsString(),
                    jObject.get("abbreviation").getAsString()
            );
        }
        return results;
    }
}
