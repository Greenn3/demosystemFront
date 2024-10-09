package com.example.demosystemfront.Controllers;

import com.example.demosystemfront.ApiService;
import com.example.demosystemfront.Entities.AccType;
import com.example.demosystemfront.Entities.Booking;
import com.example.demosystemfront.HelperPDF;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.scene.control.ScrollPane;
import javafx.util.StringConverter;

public class BookingInfoController {
    HelperPDF helperPDF = new HelperPDF();
    ApiService api = new ApiService();
    Booking booking;
    ComboBox<AccType> comboBox = new ComboBox<>();
    Booking editedBooking;
    private Stage primaryStage;

    public BookingInfoController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene createContent(Integer id) {
        BooleanProperty editable = new SimpleBooleanProperty(false);  // Use BooleanProperty

        System.out.println("id: " + id);
        VBox mainContainer = new VBox();
        mainContainer.setPadding(new Insets(20));  // Add padding around the main container
        mainContainer.setSpacing(15);  // Space between components

        // Back Button
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

        // Toggle Edit Button
        ToggleButton editModeButton = new ToggleButton("Włącz edytowanie");
        editModeButton.getStyleClass().add("toggle-button");
        //Button buttonEdit = new Button("Zmień");
        editModeButton.setOnAction(e -> editable.set(!editable.get()));  // Toggle editable state

        // Fields and Labels
        Label nameLabel = new Label("Nazwisko");
        TextField name = new TextField(booking.name);
        name.editableProperty().bind(editable);

        Label arrivalDateLabel = new Label("Data przyjazdu");
        DatePicker arrivalDatePicker = new DatePicker(booking.arrivalDate);
        arrivalDatePicker.editableProperty().bind(editable);
        arrivalDatePicker.disableProperty().bind(editable.not());

        Label departureDateLabel = new Label("Data wyjazdu");
        DatePicker departureDatePicker = new DatePicker(booking.departureDate);
        departureDatePicker.editableProperty().bind(editable);
        departureDatePicker.disableProperty().bind(editable.not());

        Label accTypeLabel = new Label("Typ noclegu");
       // TextField accType = new TextField(booking.getAccType().getName());
      //  accType.editableProperty().bind(editable);

        // Load AccTypes and ComboBox configuration
        try {
            List<AccType> list = api.loadAllAccTypes();
            comboBox.getItems().addAll(list);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        comboBox.setValue(booking.accType);
        comboBox.editableProperty().bind(editable);
        comboBox.disableProperty().bind(editable.not());

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
        // Set the StringConverter to display the name of the AccType in the ComboBox when selected
        comboBox.setConverter(new StringConverter<AccType>() {
            @Override
            public String toString(AccType object) {
                if (object != null) {
                    return object.getName();  // Display the name of the AccType
                }
                return "";
            }

            @Override
            public AccType fromString(String string) {
                // You can define logic here if you need to convert from string back to AccType,
                // but typically this isn't necessary unless you allow manual text input in the ComboBox.
                return comboBox.getItems().stream()
                        .filter(accType -> accType.getName().equals(string))
                        .findFirst().orElse(null);
            }
        });

        Label priceLabel = new Label("Cena");
        TextField price = new TextField(api.getPrice(booking));
        price.setEditable(false);

        Label discountLabel = new Label("Zniżka");
        CheckBox discount = new CheckBox();
        discount.disableProperty().bind(editable.not());

        Label discountAmountLabel = new Label("Kwota zniżki");
        TextField discountAmount = new TextField();
        discountAmount.editableProperty().bind(editable);

        TextField finalPrice = new TextField();
        finalPrice.editableProperty().bind(editable);

        TextArea addInfo = new TextArea(booking.getInfo());
        addInfo.editableProperty().bind(editable);

        TextField phone = new TextField();
        TextField email = new TextField();
        CheckBox paid = new CheckBox("Zapłacono");
        CheckBox arrived = new CheckBox("Przyjechał");
        CheckBox left = new CheckBox("Wyjechał");

        MenuButton generateDocumentMenu = new MenuButton("Wygeneruj dokument");
        MenuItem confirmation = new MenuItem("Potwierdzenie rezerwacj PDF");
        confirmation.setOnAction(event -> {
            helperPDF.generateConfirmationPDF(booking, primaryStage);
        });
        MenuItem invoice = new MenuItem("Rachunek PDF");
        invoice.setOnAction(event -> {
            helperPDF.generateInvoicePDF(booking,primaryStage);
        });


        generateDocumentMenu.getItems().addAll(confirmation, invoice);

        // Save Button
        Button save = new Button("Zapisz zmiany");
        save.setOnAction(e -> {
            Booking editedBooking2 = new Booking(id,
                    name.getText(),
                    arrivalDatePicker.getValue(),
                    departureDatePicker.getValue(),
                    comboBox.getValue(), addInfo.getText(),
                    Double.parseDouble(discountAmount.getText()),
                    phone.getText(),
                    email.getText(),
                    arrived.isSelected(),
                    paid.isSelected(),
                    left.isSelected());
            api.saveOneBookingToDatabase(editedBooking2);
        });

        // Layout Structure
        HBox header = new HBox(10, backButton, editModeButton);
        header.setAlignment(Pos.CENTER_LEFT);  // Align back button and edit button to the left

        VBox nameSection = new VBox(5, nameLabel, name);
        VBox arrivalSection = new VBox(5, arrivalDateLabel, arrivalDatePicker);
        VBox departureSection = new VBox(5, departureDateLabel, departureDatePicker);
        VBox accTypeSection = new VBox(5, accTypeLabel,  comboBox);
        VBox priceSection = new VBox(5, priceLabel, price);
        VBox discountSection = new VBox(5, discountLabel, discount, discountAmountLabel, discountAmount);
        VBox additionalInfoSection = new VBox(5, new Label("Dodatkowe informacje"), addInfo);
        VBox checkBoxSection = new VBox(5, paid, arrived, left);

        // Group sections into main layout
        mainContainer.getChildren().addAll(
                header,
                save,
                nameSection,
                arrivalSection, departureSection,
                accTypeSection,
                priceSection,
                discountSection,
                additionalInfoSection,
                checkBoxSection,
                generateDocumentMenu
        );

        // Add ScrollPane to allow scrolling
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);  // Make ScrollPane fit to the width of the window

        Scene scene = new Scene(scrollPane, 500, 500);
        scene.getStylesheets().add(getClass().getResource("/newStyle.css").toExternalForm());
        return scene;
    }
}
