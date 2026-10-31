package com.mmorano.fantasyfootballfeed.UI;

import com.mmorano.fantasyfootballfeed.Fantasy.FilterOptions;
import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.League;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;

import java.util.ArrayList;

public class LeagueButtonBuilder {

    private static final double leagueSpacer = 20;
    private static final double leagueHeight = 64;
    private static double buttonY = 501;
    private static final Font font = Font.font("System", FontWeight.NORMAL, 24);

    private static String borderOn = "-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 50; -fx-background-radius: 50;";
    private static String borderOff = "-fx-border-color: black; -fx-border-width: 0; -fx-border-radius: 50; -fx-background-radius: 50;";

    public static ArrayList<Button> getLeagueButton(ArrayList<League> leagues, FilterOptions options){
        ArrayList<Button> buttons = new ArrayList<>();
        for(League league : leagues){
            //Button button = new Button(league.getLeagueName());
            Button button = new Button(league.getLeagueName());
            button.setFont(font);
            button.setStyle(borderOn);

            ImageView image = new ImageView(league.getLeagueImage());
            image.setFitHeight(48);
            image.setFitWidth(48);

            javafx.scene.shape.Rectangle clip2 = new Rectangle(image.getFitWidth(), image.getFitHeight());
            clip2.setArcWidth(30);   // Adjust for desired corner roundness
            clip2.setArcHeight(30);
            image.setClip(clip2);

            button.setGraphic(image);
            button.setLayoutX(14);
            button.setLayoutY(buttonY);
            button.setPrefHeight(Region.USE_COMPUTED_SIZE);
            button.setPrefWidth(Region.USE_COMPUTED_SIZE);

            double btnWitdh = switch ((int) Screen.getPrimary().getVisualBounds().getWidth()) {
                case 1440, 1400 -> 326;
                default -> 350;//1920x1080
            };

            button.setMaxWidth(btnWitdh);
            button.setFocusTraversable(false);
            buttonY += leagueHeight + leagueSpacer;

            button.setOnAction(Event -> {
                if(options.getLeagueIds().contains(league.getLeagueId())){
                    button.setStyle(borderOff);
                    options.getLeagueIds().remove(league.getLeagueId());
                } else {
                    button.setStyle(borderOn);
                    options.getLeagueIds().add(league.getLeagueId());
                }
            });
            buttons.add(button);
        }
        return buttons;
    }
}
