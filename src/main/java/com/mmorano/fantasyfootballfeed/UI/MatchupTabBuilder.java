package com.mmorano.fantasyfootballfeed.UI;

import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.League;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class MatchupTabBuilder {

    private static final double imageSize = 48;
    private static final Font font = Font.font("System", FontWeight.NORMAL, 18);

    public static ArrayList<Tab> getMatchupTabs(ArrayList<League> leagues){
        ArrayList<Tab> tabs = new ArrayList<>();

        for(League league : leagues){
            Tab tab = new Tab(league.getLeagueName());
            tab.setStyle("-fx-font-size: 12px;");
            ImageView icon = new ImageView(league.getLeagueImage());
            icon.setFitHeight(48);
            icon.setFitWidth(48);
            Rectangle clip = new Rectangle(icon.getFitWidth(), icon.getFitHeight());
            clip.setArcWidth(30);   // Adjust for desired corner roundness
            clip.setArcHeight(30);
            icon.setClip(clip);
            tab.setGraphic(icon);

            //build tabs
            VBox main = new VBox(12);

            main.getChildren().add(getLeagueLabel(league));
            main.getChildren().add(getMatchupHeader(league));

            main.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));

            tab.setContent(main);
            tabs.add(tab);

        }

        return tabs;
    }

    private static HBox getMatchupHeader(League league){
        HBox hBox = new HBox(12);
        hBox.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));

        return hBox;
    }

    private static Label getLeagueLabel(League league){
        Label leagueLabel = new Label(league.getLeagueName());
        leagueLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 32));
        leagueLabel.setPrefHeight(Region.USE_COMPUTED_SIZE);
        leagueLabel.setMaxWidth(Double.MAX_VALUE);
        leagueLabel.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        leagueLabel.setAlignment(Pos.CENTER);
        return leagueLabel;
    }
}
