package com.mmorano.fantasyfootballfeed;

import com.mmorano.fantasyfootballfeed.DataFetching.SleeperDataFetcher;
import com.mmorano.fantasyfootballfeed.DataParsing.LeagueParser;
import com.mmorano.fantasyfootballfeed.Fantasy.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    ComboBox ddFantasyApp;
    @FXML
    TextField inputUsername;
    @FXML
    Button btnAddAccount, btnContinueToFeed;
    @FXML
    Text txtSearchResponse;

    private SleeperDataFetcher sleeperDataFetcher;
    private HashSet<String> sleeperIds = new HashSet<>();
    User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ddFantasyApp.getItems().addAll("Sleeper");
        sleeperDataFetcher = new SleeperDataFetcher();
        user = new User();
    }

    @FXML
    protected void onContinueToFeedButtonClicked(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fantasy-feed.fxml"));
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
                txtSearchResponse.setText(MessageFormat.format("{0} account added.", ddFantasyApp.getValue().toString()));
                txtSearchResponse.setFill(Color.BLACK);
            }
        }
    }
}
