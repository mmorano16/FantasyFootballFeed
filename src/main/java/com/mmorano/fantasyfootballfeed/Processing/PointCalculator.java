package com.mmorano.fantasyfootballfeed.Processing;

import com.mmorano.fantasyfootballfeed.Fantasy.FilterOptions;
import com.mmorano.fantasyfootballfeed.PlayFeed.Participant;
import com.mmorano.fantasyfootballfeed.PlayFeed.Play;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PointCalculator {

    private final double
            passerYards = .04,
            rusherYards = .1,
            receiverYards = .1,
            touchdown = 6,
            tdPasser = 4,
            interception = -1,
            fumble = -2,
            patKick = 1,
            conversion = 2,
            missedKick = -1;
    private double reception;

    public double getPlayPoints(Play play, Participant participant, FilterOptions.Scoring scoring){
        double points = 0;
        reception = scoring.value;
        ArrayList<String> playParticipants = new ArrayList<>();
        play.getParticipants().forEach((k,v)->{
            playParticipants.add(v.getType());
        });
        boolean noPenalty = !play.isPenalty() || play.getText().contains("enforced between downs");
        if((noPenalty) && !participant.getPlayer().getPosition().equals("K"))
            points += getYardagePoints(participant.getType(), play.getText(), play.getPlayType().getText(), play.getStatYardage(), play.isTurnover());
        if(play.isScoringPlay() && noPenalty && !play.isTurnover())
            points += getScoringPoints(participant.getType(), participant.getPlayer().getPosition().equals("K"), play.getText().endsWith("ATTEMPT SUCCEEDS."), play.getStatYardage());
        //Turnover returned for TD, kicker makes PAT
        if(play.isScoringPlay() && noPenalty && play.isTurnover() && participant.getType().equals("patScorer") && participant.getPlayer().getPosition().equals("K"))
            points += 1;
        if(!play.getPlayType().getText().contains("Incompletion") && noPenalty && play.hasYAC() && !participant.getPlayer().getPosition().equals("K") &&
                (new HashSet<String>(List.of("receiver", "scorer", "patScorer", "fumbler"))).contains(participant.getType()))
            points += reception;
        if(play.isTurnover()) {
            points += getTurnoverPoints(participant.getType(), play.getPlayType().getText(), participant.getPlayer().getPosition(), playParticipants);
        }
        return points;
    }

    private double getTurnoverPoints(String playerType, String playType, String position, ArrayList<String> playParticipants){
        double points;
        switch (playerType){
            case "passer":
                if(!playType.contains("Fumble"))
                    points = interception;
                else if(!playParticipants.contains("fumbler"))
                    points = fumble;
                else points = 0;
                break;
            case "fumbler":
                points = fumble;
                break;
            case "tackler":
                if(position.equals("QB") && !playParticipants.contains("passer")){
                    if(playType.contains("Interception"))
                        points = interception;
                    else if(playType.contains("Fumble"))
                        points = fumble;
                    else
                        points = 0;
                }else
                    points = 0;
                break;
            default:
                points = 0;
                break;
        }

        return points;
    }

    private double getScoringPoints(String playerType, boolean isKicker, boolean conversionSuccessful, int yardage){
        double points;
        switch (playerType){
            case "patScorer":
                if(isKicker)
                    points = patKick;
                else if(conversionSuccessful)
                    points = conversion;
                else
                    points = 0;
                break;
            case "patPasser":
                if(conversionSuccessful)
                    points = conversion;
                else
                    points = 4;
                break;
            case "scorer":
                if(isKicker)
                    points = getFieldGoalPoints(yardage);
                else
                    points = touchdown;
                break;
            case "passer":
                points = tdPasser;
                break;
            default:
                points = 0;
                break;
        }

        return points;
    }

    private int getFieldGoalPoints(int yardage){
        int points;
        if(yardage <= 39)
            points = 3;
        else if(yardage >= 40 || yardage <= 49)
            points = 4;
        else if(yardage >= 50 || yardage <= 59)
            points = 5;
        else if(yardage >= 60)
            points = 6;
        else
            points = 0;
        return points;
    }

    private double getYardagePoints(String playerType, String playDesc, String playType, int yards, boolean isTurnover){
        double points;
        switch (playerType){
            case "passer":
                if(playType.startsWith("Sack") || isTurnover)
                    yards = 0;
                points = yards * passerYards;
                break;
            case "receiver":
                points = yards * receiverYards;
                break;
            case "rusher":
                points = yards * rusherYards;
                break;
            case "fumbler":
                if(playType.endsWith("(Opponent)") && !playDesc.contains("no gain") && !playType.startsWith("Sack"))
                {
                    int index = playDesc.indexOf("yards") - 1;
                    index = index > -1 ? index : playDesc.indexOf("Yds") - 1;
                    if(index > -1) {
                        String temp = playDesc.substring(0, index);
                        temp = temp.substring(temp.lastIndexOf(" ") + 1);
                        try {
                            yards = Integer.parseInt(temp);
                        } catch (Exception e) {
                        }
                    }
                }
                else if(playType.startsWith("Sack")){
                    yards = 0;
                }
                points = yards * rusherYards;
                break;
            case "scorer":
                if(playType.startsWith("Rush"))
                    points = yards * rusherYards;
                else if(playType.startsWith("Pass"))
                    points = yards * receiverYards;
                else
                    points = 0;
                break;
            default:
                points = 0;
                break;
        }
        return points;
    }
}

//[
// passer,(+0.04 per yard, +4 isTouchdown, -1 isTurnover)
// receiver,(+0.1 per yard, +1 per reception)
// rusher,(+0.1 per yard)
// fumbler (-2 isTurnover),
// penalized,--penalized player on isPenalty play
// scorer,(+3 FG0-19/20-29/30-39, +4 FG40-49, +5 FG50-59, +6 FG60+, +6 touchdown)
// patScorer,(+2 player scorer, +1 kicker, -1 field goal missed(text has extra point is No Good)) --field goal if kicker, 2pt conversion if player
// other,--target on interceptions
// tackler,--tackler on turnover
// kicker,(-1 field goal missed)--missed field goal kicker
// recoverer,--fumble recoverer
// returner,--muffed punt returner--will be deleting
// patPasser,(+2)--2pt conversion passer
// blocker,
// assistedBy--tackle assisted by
// ]
