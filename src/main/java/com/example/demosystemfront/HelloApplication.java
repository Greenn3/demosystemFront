package com.example.demosystemfront;


import com.example.demosystemfront.Controllers.MenuController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.time.LocalTime;

public class HelloApplication extends Application {

    Scene scene;
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Recepcja");
        Application.setUserAgentStylesheet("nord-light.css");

        MenuController menuScene = new MenuController(primaryStage);
        Scene scene = menuScene.createContent();
      //  scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        primaryStage.setScene(scene);
//primaryStage.setFullScreen(true);
        primaryStage.show();
        startApiCheckTimer();
    }


    private void startApiCheckTimer() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> {
                    // Update connection status
                    String status = Menu.api.checkApi();
                    Menu.getConnectionStatus().setText(status);

                    // Update time label
                    Menu.getTimeLabel().setText(LocalTime.now().toString());
                }),
                new KeyFrame(Duration.seconds(1)) // Repeat every 10 seconds
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    public static void main(String[] args) {
        launch();
    }
}