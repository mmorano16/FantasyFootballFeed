package com.mmorano.fantasyfootballfeed.Processing;

import com.mmorano.fantasyfootballfeed.Fantasy.FilterOptions;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.PlayFeed.Play;

import java.util.HashMap;

public class PlayerScoreUpdater {
    private FilterOptions.Scoring scoring;
    PointCalculator calculator = new PointCalculator();

    public PlayerScoreUpdater(FilterOptions.Scoring scoring){
        this.scoring = scoring;
    }

    public void updateParticipantScores(Play play, HashMap<String, Player> players){
        play.getParticipants().forEach((id,participant) -> {
            participant.setScoreChange(calculator.getPlayPoints(play, participant));
            Player player = players.get(participant.getPlayer().getEspnId());
            if(player != null) {
                player.setScore(player.getScore() + participant.getScoreChange());
                participant.setTotalScore(player.getScore());
            }
        });
    }

    public void clearPlayerScores(HashMap<String, Player> players){
        for(Player p : players.values()){
            p.setScore(0);
        }
    }



//    {
//        67 = Passing Touchdown,
//        24 = Pass Reception,
//        68 = Rushing Touchdown,
//        36 = Interception Return Touchdown,
//        59 = Field Goal Good,
//        26 = Pass Interception Return,
//        17 = Blocked Punt,
//        29 = Fumble Recovery(Opponent),
//        3 = Pass Incompletion,
//        5 = Rush,
//        7 = Sack,
//        8 = Penalty,
//        80 = Sack Opp Fumble Recovery,
//        9 = Fumble Recovery(Own),
//        60 = Field Goal Missed,
//        30 = Muffed Punt Recovery (Opponent)
//    }

}
