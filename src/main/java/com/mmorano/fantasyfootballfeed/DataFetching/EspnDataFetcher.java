package com.mmorano.fantasyfootballfeed.DataFetching;

import com.mmorano.fantasyfootballfeed.DataParsing.PlayerParser;
import com.mmorano.fantasyfootballfeed.DataParsing.TeamParser;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class EspnDataFetcher extends DataFetcher {

    private final String apiSiteUrl = "https://site.api.espn.com/apis/site/v2/sports/football/nfl";
    private final String apiCoreUrl = "https://sports.core.api.espn.com/v2/sports/football/leagues/nfl";

    private PlayerParser playerParser = new PlayerParser();
    private TeamParser teamParser = new TeamParser();

    public void updatePlayerEspnIds(Set<String> ids, ArrayList<Player> allPlayers) {
        ArrayList<String> responses = new ArrayList<>();
        try {
            HashMap<String, String> urls = new HashMap<>();
            for(String id : ids)
                urls.put("Team:" + id, MessageFormat.format("{0}/teams/{1}/roster", apiSiteUrl, id));
            responses = getResponses(urls);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        for(String response : responses)
            playerParser.parseESPNPlayerJson(response, allPlayers);
    }

    public HashMap<String, String> getTeamData(){
        return teamParser.parseTeamIds(getResponse(MessageFormat.format("{0}/teams", apiSiteUrl)));
    }
}
