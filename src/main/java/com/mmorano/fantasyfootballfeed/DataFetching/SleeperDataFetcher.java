package com.mmorano.fantasyfootballfeed.DataFetching;

import com.mmorano.fantasyfootballfeed.DataParsing.LeagueParser;

import java.text.MessageFormat;

public class SleeperDataFetcher extends DataFetcher {

    private final String apiUrl = "https://api.sleeper.app/v1";
    private LeagueParser leagueParser = new LeagueParser();

    public String getOwnerId(String username){
        String id = "";
        String response = getResponse(MessageFormat.format("{0}/user/{1}", apiUrl, username));
        if(!response.isEmpty() && !response.equals("null"))
            id = leagueParser.parseOwnerId(response);
        return id;
    }

}
