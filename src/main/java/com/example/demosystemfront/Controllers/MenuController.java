package com.example.demosystemfront.Controllers;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

public class MenuController {
    private Stage primaryStage;

    public MenuController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public VBox createContent() {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: lightblue");

        // Create buttons to switch between scenes
        Button scene1Button = new Button("Nowa rezerwacja");
        scene1Button.setOnAction(e -> {
            AddBookingViewController addBookingViewController = new AddBookingViewController(primaryStage);
            primaryStage.setScene(addBookingViewController.createContent());
        });

        Button scene2Button = new Button("Kalendarz");
        scene2Button.setOnAction(e -> {
            CalendarViewController calendarViewController = new CalendarViewController(primaryStage);
            try {
                primaryStage.setScene(calendarViewController.createContent());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button scene3Button = new Button("Cennik");
        scene3Button.setOnAction(e -> {
            PriceListViewController priceListViewController = new PriceListViewController(primaryStage);
            primaryStage.setScene(priceListViewController.createContent());
        });

        layout.getChildren().addAll(scene1Button, scene2Button, scene3Button);
        return layout;
    }
}
