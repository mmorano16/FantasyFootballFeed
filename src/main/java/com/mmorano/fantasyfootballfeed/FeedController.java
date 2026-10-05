package com.mmorano.fantasyfootballfeed;

import com.mmorano.fantasyfootballfeed.DataFetching.SleeperDataFetcher;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.Fantasy.User;
import com.mmorano.fantasyfootballfeed.PlayFeed.Participant;
import com.mmorano.fantasyfootballfeed.PlayFeed.PlayCell;
import com.mmorano.fantasyfootballfeed.PlayFeed.Play;
import com.mmorano.fantasyfootballfeed.PlayFeed.PlayType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.HashSet;

public class FeedController {

    @FXML
    protected ListView<Play> listFeed = new ListView<>();
    protected ObservableList<Play> items = FXCollections.observableArrayList ();

    @FXML
    private AnchorPane paneFeedSettings = new AnchorPane();

    private SleeperDataFetcher sleeperDataFetcher;
    private HashSet<String> sleeperIds = new HashSet<>();
    private User user;
    private DataInitializer initializer;


    public void initData(User user, DataInitializer initializer){
        this.initializer = initializer;
        this.user = user;
        Button button = new Button("TEST");
        Label l = new Label(String.valueOf(items.size()));
        l.setLayoutY(100);
        button.setOnAction(Event ->{
            Play play = new Play();
            items.add(getPlay(1, 1));
            items.add(getPlay(1, 3.24));
            items.add(getPlay(1, -2.4));
            items.add(getPlay(2, .24));
            items.add(getPlay(2, 0));
            items.add(getPlay(2, 1));
            items.add(getPlay(3, -3.4));
            items.add(getPlay(3, 2.22));
            items.add(getPlay(3, -.12));
            listFeed.setCellFactory(param -> new PlayCell(initializer.teams, user));
            listFeed.setItems(items);
            l.setText(String.valueOf(items.size()));
        });
        paneFeedSettings.getChildren().add(button);

        //listFeed.setMouseTransparent(true);
        listFeed.setFocusTraversable(false);
    }

    private Play getPlay(int num, double points){
        Play play = new Play();
        PlayType playType = new PlayType();
        playType.setText("PASS PLAY");
        playType.setAbbrev("INTR");
        play.setPlayType(playType);
        play.setPeriod(1);
        play.setClockTime("10:45");
        play.setHomeAbbrev("BUF");
        play.setHomeScore(7);
        play.setAwayAbbrev("NE");
        play.setAwayScore(0);
        play.setText("(Shotgun) D.Maye pass incomplete short right.PENALTY on NE-A.Vera-Tucker, Ineligible Downfield Pass, 5 yards, enforced at SEA 48 - No Play. " +
                "AAAAAAAAAAAAAAAAAAAAAAaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        play.setDownDistanceText("1st & 10 at SEA 48");
        Participant participant = new Participant();
        Player player = new Player();
        player.setPlayerName("Josh Allen");
        player.setPosition("QB");
        player.setScore(13.48);
        player.setTeamId("2");
        player.setPlayerImage("https://a.espncdn.com/i/headshots/nfl/players/full/3918298.png");
        participant.setPlayer(player);
        participant.setScoreChange(points);
        participant.setTotalScore(13.48);
        if(points == 1)
            play.setTurnover(true);
        if(points == 0)
            play.setScoringPlay(true);
        for(int i = 0; i < num; i++) {
            play.getParticipants().put(String.valueOf(i), participant);
        }
        return play;
    }
}

