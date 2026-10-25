package com.mmorano.fantasyfootballfeed;

import com.mmorano.fantasyfootballfeed.DataFetching.EspnDataFetcher;
import com.mmorano.fantasyfootballfeed.DataFetching.SleeperDataFetcher;
import com.mmorano.fantasyfootballfeed.Fantasy.FilterOptions;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.Fantasy.User;
import com.mmorano.fantasyfootballfeed.PlayFeed.Participant;
import com.mmorano.fantasyfootballfeed.PlayFeed.PlayCell;
import com.mmorano.fantasyfootballfeed.PlayFeed.Play;
import com.mmorano.fantasyfootballfeed.PlayFeed.PlayType;
import com.mmorano.fantasyfootballfeed.Processing.DataInitializer;
import com.mmorano.fantasyfootballfeed.Workers.FeedWorker;
import com.sun.jdi.ThreadReference;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;

import java.util.HashSet;

public class FeedController {

    @FXML
    protected ListView<Play> listFeed = new ListView<>();
    protected ObservableList<Play> items = FXCollections.observableArrayList ();

    @FXML
    private AnchorPane paneFeedSettings = new AnchorPane();

    private SleeperDataFetcher sleeperDataFetcher;
    private HashSet<String> sleeperIds = new HashSet<>();
    private DataInitializer initializer;
    Thread runner;


    public void initData(DataInitializer initializer){
        this.initializer = initializer;
        Button button2 = new Button("Load Plays");
        FilterOptions options = new FilterOptions();
//        Button btnMyPlayers = new Button("My Players");
//        btnMyPlayers.setOnAction(Event -> {
//            options.setMyPlayers(true);
//            btnMyPlayers.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
//        });
//        Button btnOppPlayers = new Button("Opp Players");
//        btnOppPlayers.setOnAction(Event -> {
//            options.setOpponentPlayers(true);
//            btnOppPlayers.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
//        });
//        Button btnAllPlayers = new Button("All Players");
//        btnAllPlayers.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
//        btnAllPlayers.setOnAction(Event -> {
//            options.setAllPlayers(true);
//            btnAllPlayers.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
//            btnMyPlayers.setStyle("-fx-border-color: black; -fx-border-width: 0px;");
//            btnOppPlayers.setStyle("-fx-border-color: black; -fx-border-width: 0px;");
//        });
        button2.setLayoutX(200);
        Label l1 = new Label("TEST");
        l1.setLayoutX(50);
        l1.setLayoutY(300);
        FeedWorker fw = new FeedWorker(initializer);
        button2.setOnAction(Event -> {
            EspnDataFetcher dataFetcher = new EspnDataFetcher();
            items.clear();
            options.setMyPlayers(true);
            options.setOpponentPlayers(true);
            //options.setAllPlayers(true);
            items.addAll(dataFetcher.getPlays(initializer, options));
            listFeed.setCellFactory(param -> new PlayCell(initializer.teams, initializer.user));
            listFeed.setItems(items);
        });
//        button2.setOnAction(Event -> {
//            //l1.textProperty().bind(fw.messageProperty());
//            ChangeListener<String> cl = (observableValue, s, t1) -> l1.setText(t1.toString());
//            fw.messageProperty().addListener(cl);
//            runner = new Thread(fw);
//            runner.start();
//        });

        paneFeedSettings.getChildren().add(button2);
        paneFeedSettings.getChildren().add(l1);

        //listFeed.setMouseTransparent(true);
        listFeed.setFocusTraversable(false);
    }

//    private Play getPlay(int num, double points){
//        Play play = new Play();
//        PlayType playType = new PlayType();
//        playType.setText("PASS PLAY");
//        playType.setAbbrev("INTR");
//        play.setPlayType(playType);
//        play.setPeriod(1);
//        play.setClockTime("10:45");
//        play.setHomeAbbrev("BUF");
//        play.setHomeScore(7);
//        play.setAwayAbbrev("NE");
//        play.setAwayScore(0);
//        play.setText("(Shotgun) D.Maye pass incomplete short right.PENALTY on NE-A.Vera-Tucker, Ineligible Downfield Pass, 5 yards, enforced at SEA 48 - No Play. " +
//                "AAAAAAAAAAAAAAAAAAAAAAaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
//        play.setDownDistanceText("1st & 10 at SEA 48");
//        Participant participant = new Participant();
//        Player player = new Player();
//        player.setPlayerName("Josh Allen");
//        player.setPosition("QB");
//        player.setScore(13.48);
//        player.setTeamId("2");
//        player.setPlayerImage("https://a.espncdn.com/i/headshots/nfl/players/full/3918298.png");
//        participant.setPlayer(player);
//        participant.setScoreChange(points);
//        participant.setTotalScore(13.48);
//        if(points == 1)
//            play.setTurnover(true);
//        if(points == 0)
//            play.setScoringPlay(true);
//        for(int i = 0; i < num; i++) {
//            play.getParticipants().put(String.valueOf(i), participant);
//        }
//        return play;
//    }
}

