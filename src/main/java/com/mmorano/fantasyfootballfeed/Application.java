package com.mmorano.fantasyfootballfeed;

import com.mmorano.fantasyfootballfeed.PlayFeed.Event;
import com.mmorano.fantasyfootballfeed.Processing.DataInitializer;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Application extends javafx.application.Application {
    public static double listViewWidth;
    public static String feedFxmlFile;
    @Override
    public void start(Stage stage) throws IOException {
        DataInitializer initializer = new DataInitializer();
        // 1. Create UI elements for your startup screen
        Label statusLabel = new Label("Initializing application...");
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(250);

        VBox root = new VBox(10, statusLabel, progressBar);
        root.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #f3f3f3;");

        // 2. Set up the startup screen stage
        Scene initScene = new Scene(root, 400, 200);
        stage.initStyle(StageStyle.UNDECORATED); // Removes minimize/maximize/close buttons
        stage.setScene(initScene);
        stage.show();
        // 3. Perform heavy startup work in a background thread to prevent the UI from freezing
        Task<Void> startupTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                initializer.initializeData();
//                for (int i = 1; i <= 10; i++) {
//                    Thread.sleep(200); // Simulate background loading tasks
//                    updateMessage("Loading Player Data...");
//                    updateProgress(i, 10);
//                }
                return null;
            }
        };
        // Bind the progress bar and status text to the background task
        progressBar.progressProperty().bind(startupTask.progressProperty());
        statusLabel.textProperty().bind(startupTask.messageProperty());

        // 4. Once background tasks are complete, switch to the main application window
        startupTask.setOnSucceeded(event -> {
            stage.close(); // Close the splash screen
            // Open the main program window
            try {showMainStage(initializer);} catch (IOException e) {throw new RuntimeException(e);}
        });
        new Thread(startupTask).start();
    }
    private void showMainStage(DataInitializer initializer) throws IOException {
        Stage stage = new Stage();

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double usableWidth = visualBounds.getWidth();
        feedFxmlFile = switch ((int) usableWidth) {
            case 1440, 1400 -> "fantasy-feed-1440x900.fxml";
            default -> "fantasy-feed-1920x1080.fxml";
        };//default to 1920x1080

        FXMLLoader fxmlLoader;
        Scene scene;
        FeedController feedController = null;
        UserEntryController entryController = null;
        if(initializer.saveData == null) {
            fxmlLoader = new FXMLLoader(Application.class.getResource("username-entry.fxml"));
            scene = new Scene(fxmlLoader.load(), 557, 336);
            entryController = fxmlLoader.getController();
            entryController.setDataInitializer(initializer);
        }
        else {
            fxmlLoader = new FXMLLoader(Application.class.getResource(feedFxmlFile));
            Parent root = fxmlLoader.load();
            scene = new Scene(root);
            feedController = fxmlLoader.getController();
            feedController.initData(initializer);
        }
        stage.setTitle("Fantasy Football Feed");
        stage.setScene(scene);
        stage.show();
        if(feedController != null)
            listViewWidth = feedController.listFeed.getPrefWidth();
    }
}
