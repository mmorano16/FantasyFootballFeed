package com.mmorano.fantasyfootballfeed.DataParsing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mmorano.fantasyfootballfeed.PlayFeed.Event;

import java.util.ArrayList;
import java.util.HashMap;

public class EventParser extends DataParser{
    private HashMap<String, String> teams;
    public EventParser(HashMap<String, String> teams){
        this.teams = teams;
    }
    public ArrayList<String> parseEventIds(String json){
        ArrayList<String>  ids = new ArrayList<>();
        JsonObject jObject = JsonParser.parseString(json).getAsJsonObject();
        for(JsonElement element : jObject.get("items").getAsJsonArray())
            ids.add(getIdFromLink(element.getAsJsonObject().get("$ref").getAsString()));
        return ids;
    }

    public void parseEvent(String json, HashMap<String, Event> events){
        Event event = new Event();
        JsonObject jObject = JsonParser.parseString(json).getAsJsonObject();
        String key = jObject.get("id").getAsString();
        event.setId(key);
        JsonArray jArray = jObject.get("competitors").getAsJsonArray();
        for(JsonElement element : jArray){
            jObject = element.getAsJsonObject();
            String id = jObject.get("id").getAsString();
            String abbrev = jObject.get("homeAway").getAsString();
            if(abbrev.equals("home")) {
                event.setHomeTeamId(id);
                event.setHomeTeamAbbrev(teams.get(id));
            } else if (abbrev.equals("away")){
                event.setAwayTeamId(id);
                event.setAwayTeamAbbrev(teams.get(id));
            }
        }
        events.put(key, event);
    }
}
