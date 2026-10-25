package com.mmorano.fantasyfootballfeed.DataParsing.Plays;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mmorano.fantasyfootballfeed.DataParsing.DataParser;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.PlayFeed.Participant;

import java.util.HashMap;

public class ParticipantParser extends DataParser {
    private final HashMap<String, Player> players;

    public ParticipantParser(HashMap<String, Player> players){
        this.players = players;
    }

    public HashMap<String, Participant> getParticipants(JsonArray jArray){
        HashMap<String, Participant> results = new HashMap<>();
        for(JsonElement element : jArray){
            Participant participant = new Participant();
            participant.setType(element.getAsJsonObject().get("type").getAsString());
            String playerLink = element.getAsJsonObject().get("athlete").getAsJsonObject().get("$ref").getAsString();
            String id = getIdFromLink(playerLink);
            Player player = players.get(id);
            if(player != null){
                participant.setPlayer(player);
                results.put(id, participant);
            }
        }
        return results;
    }
}
