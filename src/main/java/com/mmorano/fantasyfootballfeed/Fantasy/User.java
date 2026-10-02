package com.mmorano.fantasyfootballfeed.Fantasy;

import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.League;

import java.util.ArrayList;

public class User {
    private String sleeperId;
    //other app owner ids?
    ArrayList<League> leagues = new ArrayList<>();


    public String getSleeperId() {
        return sleeperId;
    }

    public void setSleeperId(String sleeperId) {
        this.sleeperId = sleeperId;
    }

    public ArrayList<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(ArrayList<League> leagues) {
        this.leagues = leagues;
    }
}
