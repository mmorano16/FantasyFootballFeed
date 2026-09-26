package com.mmorano.fantasyfootballfeed;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("username-entry.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 557, 336);
        stage.setTitle("Fantasy Football Feed");
        stage.setScene(scene);
        stage.show();
    }
}
