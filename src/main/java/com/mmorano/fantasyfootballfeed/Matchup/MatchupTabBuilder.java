package com.mmorano.fantasyfootballfeed.Matchup;

import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.Fantasy.Sleeper.League;
import com.mmorano.fantasyfootballfeed.PlayFeed.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Screen;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MatchupTabBuilder {

    private static final double imageSize = 48;
    private static final Font font = Font.font("System", FontWeight.NORMAL, 36);
    private static HashMap<String, Double> playerScores;

    public static ArrayList<Tab> getMatchupTabs(ArrayList<League> leagues, HashMap<String, Double> scores, ArrayList<Event> events){
        ArrayList<Tab> tabs = new ArrayList<>();
        tabs.add(getEventsTab(leagues, events));
        playerScores = scores;
        for(League league : leagues){
            Tab tab = new Tab(league.getLeagueName());
            tab.setStyle("-fx-font-size: 12px;");
            ImageView icon = new ImageView(league.getLeagueImage());
            icon.setFitHeight(48);
            icon.setFitWidth(48);
            Rectangle clip = new Rectangle(icon.getFitWidth(), icon.getFitHeight());
            clip.setArcWidth(30);
            clip.setArcHeight(30);
            icon.setClip(clip);
            tab.setGraphic(icon);

            VBox main = new VBox(12);

            main.getChildren().add(getLeagueLabel(league));
            main.getChildren().add(getMatchupHeader(league));
            for(int i = 0; i < league.getPlayers().size(); i++){
                main.getChildren().add(getPlayerRow(league.getPlayers().get(i), league.getMatchup().getPlayers().get(i), league.getRosterPositions()[i], league.getLeagueId()));
            }

            main.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));

            tab.setContent(main);
            tabs.add(tab);

        }

        return tabs;
    }

    private static Tab getEventsTab(ArrayList<League> leagues, ArrayList<Event> events){
        Tab tab = new Tab("This Weeks Games");
        tab.setStyle("-fx-font-size: 18px;");
        VBox vBox = new VBox(12);

        events.sort(Comparator.comparing(Event::getStartTime));

        LocalDateTime time = LocalDateTime.MIN;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US);
        Font font = Font.font("System", FontWeight.NORMAL, 18);

        for(Event event : events){
            ArrayList<Player> players = new ArrayList<>(leagues.stream().flatMap(x -> x.getPlayers().stream()).distinct().toList());
            int homeCount = 0, awayCount = 0;
            for(Player player : players){
                if(event.getHomeTeamId().equals(player.getTeamId()))
                    homeCount++;
                else if(event.getAwayTeamId().equals(player.getTeamId()))
                    awayCount++;
            }

            if(!time.isEqual(event.getStartTime())){
                time = event.getStartTime();
                Label timeLabel = new Label(MessageFormat.format("{0} {1}", time.getDayOfWeek(), time.format(formatter)));
                timeLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
                timeLabel.setUnderline(true);
                vBox.getChildren().add(timeLabel);
            }

            Label eventLabel = new Label(MessageFormat.format("\t({0}) {1} VS {2} ({3})",
                    homeCount, event.getHomeTeamAbbrev(), event.getAwayTeamAbbrev(), awayCount));
            eventLabel.setFont(font);
            vBox.getChildren().add(eventLabel);
        }
        tab.setContent(vBox);
        return tab;
    }

    private static HBox getPlayerRow(Player p1, Player p2, String pos, String leagueId){
        HBox hBox = new HBox(12);
        hBox.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));

        ImageView p1Image;
        ImageView p2Image;
        p1Image = p1.getPlayerImage() == null ? new ImageView() : new ImageView(p1.getPlayerImage());
        p1Image.setPreserveRatio(true);
        //p1Image.setFitWidth(48);
        p1Image.setFitHeight(48);
        p2Image = p2.getPlayerImage() == null ? new ImageView() : new ImageView(p2.getPlayerImage());
        p2Image.setPreserveRatio(true);
        //p2Image.setFitWidth(48);
        p2Image.setFitHeight(48);
        Text t1 = new Text(p1.getPlayerName());
        t1.setFont(Font.font("System", FontWeight.NORMAL, 24));
        Text t2 = new Text(p2.getPlayerName());
        t2.setFont(Font.font("System", FontWeight.NORMAL, 24));
        Text s1 = new Text(String.valueOf(playerScores.get(leagueId + p1.getSleeperId())));
        s1.setFont(Font.font("System", FontWeight.SEMI_BOLD, 24));
        Text s2 = new Text(String.valueOf(playerScores.get(leagueId + p2.getSleeperId())));
        s2.setFont(Font.font("System", FontWeight.SEMI_BOLD, 24));
        StackPane posPane = getPositionSquare(pos);
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        hBox.getChildren().addAll(p1Image, t1, spacer1, s1, posPane, s2, spacer2, t2, p2Image);
        return hBox;
    }

    private static HBox getMatchupHeader(League league){
        HBox root = new HBox(0);
        root.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));

        HBox hBoxLeft = new HBox(12);
        hBoxLeft.getChildren().addAll(
                getTeamImage(league.getAvatar()),
                getTeamLabel(league.getTeamName()),
                getFullSPacer(),
                getTeamScoreLabel(league.getLeagueId(), league.getPlayers())
        );

        HBox hBoxCenter = new HBox(12);
        hBoxCenter.getChildren().addAll(
                getCenterSpacer(),
                getTeamImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRix9FWtPrlvA9V-XjjYkaZBb1_HxA6u-gtjrr2Z8yE-Q&s=10"),
                getCenterSpacer()
        );

        HBox hBoxRight = new HBox(12);
        hBoxRight.getChildren().addAll(
                getTeamScoreLabel(league.getLeagueId(), league.getMatchup().getPlayers()),
                getFullSPacer(),
                getTeamLabel(league.getMatchup().getTeamName()),
                getTeamImage(league.getMatchup().getAvatar())
        );

        HBox.setHgrow(hBoxLeft, Priority.ALWAYS);
        HBox.setHgrow(hBoxRight, Priority.ALWAYS);

        hBoxLeft.setPrefWidth(0);
        hBoxRight.setPrefWidth(0);
        hBoxLeft.setMaxWidth(Double.MAX_VALUE);
        hBoxRight.setMaxWidth(Double.MAX_VALUE);

        hBoxLeft.setAlignment(Pos.CENTER_LEFT);
        hBoxRight.setAlignment(Pos.CENTER_RIGHT);

        root.getChildren().addAll(hBoxLeft, hBoxCenter, hBoxRight);
        return root;
    }

    private static ImageView getTeamImage(String avatar){
        ImageView image = new ImageView(avatar);
        image.setFitHeight(64);
        image.setFitWidth(64);
        return image;
    }

    private static Label getTeamLabel(String teamName){
        Label team = new Label(teamName);
        team.setPrefHeight(64);
        team.setFont(font);
        team.setAlignment(Pos.CENTER_LEFT);
        team.setUnderline(true);
        return team;
    }

    private static Label getTeamScoreLabel(String leagueId, ArrayList<Player> players){
        Label score = new Label(String.format("%.2f", getTeamScore(leagueId, players)));
        score.setPrefHeight(64);
        score.setAlignment(Pos.CENTER_LEFT);
        score.setFont(Font.font("System", FontWeight.BOLD, 32));
        return score;
    }

    private static Region getCenterSpacer(){
        Region spacer = new Region();
        spacer.setPrefWidth(10);
        return spacer;
    }

    private static Region getFullSPacer(){
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
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

    private static StackPane getPositionSquare(String pos){
        Label label = new Label(pos);
        label.setFont(Font.font("System", FontWeight.BOLD, 12));
        StackPane pane = new StackPane();
        Rectangle r = new Rectangle(32,32);
        r.setArcWidth(15);   // Adjust for desired corner roundness
        r.setArcHeight(15);
        switch (pos){
            case "QB":
                r.setFill(Color.RED);
                pane.getChildren().add(r);
                break;
            case "WR":
                r.setFill(Color.LIGHTBLUE);
                pane.getChildren().add(r);
                break;
            case "RB":
                r.setFill(Color.LIGHTGREEN);
                pane.getChildren().add(r);
                break;
            case "TE":
                r.setFill(Color.ORANGE);
                pane.getChildren().add(r);
                break;
            case "FLEX":
                HBox hbox = new HBox();
                hbox.setPrefSize(32,32);
                hbox.setMaxSize(32,32);
                Region r1 = new Region();
                r1.setPrefWidth(32.0/3);
                r1.setPrefHeight(32);
                r1.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(5, 0, 0, 5, false), Insets.EMPTY)));
                Rectangle r2 = new Rectangle(32.0/3, 32);
                r2.setFill(Color.LIGHTGREEN);
                Region r3 = new Region();
                r3.setPrefWidth(32.0/3);
                r3.setPrefHeight(32);
                r3.setBackground(new Background(new BackgroundFill(Color.ORANGE, new CornerRadii(0, 5, 5, 0, false), Insets.EMPTY)));
                HBox.setHgrow(r1, Priority.ALWAYS);
                HBox.setHgrow(r2, Priority.ALWAYS);
                HBox.setHgrow(r3, Priority.ALWAYS);
                hbox.getChildren().addAll(r1, r2, r3);
                pane.getChildren().add(hbox);
                label.setText("WRT");
//                hbox.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(15), Insets.EMPTY)));
//                hbox.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, new CornerRadii(15), BorderWidths.EMPTY)));
                break;
            case "SUPER_FLEX":
                VBox vBox = new VBox();
                vBox.setPrefSize(32,32);
                vBox.setMaxSize(32,32);
                HBox hBox1 = new HBox();
                hBox1.setPrefSize(32,16);
                hBox1.setMaxSize(32,16);
                hBox1.getChildren().addAll(
                        getSFlexRegions(Color.LIGHTBLUE, 5, 0, 0, 0),
                        getSFlexRegions(Color.LIGHTGREEN, 0, 5, 0, 0)
                );
                HBox hBox2 = new HBox();
                hBox2.setPrefSize(32,16);
                hBox2.setMaxSize(32,16);
                hBox2.getChildren().addAll(
                        getSFlexRegions(Color.ORANGE, 0, 0, 0, 5),
                        getSFlexRegions(Color.RED, 0, 0, 5, 0)
                );

                vBox.getChildren().addAll(hBox1, hBox2);
                pane.getChildren().add(vBox);

                label.setText("W  R \nT  Q");
                //label.setAlignment(Pos.CENTER);
                label.setTextAlignment(TextAlignment.CENTER);
                break;
            case "K":
                r.setFill(Color.PURPLE);
                pane.getChildren().add(r);
                break;
            case "DEF":
                r.setFill(Color.BROWN);
                pane.getChildren().add(r);
                break;
        }
        pane.getChildren().add(label);
        return pane;
    }

    private static Region getSFlexRegions(Color color, double topLeft, double topRight, double bottomRight, double bottomLeft){
        Region r = new Region();//top left
        r.setBackground(new Background(new BackgroundFill(color, new CornerRadii(topLeft, topRight, bottomRight, bottomLeft, false), Insets.EMPTY)));
        r.setPrefSize(16, 16);
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }

    private static double getTeamScore(String leagueId, ArrayList<Player> players){
        double total = 0;
        for(Player p : players)
            if(!p.getSleeperId().equals("0"))
                total += playerScores.get(leagueId + p.getSleeperId());
        return total;
    }
}
