package com.mmorano.fantasyfootballfeed.PlayFeed;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

public class PlayCell extends ListCell<Play> {
    private final AnchorPane anchorPane = new AnchorPane();
    private final Label titleLabel = new Label();
    private final Button actionButton = new Button("Action");

    public PlayCell() {
        super();
        // Setup layout constraints for the elements inside the AnchorPane
        AnchorPane.setLeftAnchor(titleLabel, 10.0);
        AnchorPane.setTopAnchor(titleLabel, 5.0);

        AnchorPane.setRightAnchor(actionButton, 10.0);
        AnchorPane.setTopAnchor(actionButton, 2.0);

        // Add children to the template layout
        anchorPane.getChildren().addAll(titleLabel, actionButton);
        anchorPane.setPrefHeight(Region.USE_COMPUTED_SIZE);

        // Optional: Attach logic or click handlers directly inside the cell
        actionButton.setOnAction(event -> {
            Play item = getItem();
            System.out.println("Button clicked for item: " + item.getId());
        });
    }

    @Override
    protected void updateItem(Play item, boolean empty) {
        super.updateItem(item, empty);

        // If the row is empty, render nothing to keep the view clean
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Bind your data model values to the UI controls
            titleLabel.setText(item.getId());

            // Set the AnchorPane as the physical visual node of this cell
            setGraphic(anchorPane);
        }
    }
}

