package com.mmorano.fantasyfootballfeed.PlayFeed;
import java.text.MessageFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Event {
    private String id;
    private String homeTeamId;
    private String homeTeamAbbrev;
    private String awayTeamId;
    private String awayTeamAbbrev;
    private LocalDateTime startTime;

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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        try{
            Instant instant = OffsetDateTime.parse(startTime).toInstant();
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            this.startTime = zonedDateTime.toLocalDateTime();
        } catch (Exception e){
            this.startTime = LocalDateTime.MIN;
        }
    }

    @Override
    public String toString(){
        return MessageFormat.format("\nEvent ID:{0}\nHome ID:{1}\nHome Abbrev:{2}\nAway ID:{3}\nAway Abbrev:{4}\n",
                id, homeTeamId, homeTeamAbbrev, awayTeamId, awayTeamAbbrev);
    }
}