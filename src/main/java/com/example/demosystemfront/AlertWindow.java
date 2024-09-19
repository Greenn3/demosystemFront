package com.example.demosystemfront;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class AlertWindow {

    Alert serverConnectionError = new Alert(Alert.AlertType.ERROR);

    public Alert getServerConnectionError() {
        serverConnectionError.setContentText("Brak połączenia z serwerem");
        serverConnectionError.setHeaderText("Error!");

        serverConnectionError.show();
        return serverConnectionError;
    }

    public void setServerConnectionError(Alert serverConnectionError) {
        this.serverConnectionError = serverConnectionError;
    }

    public void showNotification(String message, int seconds) {
        // Create a new stage for the notification
        Stage notificationStage = new Stage();
        notificationStage.initStyle(StageStyle.TRANSPARENT); // Transparent window

        // Ensure the notification is always on top
        notificationStage.setAlwaysOnTop(true);

        // Create a label for the notification message
        Label label = new Label(message);
        label.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8); -fx-text-fill: white; -fx-padding: 10px;");
        label.setAlignment(Pos.CENTER);

        // Create the layout and set the scene
        StackPane pane = new StackPane(label);
        pane.setStyle("-fx-background-radius: 10; -fx-border-radius: 10;");
        Scene scene = new Scene(pane, 250, 100);
        scene.setFill(null);  // Makes the background of the stage transparent

        notificationStage.setScene(scene);

        // Set the position of the notification (e.g., bottom-right of the screen)
        notificationStage.setX(800);
        notificationStage.setY(500);

        // Show the notification stage
        notificationStage.show();

        // Use PauseTransition to hide the stage after a few seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
        delay.setOnFinished(event -> notificationStage.close());
        delay.play();
    }
}
