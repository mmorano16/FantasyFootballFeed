package com.mmorano.fantasyfootballfeed.DataParsing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.League;

import java.util.HashMap;

public class LeagueParser extends DataParser{
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

    public HashMap<String, String> parseLeagueIds(String json){
        HashMap<String, String> ids = new HashMap<>();
        JsonArray jArray = JsonParser.parseString(json).getAsJsonArray();
        for(JsonElement element : jArray) {
            JsonObject jObject = element.getAsJsonObject();
            ids.put(jObject.get("league_id").getAsString(), jObject.get("name").getAsString());
        }
        return ids;
    }

    public League parseLeague(String json, String ownerId, HashMap<String, Player> players){
        League league = new League();
        JsonArray jArray = JsonParser.parseString(json).getAsJsonArray();
        JsonObject jObject = findObjectByValue(jArray, "owner_id", ownerId, true);
        league.setRosterId(jObject.get("roster_id").getAsString());
        jArray = jObject.get("starters").getAsJsonArray();
        for(JsonElement element : jArray)
            league.getPlayers().add(players.get(element.getAsString()));
        for(Player player : league.getPlayers())
            if(player != null && player.getEspnId() != null)
                league.getPlayerMap().put(player.getEspnId(), player);
        return league;
    }
}
