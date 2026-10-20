package com.mmorano.fantasyfootballfeed;

import com.mmorano.fantasyfootballfeed.DataFetching.EspnDataFetcher;
import com.mmorano.fantasyfootballfeed.DataFetching.SleeperDataFetcher;
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
    Thread runner;


    public void initData(DataInitializer initializer){
        this.initializer = initializer;
        Button button2 = new Button("Load Plays");
        button2.setLayoutX(200);
        Label l1 = new Label("TEST");
        l1.setLayoutX(50);
        l1.setLayoutY(300);
        FeedWorker fw = new FeedWorker(initializer);
        button2.setOnAction(Event -> {
            EspnDataFetcher dataFetcher = new EspnDataFetcher();
            items.addAll(dataFetcher.getPlays(initializer, null));
            listFeed.setCellFactory(param -> new PlayCell(initializer.teams, user));
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
}

