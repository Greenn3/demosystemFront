package com.example.demosystemfront.Controllers;

import com.example.demosystemfront.AlertWindow;
import com.example.demosystemfront.ApiService;
import com.example.demosystemfront.Entities.AccType;
import com.example.demosystemfront.Entities.PricePerType;
import com.example.demosystemfront.Entities.PricePeriod;
import com.example.demosystemfront.Menu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpRetryException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class PriceListViewController {
    Menu menu = new Menu();

    private Stage primaryStage;
    ApiService apiService = new ApiService();
    public List<PricePerType> pricePerTypeList = new ArrayList<>();
    public List<AccType> accTypes;
    public List<PricePeriod> pricePeriods;
    public PriceListViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

AlertWindow alertWindow = new AlertWindow();

//    public void testPrintLists(){
//        for(AccType a : accTypes) System.out.print(a.getName()+ "\t") ;
//        System.out.println("\n");
//        for(PricePeriod a : pricePeriods) System.out.print(a.getName() + "\t");
//        System.out.println("\n");
//        int count = 0;
//        for (int i = 0; i<pricePeriods.size(); i++){
//            for(int j = 0; j<accTypes.size(); j++){
//                System.out.print(pricePerTypeList.get(count).getPrice());
//                count++;
//            }
//            System.out.println();
//        }
//    }
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
//        mainGrid.getStyleClass().add("grid-pane");
mainGrid.getStyleClass().add("price-list-grid");
        // Create the main grid (5x5)
        int count = 0;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                int id = Integer.parseInt(String.valueOf(row) + String.valueOf(col));
                TextField textField = new TextField();
                textField.setText(pricePerTypeList.get(count).getPrice().toString());
                TextField spare = new TextField();
//                textField.getStyleClass().add("text-field");
                spare.setText(" PLN");
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
               // box.setStyle("-fx-border-color: black; -fx-border-width: 2;");

                // Make the borders of textField and spare invisible using CSS
             //  spare.setStyle("-fx-background-color: white; -fx-border-color: transparent;");
            //   textField.getStyleClass().add("price-list-box");
             //   spare.getStyleClass().add("price-list-box");
                textField.getStyleClass().add("price");
                spare.getStyleClass().add("currency");
                box.getStyleClass().add("price-list-box");
                box.getChildren().addAll(textField, spare);
                mainGrid.add(box, col, row);
            }
        }

        // Create the top header grid (1x5)
        GridPane topHeaderGrid = new GridPane();
        topHeaderGrid.setHgap(2);
        topHeaderGrid.setStyle("-fx-padding: 10px;");
topHeaderGrid.getStyleClass().add("price-list-header");
        topHeaderGrid.add((new TextField("STY - KWI")) ,0,0);
        topHeaderGrid.add((new TextField("MAJ - CZE")) ,1,0);
        topHeaderGrid.add((new TextField("LIP - SIE")) ,2,0);
        topHeaderGrid.add((new TextField("WRZ")) ,3,0);
        topHeaderGrid.add((new TextField("PAŹ - GRU")) ,4,0);

//        for (int col = 0; col < 5; col++) {
//           // TextField textField = new TextField(pricePeriods.get(col).getName());
//            TextField textField = new TextField();
//            textField.setPrefWidth(150);
//            textField.setPrefHeight(50);
//            textField.setEditable(false);
//            textField.getStyleClass().add("price-list-box");
//
//            topHeaderGrid.add(textField, col, 0);
//        }

        // Create the left header grid (5x1)
        GridPane leftHeaderGrid = new GridPane();
        leftHeaderGrid.setVgap(9);
        leftHeaderGrid.setStyle("-fx-padding: 10px;");



        for (int row = 0; row < 6; row++) {
            TextField textField = new TextField(accTypes.get(row).getName());
            textField.setPrefWidth(200);
            textField.setPrefHeight(50);
            textField.setEditable(false);
            textField.getStyleClass().add("price-list-box");

            leftHeaderGrid.add(textField, 0, row);
        }
        Button button = new Button();
        button.setText("Zapisz");
        button.getStyleClass().add("button-save");


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
      //  button.getStyleClass().add("button-save");

        // Create the main layout
        GridPane root = new GridPane();
        leftHeaderGrid.getStyleClass().add("price-list-left-header");
       // root.getStyleClass().add("price-list-grid");
        root.add(topHeaderGrid, 1, 0);
        root.add(leftHeaderGrid, 0, 1);
        root.add(mainGrid, 1, 1);
        TextField textField = new TextField();
       textField.getStyleClass().add("space");
        root.add(textField, 0,2);
        root.add(button, 0, 3);


return root;

    }


    public Scene createContent() {
        VBox menuBox = menu.showMenu(primaryStage);
        BorderPane mainPane = new BorderPane();
        mainPane.setLeft(menuBox);


        VBox layout = new VBox(10);
       // layout.setStyle("-fx-background-color: lightyellow");
        VBox smallerBox = new VBox();
        smallerBox.getChildren().add(layout);
        smallerBox.getStyleClass().add("smaller-box");
//layout.getStyleClass().add("layout-price-list");
        VBox cover = new VBox();
        cover.getChildren().add(smallerBox);
        cover.getStyleClass().add("cover");
        mainPane.setCenter(cover);
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
        gridPane.getStyleClass().add("price-list-grid");
        layout.getStyleClass().add("layout-price-list");
    //   layout.getChildren().add(backButton);
        layout.getChildren().add(gridPane);
        menuBox.getStyleClass().add("menu-panel");
        mainPane.setTop(menu.showTopPanel());
        //layout.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        Scene scene = new Scene(mainPane, 1200, 750);
        scene.getStylesheets().add(getClass().getResource("/nextStyle.css").toExternalForm());
        return scene;
    }
}
