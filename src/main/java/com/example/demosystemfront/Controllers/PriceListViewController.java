package com.example.demosystemfront.Controllers;

import com.example.demosystemfront.ApiService;
import com.example.demosystemfront.Entities.AccType;
import com.example.demosystemfront.Entities.PricePerType;
import com.example.demosystemfront.Entities.PricePeriod;
import com.example.demosystemfront.LocalDateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PriceListViewController {
    private Stage primaryStage;
    ApiService apiService = new ApiService();
    public List<PricePerType> pricePerTypeList = new ArrayList<>();
    public List<AccType> accTypes;
    public List<PricePeriod> pricePeriods;
    public PriceListViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }



    public void testPrintLists(){
        for(AccType a : accTypes) System.out.print(a.getName()+ "\t") ;
        System.out.println("\n");
        for(PricePeriod a : pricePeriods) System.out.print(a.getName() + "\t");
        System.out.println("\n");
        int count = 0;
        for (int i = 0; i<pricePeriods.size(); i++){
            for(int j = 0; j<accTypes.size(); j++){
                System.out.print(pricePerTypeList.get(count).getPrice());
                count++;
            }
            System.out.println();
        }
    }
    public void update(String value, int row, int col){
        int index = row*5 + col;
        pricePerTypeList.get(index).setPrice(Double.parseDouble(value));
    }
    @FXML
    protected GridPane showPriceListScreen() throws IOException, InterruptedException {
        accTypes = apiService.loadAllAccTypes();
        pricePerTypeList = apiService.loadAllPricePerType();
        pricePeriods = apiService.loadAllPricePeriods();
        GridPane mainGrid = new GridPane();
        mainGrid.setHgap(2);
        mainGrid.setVgap(2);
        mainGrid.setStyle("-fx-padding: 10px;");

        // Create the main grid (5x5)
        int count = 0;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                int id = Integer.parseInt(String.valueOf(row) + String.valueOf(col));
                TextField textField = new TextField();
                textField.setText(pricePerTypeList.get(count).getPrice().toString());
                count++;
                int finalRow = row;
                int finalCol = col;
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        System.out.println("Focus lost. Text is: " + textField.getText());
                        // Perform action here
                        update(textField.getText(), finalRow, finalCol);
                    }
                });

                //  PricePerType pricePerType = new PricePerType(id, pricePeriods.get(col), accTypes.get(row), Double.parseDouble(textField.getText()));

                //pricePerTypeList.add(pricePerType);
                textField.setPrefWidth(100);
                textField.setPrefHeight(50);
                mainGrid.add(textField, col, row);
            }
        }

        // Create the top header grid (1x5)
        GridPane topHeaderGrid = new GridPane();
        topHeaderGrid.setHgap(2);
        topHeaderGrid.setStyle("-fx-padding: 10px;");

        for (int col = 0; col < 5; col++) {
            TextField textField = new TextField(pricePeriods.get(col).getName());
            textField.setPrefWidth(100);
            textField.setPrefHeight(50);
            topHeaderGrid.add(textField, col, 0);
        }

        // Create the left header grid (5x1)
        GridPane leftHeaderGrid = new GridPane();
        leftHeaderGrid.setVgap(5);
        leftHeaderGrid.setStyle("-fx-padding: 10px;");

        for (int row = 0; row < 6; row++) {
            TextField textField = new TextField(accTypes.get(row).getName());
            textField.setPrefWidth(100);
            textField.setPrefHeight(50);
            leftHeaderGrid.add(textField, 0, row);
        }
        Button button = new Button();
        button.setText("Zapisz");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    apiService.updatePriceListToDataBase(pricePerTypeList);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Create the main layout
        GridPane root = new GridPane();
        root.add(topHeaderGrid, 1, 0);
        root.add(leftHeaderGrid, 0, 1);
        root.add(mainGrid, 1, 1);
        root.add(button, 0, 2);

        // Create the scene
        Scene scene = new Scene(root, 700, 400);

        // Set the scene to the stage
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle("GridPane Example");
       // primaryStage.show();
        testPrintLists();
return root;

    }


    public Scene createContent() {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: lightyellow");

        Button backButton = new Button("<");
        backButton.setOnAction(e -> {
            MenuController menuController = new MenuController(primaryStage);
            primaryStage.setScene(new Scene(menuController.createContent(), 400, 300));
        });
        GridPane gridPane = null;
        try {
            gridPane = showPriceListScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        layout.getChildren().add(backButton);
        layout.getChildren().add(gridPane);
        return new Scene(layout, 600, 600);
    }
}
