package com.mmorano.fantasyfootballfeed;

import com.mmorano.fantasyfootballfeed.DataFetching.SleeperDataFetcher;
import com.mmorano.fantasyfootballfeed.Fantasy.User;
import com.mmorano.fantasyfootballfeed.PlayFeed.PlayCell;
import com.mmorano.fantasyfootballfeed.PlayFeed.Play;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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

    public void initData(User user, DataInitializer initializer){
        this.initializer = initializer;
        this.user = user;

        items.add(new Play());
        items.add(new Play());
        items.add(new Play());

        listFeed.setCellFactory(param -> new PlayCell());
        listFeed.setItems(items);
    }

}

