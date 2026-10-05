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
import javafx.scene.text.*;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.HashMap;

import static com.mmorano.fantasyfootballfeed.UserEntryController.listViewWidth;

public class PlayCell extends ListCell<Play> {
    // Top structural container
    private final VBox mainContainer = new VBox(10);

    // Rows
    private final HBox headerRow = new HBox(10);
    private final VBox participantPane = new VBox(6);

    // Header components
    private final Label TOorTDLabel = new Label();
    private final Label playLabel = new Label();
    private final Label timeLabel = new Label();
    private final Label scoreLabel = new Label();

    // Bottom description
    private final Label playDescriptionLabel = new Label();

    private final Font font = Font.font("Syste", FontWeight.NORMAL, 16);
    private final HashMap<String, String> teams;
    private final User user;
    private final double listWidth = listViewWidth - 36;
    private final DecimalFormat df = new DecimalFormat("0.00");

    public PlayCell(HashMap<String, String> teams, User user) {
        super();
        this.teams = teams;
        this.user = user;

        // Configure header row layouts (Push score to the far right using a Spacer)
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        headerRow.getChildren().addAll(TOorTDLabel, playLabel, timeLabel, spacer, scoreLabel);

        // Configure main outer box style
        mainContainer.setPadding(new Insets(8));
        mainContainer.setMaxWidth(listWidth);
        mainContainer.setPrefWidth(listWidth);
        mainContainer.setBorder(new Border(new BorderStroke(
                Color.GREY, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2)
        )));
        mainContainer.setBackground(new Background(new BackgroundFill(Color.rgb(230, 230, 230), new CornerRadii(5), Insets.EMPTY)));

        // Configure internal participant block styling
        participantPane.setPadding(new Insets(6));
        participantPane.setBackground(new Background(new BackgroundFill(Color.rgb(200, 200, 200), new CornerRadii(5), Insets.EMPTY)));

        // Wrap layout together structurally
        mainContainer.getChildren().addAll(headerRow, participantPane, playDescriptionLabel);

        // Prevent description text wrapping overlap bugs
        playDescriptionLabel.setWrapText(true);
        playDescriptionLabel.setMaxWidth(listWidth);
    }

    @Override
    protected void updateItem(Play play, boolean empty) {
        super.updateItem(play, empty);

        if (empty || play == null) {
            setText(null);
            setGraphic(null);
            setStyle("");
        } else {
            // 1. Turnover / TD Logic Reset
            setDefaults(TOorTDLabel);
            if (!play.isTurnover()) {
                TOorTDLabel.setVisible(false);
                TOorTDLabel.setManaged(false); // Prevents layout engine reserving invisible layout space
            } else {
                TOorTDLabel.setVisible(true);
                TOorTDLabel.setManaged(true);
                // Ensure text is filled if you customize turnover text strings
            }

            // 2. Map Text Metrics safely
            setDefaults(playLabel);
            playLabel.setText(play.getPlayType().getText());

            setDefaults(timeLabel);
            timeLabel.setText(MessageFormat.format("(Q{0} {1})", play.getPeriod(), play.getClockTime()));

            setDefaults(scoreLabel);
            scoreLabel.setText(MessageFormat.format("{0} {1}-{2} {3}", play.getHomeAbbrev(), play.getHomeScore(), play.getAwayScore(), play.getAwayAbbrev()));

            // 3. Rebuild inner lists cleanly
            buildParticipantPane(play.getParticipants());

            // 4. Description mapping
            setDefaults(playDescriptionLabel);
            playDescriptionLabel.setText(MessageFormat.format("{0} - {1}", play.getDownDistanceText(), play.getText()));

            // Attach root UI graphic component safely
            setGraphic(mainContainer);
        }
    }

    private void buildParticipantPane(HashMap<String, Participant> participants) {
        // CRITICAL FIX: Erase nodes from prior cell states before drawing new entries
        participantPane.getChildren().clear();

        if (participants == null || participants.isEmpty()) {
            participantPane.setVisible(false);
            participantPane.setManaged(false);
            return;
        }

        participantPane.setVisible(true);
        participantPane.setManaged(true);

        participants.forEach((id, participant) -> {
            Player player = participant.getPlayer();

            // Row Container for individual player card details
            HBox playerRow = new HBox(12);
            playerRow.setAlignment(Pos.CENTER_LEFT);
            playerRow.setPadding(new Insets(4, 0, 4, 0));

            // Image profile node setup
            ImageView playerImage = new ImageView(new Image(player.getPlayerImage(), true));
            playerImage.setFitHeight(60);
            playerImage.setFitWidth(60);
            playerImage.setPreserveRatio(true);
            playerImage.setSmooth(true);

            // Data descriptor block
            Text playerNameText = new Text(player.getPlayerName());
            setDefaults(playerNameText);
            playerNameText.setStyle("-fx-font-weight: bold;");
            Text teamPosText = new Text(MessageFormat.format("{0} - {1}", teams.get(player.getTeamId()), player.getPosition()));
            setDefaults(teamPosText);
            TextFlow playerDetailsText = new TextFlow(playerNameText, new Text("\n"), teamPosText);

            Region middleSpacer = new Region();
            HBox.setHgrow(middleSpacer, Priority.ALWAYS);

            // Scoring change labels (Push score tracking values rightward safely)
            Text pointsChangeText = new Text(
                    MessageFormat.format("{0}{1}", participant.getScoreChange() >= 0 ? "+" : "", df.format(participant.getScoreChange()))
            );
            setDefaults(pointsChangeText);
            if(participant.getScoreChange() > 0)
                pointsChangeText.setFill(Color.GREEN);
            else if(participant.getScoreChange() < 0)
                pointsChangeText.setFill(Color.RED);
            else
                pointsChangeText.setFill(Color.BLACK);
            pointsChangeText.setStyle("-fx-font-weight: bold;");
            Text ptsText = new Text("PTS");
            setDefaults(ptsText);
            TextFlow playPointsText = new TextFlow(pointsChangeText, new Text("\n"), ptsText);
            playPointsText.setTextAlignment(TextAlignment.CENTER);

            Text scoreText = new Text(String.valueOf(df.format(player.getScore())));
            setDefaults(scoreText);
            scoreText.setStyle("-fx-font-weight: bold;");
            Text totalText = new Text("TOTAL");
            setDefaults(totalText);
            TextFlow totalScoreText = new TextFlow(scoreText, new Text("\n"), totalText);
            totalScoreText.setTextAlignment(TextAlignment.CENTER);

            // Build layout structure
            playerRow.getChildren().addAll(playerImage, playerDetailsText, middleSpacer, playPointsText, totalScoreText);
            participantPane.getChildren().add(playerRow);
        });
    }

    private void setDefaults(Label label) {
        label.setFont(font);
        label.setPrefHeight(Region.USE_COMPUTED_SIZE);
        label.setPrefWidth(Region.USE_COMPUTED_SIZE);
    }
    private void setDefaults(Text text){
        text.setFont(font);
        text.prefHeight(Region.USE_COMPUTED_SIZE);
        text.prefWidth(Region.USE_COMPUTED_SIZE);
    }
}
