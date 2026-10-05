package com.mmorano.fantasyfootballfeed.DataParsing;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;

import java.util.ArrayList;
import java.util.Map;

public class PlayerParser extends DataParser{
    public ArrayList<Player> parseSleeperPlayerJson(String json){
        ArrayList<Player> players = new ArrayList<>();
        JsonObject jObject = JsonParser.parseString(json).getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jObject.entrySet()) {
            JsonObject playerObject = entry.getValue().getAsJsonObject();
            String id = entry.getKey();
            Player player = new Player();
            if(playerObject.has("full_name"))
                player.setPlayerName(playerObject.get("full_name").getAsString());
            player.setPosition(playerObject.get("position").getAsString());
            player.setSleeperId(id);
            players.add(player);
        }
        return players;
    }

    public void parseESPNPlayerJson(String json, ArrayList<Player> players){
        JsonObject jObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray offense = getArrayByPosition(jObject, "offense");
        JsonArray specialTeam = getArrayByPosition(jObject, "specialTeam");
        for(Player player : players){
            setPlayerESPNIds(offense, player);
            setPlayerESPNIds(specialTeam, player);
            //player.setTeamId(jObject.get("team").getAsJsonObject().get("id").getAsString());
        }
    }

    private void setPlayerESPNIds(JsonArray jArray, Player player){
        JsonObject playerObject = findObjectByValue(jArray, "fullName", player.getPlayerName(), false);
        if(playerObject != null && player.getPlayerName() != null && !player.getPlayerName().isEmpty()){
            player.setEspnId(playerObject.get("id").getAsString());
            player.setTeamId(parseTeamId(playerObject));
            if(playerObject.has("headshot"))
                //if(playerObject.get("headshot").getAsJsonObject().has("href"))
                player.setPlayerImage(playerObject.get("headshot").getAsJsonObject().get("href").getAsString());
        }
    }

    private String parseTeamId(JsonObject jObject){
        String teamId = "";
        if(jObject.has("teams")){
            teamId = getIdFromLink(jObject.get("teams").getAsJsonArray().get(0).getAsJsonObject().get("$ref").getAsString());
        }
        return teamId;
    }
    private JsonArray getArrayByPosition(JsonObject jObject, String position){
        return findObjectByValue(
                jObject.get("athletes").getAsJsonArray(), "position", position, true)
                .get("items").getAsJsonArray();
    }
}
