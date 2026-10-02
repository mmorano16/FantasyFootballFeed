package com.mmorano.fantasyfootballfeed.PlayFeed;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Play {
    private String id;
    private PlayType playType;
    private String text;
    private int awayScore;
    private String awayAbbrev;
    private int homeScore;
    private String homeAbbrev;
    private int period;
    private String clockTime;
    private boolean scoringPlay;
    private int scoreValue;
    private String team;
    private HashMap<String, Participant> participants = new HashMap<>();
    private LocalDateTime time;
    private boolean isPenalty;
    private int statYardage;
    private String downDistanceText;
    private boolean isTurnover;
    private boolean hasYAC;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PlayType getPlayType() {
        return playType;
    }

    public void setPlayType(PlayType playType) {
        this.playType = playType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getClockTime() {
        return clockTime;
    }

    public void setClockTime(String clockTime) {
        this.clockTime = clockTime;
    }

    public boolean isScoringPlay() {
        return scoringPlay;
    }

    public void setScoringPlay(boolean scoringPlay) {
        this.scoringPlay = scoringPlay;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(int scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public HashMap<String, Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(HashMap<String, Participant> participants) {
        this.participants = participants;
    }

    public LocalDateTime getTime() {
        return time == null ? LocalDateTime.MIN : time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public boolean isPenalty() {
        return isPenalty;
    }

    public void setPenalty(boolean penalty) {
        isPenalty = penalty;
    }

    public int getStatYardage() {
        return statYardage;
    }

    public void setStatYardage(int statYardage) {
        this.statYardage = statYardage;
    }

    public String getDownDistanceText() {
        return downDistanceText;
    }

    public void setDownDistanceText(String downDistanceText) {
        this.downDistanceText = downDistanceText;
    }

    public boolean isTurnover() {
        return isTurnover;
    }

    public void setTurnover(boolean turnover) {
        isTurnover = turnover;
    }

    public String getAwayAbbrev() {
        return awayAbbrev;
    }

    public void setAwayAbbrev(String awayAbbrev) {
        this.awayAbbrev = awayAbbrev;
    }

    public String getHomeAbbrev() {
        return homeAbbrev;
    }

    public void setHomeAbbrev(String homeAbbrev) {
        this.homeAbbrev = homeAbbrev;
    }

    public boolean hasYAC() {
        return hasYAC;
    }

    public void setHasYAC(boolean hasYAC) {
        this.hasYAC = hasYAC;
    }

    public long getIdInt(){
        return Long.parseLong(id);
    }

    @Override
    public String toString(){
        //play type (period time) home score - away score
        //participants
        //down distance text
        //text
        return MessageFormat.format(
                "\n{0} ({1}Q {2})\t{3} {4}-{5} {6}\n{7}\n{8} - {9}\n",
                playType.getText(), period, clockTime, homeAbbrev, homeScore, awayScore, awayAbbrev, participants.values().toString(), downDistanceText, text);
    }
}

