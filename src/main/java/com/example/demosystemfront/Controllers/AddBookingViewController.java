package com.example.demosystemfront.Controllers;

import com.example.demosystemfront.ApiService;
import com.example.demosystemfront.Entities.AccType;
import com.example.demosystemfront.Entities.Booking;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class AddBookingViewController {
    private Stage primaryStage;
    TextField textFieldName = new TextField();
    DatePicker datePickerDepartureDate = new DatePicker();
    DatePicker datePickerArrivalDate = new DatePicker();
    ComboBox<AccType> comboBox = new ComboBox<>();

    ApiService apiService = new ApiService();

   // List<AccType> accTypes = new ArrayList<>();


    public AddBookingViewController(Stage primaryStage){
       this.primaryStage = primaryStage;
    }


    public Scene createContent()   {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: lightgreen");
        try {
            comboBox.getItems().addAll(apiService.loadAllAccTypes());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        comboBox.setCellFactory(new Callback<ListView<AccType>, ListCell<AccType>>() {
            @Override
            public ListCell<AccType> call(ListView<AccType> param) {
                return new ListCell<AccType>() {
                    @Override
                    protected void updateItem(AccType item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });


        Button backButton = new Button("<");
        backButton.setOnAction(e -> {
            MenuController menuController = new MenuController(primaryStage);
            primaryStage.setScene(new Scene(menuController.createContent(), 400, 300));
        });
        Button checkPriceButton = new Button("Sprawdź cenę");
        Label label = new Label("Cena rezerwacji");
        TextField textFieldPrice = new TextField();
        checkPriceButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Booking booking = new Booking(textFieldName.getText(), datePickerArrivalDate.getValue(), datePickerDepartureDate.getValue(), comboBox.getValue());
                textFieldPrice.setText(apiService.getPrice(booking).toString());

            }
        });


        Button saveButton = new Button("Zapisz");
        saveButton.setOnAction(e -> {
            try {
                Booking booking = new Booking(textFieldName.getText(), datePickerArrivalDate.getValue(), datePickerDepartureDate.getValue(), comboBox.getValue());
                apiService.saveOneBookingToDatabase(booking);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        layout.getChildren().addAll(backButton);
        layout.getChildren().addAll(textFieldName, datePickerArrivalDate, datePickerDepartureDate, comboBox, label, textFieldPrice, checkPriceButton, saveButton);
        return new Scene(layout, 400, 300);
    }



}
