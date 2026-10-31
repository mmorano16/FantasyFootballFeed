package com.mmorano.fantasyfootballfeed.DataFetching;

import com.mmorano.fantasyfootballfeed.DataParsing.LeagueParser;
import com.mmorano.fantasyfootballfeed.DataParsing.MatchupParser;
import com.mmorano.fantasyfootballfeed.DataParsing.PlayerParser;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.League;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SleeperDataFetcher extends DataFetcher {

    private final String apiUrl = "https://api.sleeper.app/v1";
    private LeagueParser leagueParser = new LeagueParser();
    private PlayerParser playerParser = new PlayerParser();
    private MatchupParser matchupParser = new MatchupParser();
    private HashMap<String, String> rosterImages = new HashMap<>();

    public HashMap<String, String> getRosterImages(){ return rosterImages; }

    public String getOwnerId(String username){
        String id = "";
        String response = getResponse(MessageFormat.format("{0}/user/{1}", apiUrl, username));
        if(!response.isEmpty() && !response.equals("null"))
            id = leagueParser.parseOwnerId(response);
        return id;
    }


    public ArrayList<League> getLeagueData(String ownerId, HashMap<String, Player> players, int week) {
        ArrayList<League> leagues = new ArrayList<>();
        String year;
        if (LocalDate.now().getMonthValue() > 6)
            year = String.valueOf(LocalDate.now().getYear());
        else
            year = String.valueOf(LocalDate.now().getYear() - 1);

        String leaguesResponse = getResponse(MessageFormat.format("{0}/user/{1}/leagues/nfl/{2}", apiUrl, ownerId, year));
        leagues = leagueParser.parseLeagues(leaguesResponse);
        //ArrayList<String> ids =

        for(League league : leagues){
            String rosterResponse = getResponse(MessageFormat.format("{0}/league/{1}/rosters", apiUrl, league.getLeagueId()));
            leagueParser.parseLeaguerRoster(rosterResponse, ownerId, league, players);
            HashMap<String, String> ownerRosterIds = leagueParser.getOwnerRosterIds(rosterResponse);
            String usersResponse = getResponse(MessageFormat.format("{0}/league/{1}/users", apiUrl, league.getLeagueId()));
            HashMap<String, String> leagueAvatars = leagueParser.parseLeagueAvatars(usersResponse);
            league.setAvatar(leagueAvatars.get(ownerId));
            String matchupResponse = getResponse(MessageFormat.format("{0}/league/{1}/matchups/{2}", apiUrl, league.getLeagueId(), week));
            league.setMatchup(matchupParser.parseMathup(matchupResponse, league.getRosterId(), players, ownerRosterIds));
            league.getMatchup().setAvatar(leagueAvatars.get(league.getMatchup().getOwnerId()));
            league.setLeagueType(League.LeagueType.SLEEPER);
        }

        return leagues;
    }

    public HashMap<String, Double> getPlayerScores(ArrayList<League> leagues, int week){
        HashMap<String, Double> playerScores = new HashMap<>();
        for(League league : leagues){
            if(league.getLeagueType() == League.LeagueType.SLEEPER){
                String matchupResponse = getMatchupResponse(league.getLeagueId(), week);
                playerScores.putAll(matchupParser.getMatchupScores(matchupResponse, league));
            }
        }
        return playerScores;
    }

    private String getMatchupResponse(String id, int week){
        return getResponse(MessageFormat.format("{0}/league/{1}/matchups/{2}", apiUrl, id, week));
    }

    public int getWeek(){
        return matchupParser.parseWeek(getResponse(MessageFormat.format("{0}/state/nfl", apiUrl)));
    }

    public ArrayList<Player> getPlayerData(){
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();
        try {
            HashMap<String, String> urls = new HashMap<>();
            for(String pos : new String[]{"QB","TE","RB","WR","K","DEF"})
                urls.put(pos, MessageFormat.format("{0}/players/nfl?position={1}&active=true", apiUrl, pos));
            responses = getResponses(urls);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        for(String response : responses){
            players.addAll(playerParser.parseSleeperPlayerJson(response));
        }
        return players;
    }
}
