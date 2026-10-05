package com.mmorano.fantasyfootballfeed;

import com.mmorano.fantasyfootballfeed.DataFetching.SleeperDataFetcher;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.League;
import com.mmorano.fantasyfootballfeed.Fantasy.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.ResourceBundle;

public class UserEntryController implements Initializable {
    @FXML
    private ComboBox ddFantasyApp;
    @FXML
    private TextField inputUsername;
    @FXML
    private Button btnAddAccount, btnContinueToFeed;
    @FXML
    private Text txtSearchResponse;

    private DataInitializer initializer = new DataInitializer();
    private SleeperDataFetcher sleeperDataFetcher = new SleeperDataFetcher();
    private User user;

    public static double listViewWidth;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ddFantasyApp.getItems().addAll("Sleeper");
        initializer.initializeData();
        user = new User();
        btnContinueToFeed.setDisable(true);
        inputUsername.setText("mmorano16");
    }

    @FXML
    protected void onContinueToFeedButtonClicked(ActionEvent event) throws IOException {
        btnAddAccount.setDisable(true);
        btnContinueToFeed.setDisable(true);

        user.setLeagues(sleeperDataFetcher.getLeagueData(user.getSleeperId(), initializer.sleeperPlayers));

        System.out.println(user.getLeagues());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fantasy-feed.fxml"));

        Parent root = loader.load();
        FeedController feedController = loader.getController();
        feedController.initData(user, initializer);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        listViewWidth = feedController.listFeed.getPrefWidth();
    }

    @FXML
    protected void onAddAccountButtonClicked(ActionEvent event) {
        if(ddFantasyApp.getValue() == null){
            txtSearchResponse.setText("Select Fantasy App");
            txtSearchResponse.setFill(Color.RED);
        } else if(inputUsername.getText().isEmpty()){
            txtSearchResponse.setText("Enter username");
            txtSearchResponse.setFill(Color.RED);
        }
        else{
            String ownerId = sleeperDataFetcher.getOwnerId(inputUsername.getText());
            if(ownerId.isEmpty()){
                txtSearchResponse.setText("Unable to retrieve account data.\nEnsure Username is entered correctly.");
                txtSearchResponse.setFill(Color.RED);
            } else {
                user.setSleeperId(ownerId);
                txtSearchResponse.setText(MessageFormat.format("{0} account added.", ddFantasyApp.getValue().toString()));
                txtSearchResponse.setFill(Color.BLACK);
                btnContinueToFeed.setDisable(false);
            }
        }
    }
}
