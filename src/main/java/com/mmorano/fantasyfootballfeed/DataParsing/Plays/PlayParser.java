package com.mmorano.fantasyfootballfeed.DataParsing.Plays;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mmorano.fantasyfootballfeed.DataParsing.DataParser;
import com.mmorano.fantasyfootballfeed.Fantasy.FilterOptions;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.League;
import com.mmorano.fantasyfootballfeed.PlayFeed.Event;
import com.mmorano.fantasyfootballfeed.PlayFeed.Play;
import com.mmorano.fantasyfootballfeed.Processing.DataInitializer;
import com.mmorano.fantasyfootballfeed.Processing.PlayerScoreUpdater;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PlayParser extends DataParser {

    private HashMap<String, String> teams;
    private ParticipantParser participantParser;
    private PlayTypeParser playTypeParser = new PlayTypeParser();
    private HashMap<String, Event> events;
    private FilterOptions filterOptions;
    private PlayerScoreUpdater scoreUpdater;
    private final HashSet<String> skipPlays = new HashSet<>(List.of(
            "End of Game", "Timeout", "Two-minute warning", "Official Timeout", "Kickoff", "End Period", "End of Half", "Punt", "Coin Toss", "Muffed Punt Recovery", "Blocked Punt"
    ));
    private HashMap<String, Player> players;
    public PlayParser(DataInitializer initializer, FilterOptions filterOptions){
        teams = initializer.teams;
        events = initializer.events;
        players = new HashMap<>();
        if(filterOptions.allPlayers()){
            players.putAll(initializer.espnPlayers);
        } else {
            for(League league : initializer.user.getLeagues()) {
                if (filterOptions.myPlayers())
                    players.putAll(league.getPlayerMap());
                if (filterOptions.opponentPlayers())
                    players.putAll(league.getMatchup().getPlayerMap());
            }
        }
        participantParser = new ParticipantParser(players);
        scoreUpdater = new PlayerScoreUpdater(FilterOptions.Scoring.PPR);
    }


    public ArrayList<Play> parsePlays(String json){
        ArrayList<Play> plays = new ArrayList<>();
        JsonObject jObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray jArray = jObject.get("items").getAsJsonArray();
        for(JsonElement element : jArray) {
            Play play = parsePlay(element.getAsJsonObject());
            if(!skipPlays.contains(play.getPlayType().getText()) && play.getParticipants().keySet().stream().anyMatch(x -> players.keySet().contains(x))) {
                scoreUpdater.updateParticipantScores(play, players);
                plays.add(play);
            }
        }
        return plays;
    }

    private Play parsePlay(JsonObject jObject){
        Play play = new Play();
        Event event = events.get(getEventIdFromLink(jObject.get("$ref").getAsString()));
        play.setId(jObject.get("id").getAsString());
        play.setPlayType(playTypeParser.parsePlayType(jObject.get("type").getAsJsonObject()));
        play.setText(jObject.get("text").getAsString());
        play.setAwayScore(jObject.get("awayScore").getAsInt());
        play.setAwayAbbrev(event.getAwayTeamAbbrev());
        play.setHomeScore(jObject.get("homeScore").getAsInt());
        play.setHomeAbbrev(event.getHomeTeamAbbrev());
        play.setPeriod(jObject.get("period").getAsJsonObject().get("number").getAsInt());
        play.setClockTime(jObject.get("clock").getAsJsonObject().get("displayValue").getAsString());
        play.setScoreValue(jObject.get("scoreValue").getAsInt());
        play.setTeam(getIdFromLink(jObject.get("team").getAsJsonObject().get("$ref").getAsString()));
        if(jObject.has("participants"))
            play.setParticipants(participantParser.getParticipants(jObject.get("participants").getAsJsonArray()));
        play.setPenalty(jObject.get("isPenalty").getAsBoolean());
        play.setStatYardage(jObject.get("statYardage").getAsInt());
        if(jObject.get("start").getAsJsonObject().has("downDistanceText"))
            play.setDownDistanceText(jObject.get("start").getAsJsonObject().get("downDistanceText").getAsString());
        play.setTurnover(jObject.get("isTurnover").getAsBoolean());
        play.setScoringPlay(jObject.get("scoringPlay").getAsBoolean());
        if(jObject.has("wallclock"))
            play.setTime(LocalDateTime.parse(jObject.get("wallclock").getAsString(), DateTimeFormatter.ISO_DATE_TIME));
        if(jObject.has("yardsAfterCatch"))
            play.setHasYAC(true);
        return play;
    }

    private String getEventIdFromLink(String link){
        //http://sports.core.api.espn.com/v2/sports/football/leagues/nfl/events/401872927/competitions/401872927/plays/4018729271?lang=en&region=us
        link = link.substring(0, link.lastIndexOf("plays") - 1);
        return link.substring(link.lastIndexOf("/") + 1);
    }
}
