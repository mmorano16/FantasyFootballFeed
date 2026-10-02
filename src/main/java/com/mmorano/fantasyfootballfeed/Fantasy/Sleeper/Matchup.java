package com.mmorano.fantasyfootballfeed.Fantasy.Sleeper;

import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import java.util.ArrayList;
import java.util.HashMap;

public class Matchup {
    private String id;
    ArrayList<Player> players = new ArrayList<>();
    HashMap<String, Player> playerMap = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public HashMap<String, Player> getPlayerMap() {
        return playerMap;
    }

    public void setPlayerMap(HashMap<String, Player> playerMap) {
        this.playerMap = playerMap;
    }
}
