package com.mmorano.fantasyfootballfeed.DataParsing;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.League;
import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.Matchup;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatchupParser extends DataParser {
    public Matchup parseMathup(String json, String rosterId, HashMap<String, Player> players, HashMap<String, String> ownerRosterIds){
        Matchup matchup = new Matchup();
        JsonArray jArray = JsonParser.parseString(json).getAsJsonArray();
        if(!jArray.isEmpty()) {
            JsonObject jObject = findObjectByValue(jArray, "roster_id", rosterId, true);
            matchup.setId(jObject.get("matchup_id").getAsString());
            ArrayList<JsonObject> jObjects = findAllObjectsByValue(jArray, "matchup_id", matchup.getId());
            jObject = jObjects.stream().filter(x -> !x.get("roster_id").getAsString().equals(rosterId)).findFirst().orElse(null);
            matchup.setRosterId(jObject.get("roster_id").getAsString());
            matchup.setOwnerId(ownerRosterIds.get(matchup.getRosterId()));
            jArray = jObject.get("starters").getAsJsonArray();
            for (JsonElement element : jArray) {
                Player player = players.get(element.getAsString());
                if (player != null)
                    matchup.getPlayers().add(player);
                else //add defense as player so that it appears on matchup summary
                    matchup.getPlayers().add(new Player(element.getAsString()));
            }
            for (Player player : matchup.getPlayers())
                if (player != null && player.getEspnId() != null)
                    matchup.getPlayerMap().put(player.getEspnId(), player);
        }
        return matchup;
    }

    public HashMap<String, Double> getMatchupScores(String json, League league){
        HashMap<String, Double> scores = new HashMap<>();
        JsonArray jArray = JsonParser.parseString(json).getAsJsonArray();
        if(!jArray.isEmpty()){
            JsonObject jsonObject = findObjectByValue(jArray, "roster_id", league.getRosterId(), true);
            scores.putAll(getMatchupScores(jsonObject, league.getLeagueId()));
            jsonObject = findObjectByValue(jArray, "roster_id", league.getMatchup().getRosterId(), true);
            scores.putAll(getMatchupScores(jsonObject, league.getLeagueId()));
        }
        return scores;
    }

    private HashMap<String, Double> getMatchupScores(JsonObject jsonObject, String leagueId){
        HashMap<String, Double> scores = new HashMap<>();
        //parse out the player_points object into a hashmap with playerId key before putting into return hashmap with leagueId + playerId key
        Type type = new TypeToken<HashMap<String, Double>>() {}.getType();
        HashMap<String, Double> playersPoints = new Gson().fromJson(
                jsonObject.get("players_points"),
                type
        );
        //store by leagueId + playerId so that different league scoring rules will be preserved
        for(HashMap.Entry<String, Double> entry : playersPoints.entrySet()){
            scores.put(leagueId + entry.getKey(), entry.getValue());
        }
        return scores;
    }

    public int parseWeek(String json){
        return JsonParser.parseString(json).getAsJsonObject().get("week").getAsInt();
    }
}