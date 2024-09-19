package com.example.demosystemfront.Controllers;

import com.example.demosystemfront.ApiService;
import com.example.demosystemfront.Controllers.MenuController;
import com.example.demosystemfront.Entities.AccType;
import com.example.demosystemfront.Entities.Booking;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class AddBookingViewController {
    private Stage primaryStage;
    Label labelName = new Label("Imię i nazwisko;");
    TextField textFieldName = new TextField();
    Label labelDepartureDate = new Label("Data wyjazdu;");
    DatePicker datePickerDepartureDate = new DatePicker();
    Label labelArrivalDate = new Label("Data przyjazdu:");
    DatePicker datePickerArrivalDate = new DatePicker();
    Label labelType = new Label("Typ noclegu");
    ComboBox<AccType> comboBox = new ComboBox<>();
    Label labelPhone = new Label("Numer telefonu:");
    TextField textFieldPhone = new TextField();
    Label labelEmail = new Label("Adres email:");
    TextField textFieldEmail = new TextField();
    Label labelInfo = new Label("Dodatkowe informacje:");
    TextArea textAreaInfo = new TextArea();
    ApiService apiService = new ApiService();
    TextField textFieldPrice = new TextField();

    public AddBookingViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void clearFields() {
        textAreaInfo.setText("");
        textFieldPhone.setText("");
        textFieldEmail.setText("");
        textFieldName.setText("");
        datePickerArrivalDate.setValue(null);
        datePickerDepartureDate.setValue(null);
        textFieldPrice.setText("");
    }

    public Scene createContent() {
        Label labelSuccessInfo = new Label("");
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

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
        backButton.getStyleClass().add("button-back");
        backButton.setOnAction(e -> {
            MenuController menuController = new MenuController(primaryStage);
            try {
                primaryStage.setScene(menuController.createContent());
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button checkPriceButton = new Button("Sprawdź cenę");
        Label label = new Label("Cena rezerwacji");

        checkPriceButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Booking booking = new Booking(textFieldName.getText(), datePickerArrivalDate.getValue(), datePickerDepartureDate.getValue(), comboBox.getValue());
                textFieldPrice.setText(apiService.getPrice(booking).toString());
            }
        });

        Button saveButton = new Button("Zapisz");
        saveButton.getStyleClass().add("button-add-entry");
        saveButton.setOnAction(e -> {
            Booking booking = new Booking(textFieldName.getText(), datePickerArrivalDate.getValue(), datePickerDepartureDate.getValue(), comboBox.getValue(), textFieldPhone.getText(), textFieldEmail.getText(), textAreaInfo.getText());
            apiService.saveOneBookingToDatabase(booking);
            labelSuccessInfo.setText("Pomyślnie dodano rezerwacje");
            clearFields();
        });

        layout.getChildren().addAll(backButton, labelName, textFieldName, labelArrivalDate, datePickerArrivalDate,
                labelDepartureDate, datePickerDepartureDate, labelType, comboBox, label, textFieldPrice,
                checkPriceButton, labelEmail, textFieldEmail, labelPhone, textFieldPhone, labelInfo, textAreaInfo,
                saveButton, labelSuccessInfo);

        // Wrap the VBox in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true); // Ensures the content width is always equal to the scroll pane width
        scrollPane.setPadding(new Insets(10));

        Scene scene = new Scene(scrollPane, 500, 700);
        //scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/newStyle.css").toExternalForm());
        return scene;
    }
}
