package com.mmorano.fantasyfootballfeed.Fantasy.Sleeper;

import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import java.util.ArrayList;
import java.util.HashMap;

public class Matchup {
    private String id;
    private ArrayList<Player> players = new ArrayList<>();
    private HashMap<String, Player> playerMap = new HashMap<>();
    private String rosterId;
    private String ownerId;
    private String avatar;

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

    public String getRosterId() {
        return rosterId;
    }

    public void setRosterId(String rosterId) {
        this.rosterId = rosterId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
