package com.mmorano.fantasyfootballfeed;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import com.mmorano.fantasyfootballfeed.UI.LeagueButtonBuilder;
import com.mmorano.fantasyfootballfeed.UI.MatchupTabBuilder;
import com.mmorano.fantasyfootballfeed.Workers.FeedWorker;
import com.sun.jdi.ThreadReference;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class FeedController {

    @FXML
    protected ListView<Play> listFeed = new ListView<>();
    protected ObservableList<Play> items = FXCollections.observableArrayList ();

    @FXML
    private AnchorPane paneFeedSettings = new AnchorPane();
    @FXML
    private TabPane tabPaneMatchups = new TabPane();
    @FXML
    private Button btnAllPlayers, btnMyPlayers, btnOppPlayers, btnHalfPpr, btnFullPpr, btnStandard;

//    private Border borderOn = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(50), new BorderWidths(2)));
//    private Border borderOff = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(50), new BorderWidths(0)));

    private String borderOn = "-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 50; -fx-background-radius: 50;";
    private String borderOff = "-fx-border-color: black; -fx-border-width: 0; -fx-border-radius: 50; -fx-background-radius: 50;";

    private SleeperDataFetcher sleeperDataFetcher = new SleeperDataFetcher();
    private HashSet<String> sleeperIds = new HashSet<>();
    private DataInitializer initializer;
    private FilterOptions options = new FilterOptions();
    private HashMap<String, Double> playerScores;
    Thread runner;

    public void initData(DataInitializer initializer){
        this.initializer = initializer;
        Button button2 = new Button("Load Plays");
        button2.setLayoutX(200);

        btnAllPlayers.setStyle(borderOn);
        btnFullPpr.setStyle(borderOn);
        options.setScoring(FilterOptions.Scoring.PPR);
        options.setAllPlayers(true);
        options.setLeagues(new ArrayList<>(initializer.user.getLeagues()));

//        Tab tab = new Tab(initializer.user.getLeagues().get(1).getLeagueName());
//        Font font = Font.font("System", FontWeight.NORMAL, 24);
//        tab.setStyle("-fx-font-size: 18px;");
//        ImageView icon = new ImageView(initializer.user.getLeagues().get(1).getLeagueImage());
//        icon.setFitHeight(48);
//        icon.setFitWidth(48);
//
//        Rectangle clip = new Rectangle(icon.getFitWidth(), icon.getFitHeight());
//        clip.setArcWidth(30);   // Adjust for desired corner roundness
//        clip.setArcHeight(30);
//        icon.setClip(clip);
//
//        tab.setGraphic(icon);
//
//        Label label = new Label();
//        label.setPrefHeight(Region.USE_COMPUTED_SIZE);
//        label.setPrefWidth(Region.USE_COMPUTED_SIZE);
//        tab.setContent(label);
//
//        Text t = new Text(initializer.user.getLeagues().get(1).getLeagueName());
//        t.setFont(Font.font("System", FontWeight.NORMAL, 18));
//
            tabPaneMatchups.setTabMinWidth(200);
            tabPaneMatchups.setTabMaxWidth(200);
            tabPaneMatchups.setTabMinHeight(50);
            tabPaneMatchups.setTabMaxHeight(100);
//        tabPaneMatchups.getTabs().add(tab);
//        tabPaneMatchups.setStyle(font.getStyle());

        paneFeedSettings.getChildren().addAll(LeagueButtonBuilder.getLeagueButton(initializer.user.getLeagues(), options));
        paneFeedSettings.getChildren().add(button2);
        tabPaneMatchups.getTabs().addAll(MatchupTabBuilder.getMatchupTabs(initializer.user.getLeagues()));


        FeedWorker fw = new FeedWorker(initializer);
        button2.setOnAction(Event -> {
            EspnDataFetcher dataFetcher = new EspnDataFetcher();
            items.clear();
            items.addAll(dataFetcher.getPlays(initializer, options));
            listFeed.setCellFactory(param -> new PlayCell(initializer.teams, initializer.user));
            listFeed.setItems(items);
            listFeed.setFocusTraversable(false);

            //TODO - move this to a different area or something
            playerScores = sleeperDataFetcher.getPlayerScores(initializer.user.getLeagues(), initializer.week);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            HashMap<String, Double> temp = new HashMap<>();
            int i;
            for(League league : initializer.user.getLeagues()){
                i = 0;
                for(Player player : league.getPlayers()){
                    if(player != null) {
                        String key = league.getLeagueId() + player.getSleeperId();
                        if (playerScores.containsKey(key)) {
                            temp.put(league.getRosterPositions()[i] + ": " + player.getPlayerName(), playerScores.get(key));
                        }
                    }
                    i++;
                }
                i = 0;
                for(Player player : league.getMatchup().getPlayers()){
                    if(player != null) {
                        String key = league.getLeagueId() + player.getSleeperId();
                        if (playerScores.containsKey(key)) {
                            temp.put(league.getRosterPositions()[i] + ": " + player.getPlayerName(), playerScores.get(key));
                        }
                    }
                    i++;
                }
            }
            //label.setText(gson.toJson(temp));
        });
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

