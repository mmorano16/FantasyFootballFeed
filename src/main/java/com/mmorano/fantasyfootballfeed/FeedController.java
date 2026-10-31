package com.mmorano.fantasyfootballfeed;

import com.mmorano.fantasyfootballfeed.DataFetching.EspnDataFetcher;
import com.mmorano.fantasyfootballfeed.DataFetching.SleeperDataFetcher;
import com.mmorano.fantasyfootballfeed.Fantasy.FilterOptions;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.League;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;

public class FeedController {

    @FXML
    protected ListView<Play> listFeed = new ListView<>();
    protected ObservableList<Play> items = FXCollections.observableArrayList ();

    @FXML
    private AnchorPane paneFeedSettings = new AnchorPane();
    @FXML
    private Button btnAllPlayers, btnMyPlayers, btnOppPlayers, btnHalfPpr, btnFullPpr, btnStandard;

//    private Border borderOn = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(50), new BorderWidths(2)));
//    private Border borderOff = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(50), new BorderWidths(0)));

    private String borderOn = "-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 50; -fx-background-radius: 50;";
    private String borderOff = "-fx-border-color: black; -fx-border-width: 0; -fx-border-radius: 50; -fx-background-radius: 50;";

    private SleeperDataFetcher sleeperDataFetcher;
    private HashSet<String> sleeperIds = new HashSet<>();
    private DataInitializer initializer;
    FilterOptions options = new FilterOptions();
    Thread runner;

    public void initData(DataInitializer initializer){
        this.initializer = initializer;
        Button button2 = new Button("Load Plays");
        btnAllPlayers.setStyle(borderOn);
        btnFullPpr.setStyle(borderOn);
        options.setScoring(FilterOptions.Scoring.PPR);
        options.setAllPlayers(true);
        options.setLeagues(new ArrayList<>(initializer.user.getLeagues()));

        button2.setLayoutX(200);
        FeedWorker fw = new FeedWorker(initializer);
        button2.setOnAction(Event -> {
            EspnDataFetcher dataFetcher = new EspnDataFetcher();
            items.clear();
            items.addAll(dataFetcher.getPlays(initializer, options));
            listFeed.setCellFactory(param -> new PlayCell(initializer.teams, initializer.user));
            listFeed.setItems(items);
            listFeed.setFocusTraversable(false);
        });

        double leagueSpacer = 20;
        double leagueHeight = 64;
        double buttonY = 501;
        Font font = Font.font("System", FontWeight.NORMAL, 24);

        for(League league : initializer.user.getLeagues()){
            Button button = new Button(league.getLeagueName());
            button.setFont(font);
            button.setStyle(borderOn);
            ImageView image = new ImageView(league.getLeagueImage());
            image.setFitHeight(48);
            image.setFitWidth(48);
            button.setGraphic(image);
            button.setLayoutX(14);
            button.setLayoutY(buttonY);
            button.setPrefHeight(Region.USE_COMPUTED_SIZE);
            button.setPrefWidth(Region.USE_COMPUTED_SIZE);
            button.setMaxWidth(350);
            buttonY += leagueHeight + leagueSpacer;

            button.setOnAction(Event -> {
                if(options.getLeagueIds().contains(league.getLeagueId())){
                    button.setStyle(borderOff);
                    options.getLeagueIds().remove(league.getLeagueId());
                } else {
                    button.setStyle(borderOn);
                    options.getLeagueIds().add(league.getLeagueId());
                }
            });

            paneFeedSettings.getChildren().add(button);
        }

        paneFeedSettings.getChildren().add(button2);
    }

    @FXML
    private void onMyPlayersClicked(){
        if(options.myPlayers()){
            options.setMyPlayers(false);
            btnMyPlayers.setStyle(borderOff);
            if(!options.opponentPlayers()) {
                options.setAllPlayers(true);
                btnAllPlayers.setStyle(borderOn);
            }
        } else {
            options.setMyPlayers(true);
            btnMyPlayers.setStyle(borderOn);
            btnAllPlayers.setStyle(borderOff);
        }
    }
    @FXML
    private void onOppPlayersClicked(){
        if(options.opponentPlayers()){
            options.setOpponentPlayers(false);
            btnOppPlayers.setStyle(borderOff);
            if(!options.myPlayers()){
                options.setAllPlayers(true);
                btnAllPlayers.setStyle(borderOn);
            }
        } else {
            options.setOpponentPlayers(true);
            btnOppPlayers.setStyle(borderOn);
            btnAllPlayers.setStyle(borderOff);
        }
    }
    @FXML
    private void onAllPlayersClicked(){
        options.setAllPlayers(true);
        btnAllPlayers.setStyle(borderOn);
        btnMyPlayers.setStyle(borderOff);
        btnOppPlayers.setStyle(borderOff);
    }
    @FXML
    private void onHalfPprClicked(){
        options.setScoring(FilterOptions.Scoring.HALF);
        btnHalfPpr.setStyle(borderOn);
        btnFullPpr.setStyle(borderOff);
        btnStandard.setStyle(borderOff);
    }
    @FXML
    private void onFullPprClicked(){
        options.setScoring(FilterOptions.Scoring.PPR);
        btnHalfPpr.setStyle(borderOff);
        btnFullPpr.setStyle(borderOn);
        btnStandard.setStyle(borderOff);
    }
    @FXML
    private void onStandardClicked(){
        options.setScoring(FilterOptions.Scoring.STANDARD);
        btnHalfPpr.setStyle(borderOff);
        btnFullPpr.setStyle(borderOff);
        btnStandard.setStyle(borderOn);
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

