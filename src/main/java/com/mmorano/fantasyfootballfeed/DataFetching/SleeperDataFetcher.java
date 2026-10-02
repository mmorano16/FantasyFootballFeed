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

public class SleeperDataFetcher extends DataFetcher {

    private final String apiUrl = "https://api.sleeper.app/v1";
    private LeagueParser leagueParser = new LeagueParser();
    private PlayerParser playerParser = new PlayerParser();
    private MatchupParser matchupParser = new MatchupParser();

    public String getOwnerId(String username){
        String id = "";
        String response = getResponse(MessageFormat.format("{0}/user/{1}", apiUrl, username));
        if(!response.isEmpty() && !response.equals("null"))
            id = leagueParser.parseOwnerId(response);
        return id;
    }

    public ArrayList<League> getLeagueData(String ownerId, HashMap<String, Player> players) {
        ArrayList<League> leagues = new ArrayList<>();
        String year;
        if (LocalDate.now().getMonthValue() > 6)
            year = String.valueOf(LocalDate.now().getYear());
        else
            year = String.valueOf(LocalDate.now().getYear() - 1);
        //pull league ids matching owner id
        String leagueIdsResponse = getResponse(MessageFormat.format("{0}/user/{1}/leagues/nfl/{2}", apiUrl, ownerId, year));
        HashMap<String, String> leagueIds = leagueParser.parseLeagueIds(leagueIdsResponse);

        leagueIds.forEach((id, name) -> {
            //pull league roster data
            String leagueResponse = getResponse(MessageFormat.format("{0}/league/{1}/rosters", apiUrl, id));
            League league = leagueParser.parseLeague(leagueResponse, ownerId, players);
            //pull league matchup roster data
            String matchupResponse = getResponse(MessageFormat.format("{0}/league/{1}/matchups/{2}", apiUrl, id, 3));
            league.setMatchup(matchupParser.parseMathup(matchupResponse, league.getRosterId(), players));
            //set league name and league id
            league.setLeagueName(name);
            league.setLeagueId(id);
            leagues.add(league);
        });

        return leagues;
    }
// String sleeperUserId = leagueParser.parseOwnerId(sleeperDataFetcher.getSleeperUserId(username));
//            leagueParser.setOwnerId(sleeperUserId);
//            //get sleeper leagues
//            HashMap<String, String> leagueIds = leagueParser.parseLeagueIds(sleeperDataFetcher.getLeagueIds(sleeperUserId));
//            leagueIds.forEach((id,name) ->{
//                leagues.add(getNewLeague(id, name));
//            });


//    private League getNewLeague(String id, String name){
//        try{
//            League league = new League();
//            league.setLeagueName(name);
//            league.setLeagueId(id);
//            leagueParser.parseLeague(sleeperDataFetcher.getLeagueData(id), league, sleeperPlayers);
//            matchupParser.parseMathup(sleeperDataFetcher.getMatchupData(id, 3),
//                    league.getRosterId(), league.getMatchup(), sleeperPlayers);
//            // for(String player : league.getplay)
//            for(Player player : league.getPlayers())
//                if(player != null)
//                    league.getPlayerMap().put(player.getEspnId(), player);
//            return league;
//        } catch (IOException e) {
//            return new League();
//        }
//    }


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
