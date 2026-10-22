package com.mmorano.fantasyfootballfeed.Processing;

import com.mmorano.fantasyfootballfeed.Fantasy.Player;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;

public class DataSaver {

    public  final Path savePath = Path.of(System.getProperty("user.home"), "Desktop", "save.ser");
    //public final String savePath = Path.of(System.getProperty("user.home"), "Desktop", "save.ser").toString();
    public final Path playersPath = Path.of(System.getProperty("user.home"), "Desktop", "players.ser");//.toString();

    public boolean trySaveUser(SaveData data){
        boolean result = true;
        try (FileOutputStream fileOut = new FileOutputStream(savePath.toString());
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(data);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public SaveData loadUser(){
        SaveData data = new SaveData();
        try (FileInputStream fileIn = new FileInputStream(savePath.toString());
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            data = (SaveData) in.readObject();
        } catch (Exception e){
            System.out.println(e.getStackTrace());
        }
        return data;
    }
    public void trySavePlayers(ArrayList<Player> allPlayers) {
        boolean result = true;
        try (FileOutputStream fileOut = new FileOutputStream(playersPath.toString());
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(allPlayers);
        } catch (IOException e) {
            result = false;
        }
    }

    public ArrayList<Player> loadPlayers(){
        ArrayList<Player> players = new ArrayList<>();
        try (FileInputStream fileIn = new FileInputStream(playersPath.toString());
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            players = (ArrayList<Player>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return players;
    }
}
