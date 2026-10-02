package com.mmorano.fantasyfootballfeed.PlayFeed;
import java.text.MessageFormat;

public class Event {
    private String id;
    private String homeTeamId;
    private String homeTeamAbbrev;
    private String awayTeamId;
    private String awayTeamAbbrev;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getHomeTeamAbbrev() {
        return homeTeamAbbrev;
    }

    public void setHomeTeamAbbrev(String homeTeamAbbrev) {
        this.homeTeamAbbrev = homeTeamAbbrev;
    }

    public String getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(String awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public String getAwayTeamAbbrev() {
        return awayTeamAbbrev;
    }

    public void setAwayTeamAbbrev(String awayTeamAbbrev) {
        this.awayTeamAbbrev = awayTeamAbbrev;
    }

    @Override
    public String toString(){
        return MessageFormat.format("\nEvent ID:{0}\nHome ID:{1}\nHome Abbrev:{2}\nAway ID:{3}\nAway Abbrev:{4}\n",
                id, homeTeamId, homeTeamAbbrev, awayTeamId, awayTeamAbbrev);
    }
}