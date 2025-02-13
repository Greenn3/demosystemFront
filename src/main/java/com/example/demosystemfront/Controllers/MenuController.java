package com.example.demosystemfront.Controllers;

import com.example.demosystemfront.ApiService;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

public class MenuController {
public static ApiService api = new ApiService();


    private static HBox topPanel; // Store the panel as a static instance
    private static Label connectionStatus = new Label();
    private static Label time = new Label();
    public static HBox showTopPanel() {
        // If the panel has already been created, return it
        if (topPanel != null) {
            return topPanel;
        }

        // Otherwise, initialize the panel and labels
        topPanel = new HBox();
        topPanel.getStyleClass().add("top-panel");

        //Label title = new Label("System rezerwacji");
        Label label = new Label("Połączenie z serwerem: ");
        label.getStyleClass().add("top-panel-label-label");
        connectionStatus.setText(api.checkApi());
        if(connectionStatus.getText().equals("OK"))
            connectionStatus.getStyleClass().addAll("top-panel-label-green", "top-panel-label");
        else connectionStatus.getStyleClass().addAll("top-panel-label-red", "top-panel-label");

        connectionStatus.textProperty().addListener((observable, oldValue, newValue) -> {
            // Remove old styles
            connectionStatus.getStyleClass().removeAll("top-panel-label-green", "top-panel-label-red");

            // Add new styles based on the updated text
            if ("OK".equals(newValue)) {
                connectionStatus.getStyleClass().add("top-panel-label-green");
            } else {
                connectionStatus.getStyleClass().add("top-panel-label-red");
            }
        });

        topPanel.getChildren().addAll( label, connectionStatus);
        topPanel.getStylesheets().add("/nextStyle.css");
        return topPanel;
    }

    public static Label getConnectionStatus() {
        return connectionStatus;
    }

    public static Label getTimeLabel() {
        return time;
    }

    public static VBox showMenu(Stage primaryStage) {

        VBox buttonPanel = new VBox(10);
//        buttonPanel.setPrefWidth(200);
//        buttonPanel.setAlignment(Pos.CENTER);
//        buttonPanel.setPadding(new Insets(20, 10, 20, 10));
        buttonPanel.getStyleClass().add("menu-panel");

        Button scene1Button = new Button("Nowa rezerwacja");
        scene1Button.getStyleClass().add("menu-panel-button");
        scene1Button.setOnAction(e -> {
            AddBookingViewController addBookingViewController = new AddBookingViewController(primaryStage);
            primaryStage.setScene(addBookingViewController.createContent());
        });

        Button scene2Button = new Button("Kalendarz");
        scene2Button.getStyleClass().add("menu-panel-button");
        scene2Button.setOnAction(e -> {
            CalendarViewController calendarViewController = new CalendarViewController(primaryStage);
            try {
                primaryStage.setScene(calendarViewController.createContent());
            } catch (IOException | InterruptedException | URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button scene3Button = new Button("Cennik");
        scene3Button.getStyleClass().add("menu-panel-button");
        scene3Button.setOnAction(e -> {
            PriceListViewController priceListViewController = new PriceListViewController(primaryStage);
            primaryStage.setScene(priceListViewController.createContent());
        });

        Button scene4Button = new Button("Wyszukaj rezerwację");
        scene4Button.getStyleClass().add("menu-panel-button");
        scene4Button.setOnAction(e -> {
            FindBookingControler findBookingControler = new FindBookingControler(primaryStage);
            primaryStage.setScene(findBookingControler.createContent());
        });

        Button mainSceneButton = new Button("Strona główna");
        mainSceneButton.getStyleClass().add("menu-panel-button");
        mainSceneButton.setOnAction(e -> {
            MainViewController mainViewController = new MainViewController(primaryStage);
            try {
                primaryStage.setScene(mainViewController.createContent());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonPanel.getChildren().addAll(mainSceneButton, scene1Button, scene2Button, scene3Button, scene4Button);
buttonPanel.getStylesheets().add("/nextStyle.css");
        return buttonPanel;
    }

}
