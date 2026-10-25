package com.mmorano.fantasyfootballfeed.DataFetching;

import com.mmorano.fantasyfootballfeed.DataParsing.EventParser;
import com.mmorano.fantasyfootballfeed.DataParsing.PlayerParser;
import com.mmorano.fantasyfootballfeed.DataParsing.Plays.PlayParser;
import com.mmorano.fantasyfootballfeed.DataParsing.TeamParser;
import com.mmorano.fantasyfootballfeed.Fantasy.FilterOptions;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.PlayFeed.Event;
import com.mmorano.fantasyfootballfeed.PlayFeed.Play;
import com.mmorano.fantasyfootballfeed.PlayFeed.PlayCell;
import com.mmorano.fantasyfootballfeed.Processing.DataInitializer;
import com.mmorano.fantasyfootballfeed.Processing.PlayerScoreUpdater;

import java.text.MessageFormat;
import java.util.*;

public class EspnDataFetcher extends DataFetcher {

    private final String apiSiteUrl = "https://site.api.espn.com/apis/site/v2/sports/football/nfl";
    private final String apiCoreUrl = "https://sports.core.api.espn.com/v2/sports/football/leagues/nfl";

    private PlayerParser playerParser = new PlayerParser();
    private TeamParser teamParser = new TeamParser();
    private EventParser eventParser;
    private PlayParser playParser;
    private PlayerScoreUpdater scoreUpdater = new PlayerScoreUpdater(FilterOptions.Scoring.PPR);

    public void updatePlayerEspnIds(Set<String> ids, ArrayList<Player> allPlayers) {
        HashMap<String, String> urls = new HashMap<>();
        for(String id : ids)
            urls.put("Team:" + id, MessageFormat.format("{0}/teams/{1}/roster", apiSiteUrl, id));
        for(String response : getResponses(urls))
            playerParser.parseESPNPlayerJson(response, allPlayers);
    }

    public HashMap<String, String> getTeamData(){
        return teamParser.parseTeamIds(getResponse(MessageFormat.format("{0}/teams", apiSiteUrl)));
    }

    public HashMap<String, Event> getEvents(HashMap<String, String> teams){
        eventParser = new EventParser(teams);
        HashMap<String, Event> events = new HashMap<>();
        String response = getResponse(MessageFormat.format("{0}/events", apiCoreUrl));
        for(String eventResponse : getEventData(eventParser.parseEventIds(response)))
            eventParser.parseEvent(eventResponse, events);
        return events;
    }

    private ArrayList<String> getEventData(ArrayList<String> ids){
        HashMap<String, String> urls = new HashMap<>();
        for(String id : ids)
            urls.put("Event:" + id, MessageFormat.format("{0}/events/{1}/competitions/{1}", apiCoreUrl, id));
        return getResponses(urls);
    }

    public ArrayList<Play> getPlays(DataInitializer initializer, FilterOptions filterOptions){
        playParser = new PlayParser(initializer, filterOptions);
        scoreUpdater.clearPlayerScores(initializer.espnPlayers);
        ArrayList<Play> plays = new ArrayList<>();
        HashMap<String, String> urls = new HashMap<>();
        for(String id : initializer.events.keySet())
            urls.put("Event:" + id, MessageFormat.format("{0}/events/{1}/competitions/{1}/plays?limit=500&page=1", apiCoreUrl, id));
        for(String response : getResponses(urls)){
            plays.addAll(playParser.parsePlays(response));
        }
        plays.sort(Comparator.comparing(Play::getTime).reversed());
        if(plays.size() > 50)
            plays.subList(50, plays.size()).clear();
        ArrayList<PlayCell> cells = new ArrayList<>();
        return plays;
    }
}
