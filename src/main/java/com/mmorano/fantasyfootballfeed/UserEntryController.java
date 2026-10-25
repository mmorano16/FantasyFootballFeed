package com.mmorano.fantasyfootballfeed;

import com.mmorano.fantasyfootballfeed.DataFetching.SleeperDataFetcher;
import com.mmorano.fantasyfootballfeed.Fantasy.User;
import com.mmorano.fantasyfootballfeed.Processing.DataInitializer;
import com.mmorano.fantasyfootballfeed.Processing.DataSaver;
import com.mmorano.fantasyfootballfeed.Processing.SaveData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.MessageFormat;
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
    private String sleeperId;


    public void setDataInitializer(DataInitializer initializer){
        this.initializer = initializer;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ddFantasyApp.getItems().addAll("Sleeper");
        ddFantasyApp.setValue("Sleeper");
        btnContinueToFeed.setDisable(true);
        //inputUsername.setText("mmorano16");
    }

    @FXML
    protected void onContinueToFeedButtonClicked(ActionEvent event) throws IOException {
        btnAddAccount.setDisable(true);
        btnContinueToFeed.setDisable(true);

        //user.setLeagues(sleeperDataFetcher.getLeagueData(user.getSleeperId(), initializer.sleeperPlayers));
        initializer.updateUser(sleeperId);

        System.out.println(initializer.user.getLeagues());

        saveData();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(Application.feedFxmlFile));

        Parent root = loader.load();
        FeedController feedController = loader.getController();
        feedController.initData(initializer);
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Application.listViewWidth = feedController.listFeed.getPrefWidth();
    }

    @FXML
    protected void onAddAccountButtonClicked(ActionEvent event) {
        btnAddAccount.setDisable(true);
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
                sleeperId = ownerId;
                txtSearchResponse.setText(MessageFormat.format("{0} account added.", ddFantasyApp.getValue().toString()));
                txtSearchResponse.setFill(Color.BLACK);
                btnContinueToFeed.setDisable(false);
                inputUsername.setText("");
                ddFantasyApp.setValue("");
            }
        }
    }

    private void saveData(){
        DataSaver dataSaver = new DataSaver();
        SaveData saveData = new SaveData();
        saveData.setSleeperId(sleeperId);
        dataSaver.trySaveUser(saveData);
    }
}
