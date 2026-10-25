package com.mmorano.fantasyfootballfeed.Fantasy;

import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.League;

import java.util.ArrayList;
import java.util.HashMap;

public class FilterOptions {
    private boolean myPlayers;
    private boolean opponentPlayers;
    private boolean allPlayers;

    private ArrayList<League> leagues;

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
            myPlayers = true;
            opponentPlayers = true;
        }
    }

    public ArrayList<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(ArrayList<League> leagues) {
        this.leagues = leagues;
    }

    public Scoring getScoring() {
        return scoring;
    }

    public void setScoring(Scoring scoring) {
        this.scoring = scoring;
    }

    public enum Scoring{
        PPR,
        HALF,
        STANDARD
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
