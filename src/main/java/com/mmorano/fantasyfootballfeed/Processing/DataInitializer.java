package com.mmorano.fantasyfootballfeed.Processing;

import com.mmorano.fantasyfootballfeed.DataFetching.EspnDataFetcher;
import com.mmorano.fantasyfootballfeed.DataFetching.SleeperDataFetcher;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.League;
import com.mmorano.fantasyfootballfeed.Fantasy.User;
import com.mmorano.fantasyfootballfeed.PlayFeed.Event;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;

public class DataInitializer {

    private EspnDataFetcher espnDataFetcher = new EspnDataFetcher();
    private SleeperDataFetcher sleeperDataFetcher = new SleeperDataFetcher();

    public HashMap<String, String> teams;
    public ArrayList<Player> allPlayers = new ArrayList<>();
    public HashMap<String, Player> espnPlayers = new HashMap<>();
    public HashMap<String, Player> sleeperPlayers = new HashMap<>();
    private ArrayList<League> leagues = new ArrayList<>();
    public SaveData saveData;
    public User user = new User();
    public HashMap<String, Event> events = new HashMap<>();
    private final File playersFile = new File("players.ser");
    private final File saveFile = new File("save.ser");

    public void initializeData(){
        teams = espnDataFetcher.getTeamData();
        events = espnDataFetcher.getEvents(teams);

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

        if(saveFile.exists()){
            loadSaveData();
            user.setLeagues(sleeperDataFetcher.getLeagueData(saveData.getSleeperId(), sleeperPlayers));
        }
    }

    public void updateUser(String sleeperId){
        user.setLeagues(sleeperDataFetcher.getLeagueData(sleeperId, sleeperPlayers));
    }

    private void updateAllPlayers(){
        allPlayers = sleeperDataFetcher.getPlayerData();
        espnDataFetcher.updatePlayerEspnIds(teams.keySet(), allPlayers);
        allPlayers.removeIf(x -> x.getEspnId() == null);
    }

    private boolean fileNeedsUpdate(){
        boolean result = false;
        try {
            FileTime fileTime = Files.getLastModifiedTime(playersFile.toPath());
            LocalDate lastModifiedDate = fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            result = lastModifiedDate.isBefore(LocalDate.now());
        } catch (IOException e) {
            result = true;
        }
        return result;
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

    private void loadSaveData(){
        try(FileInputStream fileIn = new FileInputStream("save.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn)){
            saveData = (SaveData) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
