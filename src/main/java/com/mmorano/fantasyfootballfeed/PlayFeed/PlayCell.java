package com.mmorano.fantasyfootballfeed.PlayFeed;
import com.mmorano.fantasyfootballfeed.Fantasy.Player;
import com.mmorano.fantasyfootballfeed.Fantasy.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.text.MessageFormat;
import java.util.HashMap;

import static com.mmorano.fantasyfootballfeed.UserEntryController.listViewWidth;

public class PlayCell extends ListCell<Play> {
    private final AnchorPane anchorPane = new AnchorPane();

    private final Label playLabel = new Label();
    private final Label timeLabel = new Label();
    private final Label scoreLabel = new Label();
    private final Label TOorTDLabel = new Label();
    private final Label playDescriptionLabel = new Label();
    private final AnchorPane participantPane = new AnchorPane();
    private final Font font = Font.font(16);
    private final double anchorValue = 4d;
    private final HashMap<String, String> teams;
    private final User user;
    private final double listWidth = listViewWidth - 14;

    public PlayCell(HashMap<String, String> teams, User user) {
        super();
        this.teams = teams;
        this.user = user;
        AnchorPane.setRightAnchor(scoreLabel, anchorValue);

        // Add children to the template layout
        anchorPane.getChildren().addAll(TOorTDLabel, playLabel, timeLabel, scoreLabel, playDescriptionLabel, participantPane);
        anchorPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        anchorPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        anchorPane.setBorder(new Border(new BorderStroke(
                Color.GREY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2)
        )));
        anchorPane.setBackground(new Background(new BackgroundFill(Color.rgb(230, 230, 230), new CornerRadii(5), Insets.EMPTY)));

    }

    @Override
    protected void updateItem(Play play, boolean empty) {
        super.updateItem(play, empty);

        // If the row is empty, render nothing to keep the view clean
        if (empty || play == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Bind your data model values to the UI controls
            setDefaults(TOorTDLabel);
            if(!play.isTurnover()) {
                TOorTDLabel.setVisible(false);
                TOorTDLabel.setPrefWidth(0);
            }

            //Play Type Label
            setDefaults(playLabel);
            playLabel.setText(play.getPlayType().getText());
            playLabel.setLayoutX(getRightAdjacentPosition(TOorTDLabel, (play.isScoringPlay() || play.isTurnover()) ? 10 : 4));
            playLabel.setLayoutY(anchorValue);

            //Game Time Label
            setDefaults(timeLabel);
            timeLabel.setText(MessageFormat.format("(Q{0} {1})", play.getPeriod(), play.getClockTime()));
            timeLabel.setLayoutX(getRightAdjacentPosition(playLabel, 10));
            timeLabel.setLayoutY(anchorValue);

            //Score label
            setDefaults(scoreLabel);
            scoreLabel.setText(MessageFormat.format("{0} {1}-{2} {3}", play.getHomeAbbrev(), play.getHomeScore(), play.getAwayScore(), play.getAwayAbbrev()));
            scoreLabel.setLayoutY(anchorValue);

            getParticipantPane(play.getParticipants());

            //Set play description
            setDefaults(playDescriptionLabel);
            playDescriptionLabel.setText(MessageFormat.format("{0} - {1}", play.getDownDistanceText(), play.getText()));
            playDescriptionLabel.setLayoutX(4);
            playDescriptionLabel.setLayoutY(getSubjacentPosition(participantPane, new Label(" \n "), play.getParticipants().size(), 10));
            playDescriptionLabel.setWrapText(true);
            playDescriptionLabel.setMaxWidth(listWidth - 30);

            // Set the AnchorPane as the physical visual node of this cell
            setGraphic(anchorPane);
        }
    }

    private void getParticipantPane(HashMap<String, Participant> participants){
        participantPane.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200), new CornerRadii(5), Insets.EMPTY)));
        participantPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        participantPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        participantPane.setLayoutX(9);
        participantPane.setLayoutY(getSubjacentPosition(playLabel, 0));

        double paneSize = new Text(" \n ").getLayoutBounds().getHeight();
        final int[] count = {0};
        participants.forEach((id,participant) -> {
            Player player = participant.getPlayer();
            AnchorPane pane = new AnchorPane();
            pane.setPrefWidth(listWidth - 40);
            pane.setLayoutY((paneSize + 20) * count[0]);
            pane.setPrefHeight(Region.USE_COMPUTED_SIZE);

            Label totalLabel = new Label(player.getScore() + "\nTOTAL");
            AnchorPane.setRightAnchor(totalLabel, anchorValue);
            setDefaults(totalLabel);
            totalLabel.setTextAlignment(TextAlignment.CENTER);
            pane.getChildren().add(totalLabel);

            Label playPointLabel = new Label(
                    MessageFormat.format("{0}{1}\nPTS", participant.getScoreChange() >= 0 ? "+" : "", participant.getScoreChange())
            );
            setDefaults(playPointLabel);
            playPointLabel.setLayoutX(getLeftAdjacentPosition(totalLabel, 10));
            playPointLabel.setTextAlignment(TextAlignment.CENTER);
            pane.getChildren().add(playPointLabel);

            ImageView playerImage = new ImageView(new Image("https://a.espncdn.com/i/headshots/nfl/players/full/3918298.png", true));
            AnchorPane.setLeftAnchor(playerImage, anchorValue);
            playerImage.setLayoutY(anchorValue);
            playerImage.setFitHeight(paneSize);
            playerImage.setFitHeight(paneSize);
            playerImage.setPreserveRatio(true);
            playerImage.setSmooth(true);
            pane.getChildren().add(playerImage);

            Label playerLabel = new Label(MessageFormat.format("{0}\n{1} - {2}",
                    player.getPlayerName(), teams.get(player.getTeamId()), player.getPosition()));
            setDefaults(playerLabel);
            playerLabel.setLayoutX(paneSize + anchorValue + 20);
            pane.getChildren().add(playerLabel);

            participantPane.getChildren().add(pane);
            count[0]++;
        });
    }

    private void setDefaults(Label l1){
        l1.setFont(font);
        l1.setPrefHeight(Region.USE_COMPUTED_SIZE);
        l1.setPrefWidth(Region.USE_COMPUTED_SIZE);
    }

    private double getRightAdjacentPosition(Label l1, double spacing){
        Text helperText = new Text(l1.getText());
        helperText.setFont(l1.getFont());
        return l1.getLayoutX() + helperText.getLayoutBounds().getWidth() + spacing;
    }

    private double getLeftAdjacentPosition(Label l1, double spacing){
        Text helperText = new Text(l1.getText());
        helperText.setFont(l1.getFont());
        //4 = anchor pane spacing
        return listWidth - anchorValue - spacing - ((helperText.getLayoutBounds().getWidth() + spacing) * 2);
    }


    private double getSubjacentPosition(AnchorPane pane, Label l1, int participantCount, double spacing){
        Text helperText = new Text(l1.getText());
        helperText.setFont(l1.getFont());
        return pane.getLayoutY() + (helperText.getLayoutBounds().getHeight() * participantCount) + (spacing * participantCount) + spacing;
    }

    private double getSubjacentPosition(Label l1, double spacing){
        Text helperText = new Text(l1.getText());
        helperText.setFont(l1.getFont());
        return l1.getLayoutY() + helperText.getLayoutBounds().getHeight() + spacing;
    }
}

