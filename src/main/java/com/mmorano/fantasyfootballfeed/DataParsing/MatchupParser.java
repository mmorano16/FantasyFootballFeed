package com.mmorano.fantasyfootballfeed.DataParsing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.Matchup;

import java.util.ArrayList;
import java.util.HashMap;

public class MatchupParser extends DataParser {
    public Matchup parseMathup(String json, String rosterId, HashMap<String, Player> players){
        Matchup matchup = new Matchup();
        JsonArray jArray = JsonParser.parseString(json).getAsJsonArray();
        JsonObject jObject = findObjectByValue(jArray, "roster_id", rosterId, true);
        matchup.setId(jObject.get("matchup_id").getAsString());
        ArrayList<JsonObject> jObjects = findAllObjectsByValue(jArray, "matchup_id", matchup.getId());
        jObject = jObjects.stream().filter(x -> !x.get("roster_id").getAsString().equals(rosterId)).findFirst().orElse(null);
        jArray = jObject.get("starters").getAsJsonArray();
        for(JsonElement element : jArray)
            matchup.getPlayers().add(players.get(element.getAsString()));
        for(Player player : matchup.getPlayers())
            if(player != null && player.getEspnId() != null)
                matchup.getPlayerMap().put(player.getEspnId(), player);
        return matchup;
    }
}