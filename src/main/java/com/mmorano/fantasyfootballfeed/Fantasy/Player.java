package com.mmorano.fantasyfootballfeed.Fantasy;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Objects;

public class Player implements Serializable{
    private String sleeperId;
    private String espnId;
    private String playerName;
    private String teamId;
    private String position;
    private double score = 0;

    public Player(){}
    public Player(Player player){
        this.sleeperId = player.getSleeperId();
        this.espnId = player.getEspnId();
        this.playerName = player.getPlayerName();
        this.teamId = player.getTeamId();
        this.position = player.getPosition();
    }

    public String getSleeperId() {
        return sleeperId;
    }

    public void setSleeperId(String sleeperId) {
        this.sleeperId = sleeperId;
    }

    public String getEspnId() {
        return espnId;
    }

    public void setEspnId(String espnId) {
        this.espnId = espnId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString(){
        return MessageFormat.format("\nName:{0}\nPosition:{1}\nTeam ID:{2}\nSleeper ID:{3}\nESPN ID:{4}\n", playerName, position, teamId, sleeperId, espnId);
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Method[] methods = this.getClass().getMethods();
        for(Method method : methods){
            if(method.getName().startsWith("get")){
                try{
                    Object x = method.invoke(this);
                    Object y = method.invoke(obj);
                    if(x==null) {
                        if (y != null) {
                            return false;
                        }
                    }
                    else if(!x.equals(y)) {
                        return false;
                    }
                } catch (IllegalAccessException | InvocationTargetException e){
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sleeperId, espnId, playerName);
    }
}