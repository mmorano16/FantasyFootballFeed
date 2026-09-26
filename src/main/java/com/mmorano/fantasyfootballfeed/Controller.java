package com.mmorano.fantasyfootballfeed;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ddFantasyApp.getItems().addAll("Sleeper");

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
        System.out.println(ddFantasyApp.getValue());
        if(ddFantasyApp.getValue() == null){
            txtSearchResponse.setText("Select Fantasy App");
            txtSearchResponse.setFill(Color.RED);
        } else if(inputUsername.getText().isEmpty()){
            txtSearchResponse.setText("Enter username");
            txtSearchResponse.setFill(Color.RED);
        }
        else{
            //query for account
        }
    }
}
