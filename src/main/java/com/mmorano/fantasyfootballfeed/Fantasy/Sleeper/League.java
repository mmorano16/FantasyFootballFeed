package com.mmorano.fantasyfootballfeed.Fantasy.Sleeper;

import com.mmorano.fantasyfootballfeed.Fantasy.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class League {
    private String leagueName;
    private String leagueId;
    private String rosterId;
    private ArrayList<Player> players = new ArrayList<>();
    private HashMap<String, Player> playerMap = new HashMap<>();
    private Matchup matchup = new Matchup();
    private String leagueImage;

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
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

    public Matchup getMatchup() {
        return matchup;
    }

    public void setMatchup(Matchup matchup) {
        this.matchup = matchup;
    }

    public String getRosterId() {
        return rosterId;
    }

    public void setRosterId(String rosterId) {
        this.rosterId = rosterId;
    }

    public String getLeagueImage() {
        return leagueImage;
    }

    public void setLeagueImage(String leagueImage) {
        this.leagueImage = leagueImage == null ?
                "https://sleepercdn.com/images/v2/icons/league/nfl/" + getRandomColor() + ".png" :
                "https://sleepercdn.com/avatars/" + leagueImage;
    }

    private String getRandomColor(){
        Random r = new Random();
        String[] colors = new String[]{"green", "red", "blue", "purple", "orange"};
        return colors[r.nextInt(colors.length)];
    }

    @Override
    public String toString() {
        return MessageFormat.format("League:{0}\nID:{1}\nRosterID:{2}\nPlayers:\n\t{3}\n", leagueName, leagueId, rosterId, players.toString());
    }
}