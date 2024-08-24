package com.example.demosystemfront.Controllers;

import com.example.demosystemfront.ApiService;
import com.example.demosystemfront.Entities.Booking;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookingInfoController {
    ApiService api = new ApiService();
    Booking booking;

    private Stage primaryStage;
    public BookingInfoController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public Scene createContent(Integer id){
        BooleanProperty editable = new SimpleBooleanProperty(false);  // Use BooleanProperty

        System.out.println("id: " + id);
        VBox vBox = new VBox();
        Button backButton = new Button("<");
        backButton.getStyleClass().add("button-back");
        backButton.setOnAction(e -> {
            MenuController menuController = new MenuController(primaryStage);
            try {
                primaryStage.setScene(menuController.createContent());
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        try {
            booking = api.findBookingById(id);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println(booking.toString());

        Button buttonEdit = new Button("Zmień");
        buttonEdit.setOnAction(e -> editable.set(!editable.get()));  // Toggle editable state

        Label nameLabel = new Label("Nazwisko");
        TextField name = new TextField();
        name.setText(booking.name);
        name.editableProperty().bind(editable);  // Bind editable property

        Label arrivalDateLabel = new Label("Data przyjazdu");
        DatePicker arrivalDatePicker = new DatePicker();
        arrivalDatePicker.setValue(booking.arrivalDate);
        arrivalDatePicker.editableProperty().bind(editable);  // Bind editable property

        Label departureDateLabel = new Label("Data wyjazdu");
        DatePicker departureDatePicker = new DatePicker();
        departureDatePicker.setValue(booking.departureDate);
        departureDatePicker.editableProperty().bind(editable);  // Bind editable property

        Label accTypeLabel = new Label("Typ noclegu");
        TextField accType = new TextField();
        accType.setText(booking.getAccType().getName());
        accType.editableProperty().bind(editable);  // Bind editable property

        Label priceLabel = new Label("Cena");
        TextField price = new TextField();
        price.setText(api.getPrice(booking));
        price.setEditable(false);

        Label discountLabel = new Label("Zniżka");
        CheckBox discount = new CheckBox();
        discount.disableProperty().bind(editable.not());  // Disable when not editable

        Label discountAmountLabel = new Label("Kwota zniżki");
        TextField discountAmount = new TextField();
        discountAmount.editableProperty().bind(editable);  // Bind editable property

        TextField finalPrice = new TextField();
        finalPrice.editableProperty().bind(editable);  // Bind editable property

        TextArea addInfo = new TextArea();
        addInfo.setText(booking.getInfo());
        addInfo.editableProperty().bind(editable);  // Bind editable property

        vBox.getChildren().addAll(
                backButton, buttonEdit, name, arrivalDateLabel, arrivalDatePicker,
                departureDateLabel, departureDatePicker, accTypeLabel, accType,
                priceLabel, price, discountLabel, discount, discountAmountLabel,
                discountAmount, finalPrice, addInfo
        );

        Scene scene = new Scene(vBox, 500, 500);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        return scene;
    }

}
