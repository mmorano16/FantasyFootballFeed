package com.mmorano.fantasyfootballfeed;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mmorano.fantasyfootballfeed.DataFetching.EspnDataFetcher;
import com.mmorano.fantasyfootballfeed.DataFetching.SleeperDataFetcher;
import com.mmorano.fantasyfootballfeed.DataParsing.LeagueParser;
import com.mmorano.fantasyfootballfeed.DataParsing.PlayerParser;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.League;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataInitializer {

    private EspnDataFetcher espnDataFetcher = new EspnDataFetcher();
    private SleeperDataFetcher sleeperDataFetcher = new SleeperDataFetcher();

    private HashMap<String, String> teams;
    public ArrayList<Player> allPlayers = new ArrayList<>();
    public HashMap<String, Player> espnPlayers = new HashMap<>();
    public HashMap<String, Player> sleeperPlayers = new HashMap<>();
    private ArrayList<League> leagues = new ArrayList<>();
    private ArrayList<String> eventIds = new ArrayList<>();
    //private HashMap<String, Event> events = new HashMap<>();

    private final File playersFile = new File("players.ser");
    public void initializeData(){
        teams = espnDataFetcher.getTeamData();

        if(!playersFile.exists() || fileNeedsUpdate()){
            updateAllPlayers();
            updatePlayersFile();
        } else {
            allPlayers = loadPlayers();
        }

        //set maps of players for each source as key
        for(Player player : allPlayers){
            if(player.getEspnId() != null)
                espnPlayers.put(player.getEspnId(), player);
            sleeperPlayers.put(player.getSleeperId(), player);
        }
    }

    private void updateAllPlayers(){
        allPlayers = sleeperDataFetcher.getPlayerData();
        espnDataFetcher.updatePlayerEspnIds(teams.keySet(), allPlayers);
        allPlayers.removeIf(x -> x.getEspnId() == null);
    }

    private boolean fileNeedsUpdate(){
        return true;
    }

    private void updatePlayersFile() {
        try (FileOutputStream fileOut = new FileOutputStream("players.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(allPlayers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Player> loadPlayers(){
        ArrayList<Player> players = new ArrayList<>();
        try (FileInputStream fileIn = new FileInputStream("players.ser");
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            players = (ArrayList<Player>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return players;
    }

}
