package com.mmorano.fantasyfootballfeed.Fantasy;

import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.League;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class FilterOptions {
    private boolean myPlayers;
    private boolean opponentPlayers;
    private boolean allPlayers;

    private ArrayList<League> leagues = new ArrayList<>();
    private HashSet<String> leagueIds = new HashSet<>();

    private Scoring scoring;

    public boolean myPlayers() {
        return myPlayers;
    }

    public void setMyPlayers(boolean myPlayers) {
        this.myPlayers = myPlayers;
        if(myPlayers)
            allPlayers = false;
    }

    public boolean opponentPlayers() {
        return opponentPlayers;
    }

    public void setOpponentPlayers(boolean opponentPlayers) {
        this.opponentPlayers = opponentPlayers;
        if(opponentPlayers)
            allPlayers = false;
    }

    public boolean allPlayers() {
        return allPlayers;
    }

    public void setAllPlayers(boolean allPlayers) {
        this.allPlayers = allPlayers;
        if(allPlayers){
            myPlayers = false;
            opponentPlayers = false;
        }
    }

    public ArrayList<League> getLeagues() {
        return leagues;
    }

    public HashSet<String> getLeagueIds() {
        return leagueIds;
    }

    public void setLeagueIds(HashSet<String> leagueIds) {
        this.leagueIds = leagueIds;
    }

    public void setLeagues(ArrayList<League> leagues) {
        this.leagues = leagues;
        for(League league : leagues)
            leagueIds.add(league.getLeagueId());
    }

    public Scoring getScoring() {
        return scoring;
    }

    public void setScoring(Scoring scoring) {
        this.scoring = scoring;
    }

    public enum Scoring{
        PPR(1.0),
        HALF(0.5),
        STANDARD(0.0);

        public final double value;
        private Scoring(double value){
            this.value = value;
        }
    }

    public HashMap<String, Player> getFilteredPlayers(){
        HashMap<String, Player> players = new HashMap<>();
        for(League league : leagues){
            if(myPlayers)
                players.putAll(league.getPlayerMap());
            if(opponentPlayers)
                players.putAll(league.getMatchup().getPlayerMap());
        }
        return players;
    }
}
