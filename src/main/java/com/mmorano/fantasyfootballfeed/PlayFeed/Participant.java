package com.mmorano.fantasyfootballfeed.PlayFeed;

import com.mmorano.fantasyfootballfeed.Fantasy.Player;

import java.text.MessageFormat;

public class Participant {
    private Player player = new Player();
    private String type;
    private double scoreChange;
    private double totalScore;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getScoreChange() {
        return scoreChange;
    }

    public void setScoreChange(double scoreChange) {
        this.scoreChange = scoreChange;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public String toString(){
        return MessageFormat.format("ID:{0} - Name:{1} - Type:{2}\n", player.getEspnId(), player.getPlayerName(), type);
    }
}
