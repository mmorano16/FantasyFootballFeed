package com.mmorano.fantasyfootballfeed.DataParsing;

import com.google.gson.*;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.League;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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

    public HashMap<String, String> getOwnerRosterIds(String json){
        HashMap<String, String> ids = new HashMap<>();
        JsonArray jArray = JsonParser.parseString(json).getAsJsonArray();
        for(JsonElement element : jArray){
            ids.put(element.getAsJsonObject().get("roster_id").getAsString(), element.getAsJsonObject().get("owner_id").getAsString());
        }
        return ids;
    }

    public HashMap<String, String> parseLeagueAvatars(String json){
        HashMap<String, String> avatars = new HashMap<>();

        JsonArray jArray = JsonParser.parseString(json).getAsJsonArray();
        for(JsonElement element : jArray){
            JsonObject jObject = element.getAsJsonObject();
            String ownerId = jObject.get("user_id").getAsString();
            JsonObject metadata = jObject.get("metadata").getAsJsonObject();
            String avatar = metadata.has("avatar") ? metadata.get("avatar").getAsString() :
                    "https://sleepercdn.com/images/v2/icons/league/nfl/" + getRandomColor() + ".png";
            avatars.put(ownerId, avatar);
        }
        return avatars;
    }

    private String getRandomColor(){
        Random r = new Random();
        String[] colors = new String[]{"green", "red", "blue", "purple", "orange"};
        return colors[r.nextInt(colors.length)];
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

    public ArrayList<League> parseLeagues(String json){
        ArrayList<League> leagues = new ArrayList<>();
        JsonArray jArray = JsonParser.parseString(json).getAsJsonArray();
        for(JsonElement element : jArray){
            JsonObject jObject = element.getAsJsonObject();
            League league = new League();
            league.setLeagueId(jObject.get("league_id").getAsString());
            league.setLeagueName(jObject.get("name").getAsString());
            league.setRosterPositions( new Gson().fromJson(jObject.get("roster_positions").getAsJsonArray(), String[].class));
            JsonElement avatar = jObject.get("avatar");
            league.setLeagueImage(avatar.isJsonNull() ? null : avatar.getAsString());
            leagues.add(league);
        }
        return leagues;
    }

    public void parseLeaguerRoster(String json, String ownerId, League league, HashMap<String, Player> players){
        JsonArray jArray = JsonParser.parseString(json).getAsJsonArray();
        JsonObject jObject = findObjectByValue(jArray, "owner_id", ownerId, true);
        league.setRosterId(jObject.get("roster_id").getAsString());
        jArray = jObject.get("starters").getAsJsonArray();
        for(JsonElement element : jArray){
            Player player = players.get(element.getAsString());
            if(player != null)
                league.getPlayers().add(player);
            else //add defense as player so that it appears on matchup summary
                league.getPlayers().add(new Player(element.getAsString()));

        }
        for(Player player : league.getPlayers())
            if(player != null && player.getEspnId() != null)
                league.getPlayerMap().put(player.getEspnId(), player);
    }
}
