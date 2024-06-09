package com.example.demosystemfront;


import com.example.demosystemfront.Controllers.MenuController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class HelloApplication extends Application {

    Scene scene;
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Menu");

        MenuController menuScene = new MenuController(primaryStage);
        Scene scene = new Scene(menuScene.createContent(), 400, 300);
        primaryStage.setScene(scene);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}