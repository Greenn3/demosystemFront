package com.example.demosystemfront.Controllers;

import com.example.demosystemfront.AlertWindow;
import com.example.demosystemfront.ApiService;
import com.example.demosystemfront.Entities.AccType;
import com.example.demosystemfront.Entities.PricePerType;
import com.example.demosystemfront.Entities.PricePeriod;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.ConnectException;
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

AlertWindow alertWindow = new AlertWindow();

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
        try{
            accTypes = apiService.loadAllAccTypes();
            pricePerTypeList = apiService.loadAllPricePerType();
            pricePeriods = apiService.loadAllPricePeriods();
        } catch (ConnectException e){
            alertWindow.getServerConnectionError();
        }
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
                TextField spare = new TextField();
                spare.setText(" EUR");
                count++;
                int finalRow = row;
                int finalCol = col;
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        System.out.println("Focus lost. Text is: " + textField.getText());
                        // Perform action here
                      //  String sub = textField.
                        //String sub = textField.getText().substring(0, textField.getText().length() - 4);
                        update(textField.getText(), finalRow, finalCol);
                    }
                });


                textField.setPrefHeight(50);
                HBox box = new HBox();
                spare.setPrefHeight(50);
                box.setMaxWidth(150);
                spare.setEditable(false);
                box.setStyle("-fx-border-color: black; -fx-border-width: 2;");

                // Make the borders of textField and spare invisible using CSS
                textField.setStyle("-fx-background-color: white; -fx-border-color: transparent;");
                spare.setStyle("-fx-background-color: white; -fx-border-color: transparent;");
                box.getChildren().addAll(textField, spare);
                mainGrid.add(box, col, row);
            }
        }

        // Create the top header grid (1x5)
        GridPane topHeaderGrid = new GridPane();
        topHeaderGrid.setHgap(2);
        topHeaderGrid.setStyle("-fx-padding: 10px;");

        for (int col = 0; col < 5; col++) {
            TextField textField = new TextField(pricePeriods.get(col).getName());
            textField.setPrefWidth(150);
            textField.setPrefHeight(50);
            textField.setEditable(false);
            topHeaderGrid.add(textField, col, 0);
        }

        // Create the left header grid (5x1)
        GridPane leftHeaderGrid = new GridPane();
        leftHeaderGrid.setVgap(5);
        leftHeaderGrid.setStyle("-fx-padding: 10px;");

        for (int row = 0; row < 6; row++) {
            TextField textField = new TextField(accTypes.get(row).getName());
            textField.setPrefWidth(200);
            textField.setPrefHeight(50);
            textField.setEditable(false);
            leftHeaderGrid.add(textField, 0, row);
        }
        Button button = new Button();
        button.setText("Zapisz");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    apiService.updatePriceListToDataBase(pricePerTypeList);

                    //błąd połączenia z serwerem
                } catch (ConnectException e){
                alertWindow.getServerConnectionError();
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
        Scene scene = new Scene(root, 700, 800);

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
       // layout.setStyle("-fx-background-color: lightyellow");

        Button backButton = new Button("<");
        backButton.setOnAction(e -> {
            MenuController menuController = new MenuController(primaryStage);
            try {
                primaryStage.setScene(menuController.createContent());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
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
        //layout.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        layout.getStylesheets().add(getClass().getResource("/newStyle.css").toExternalForm());
        return new Scene(layout, 1000, 500);
    }
}
