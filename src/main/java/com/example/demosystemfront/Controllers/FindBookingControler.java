package com.example.demosystemfront.Controllers;

import com.example.demosystemfront.ApiService;
import com.example.demosystemfront.Entities.Booking;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class FindBookingControler {

    Stage primaryStage;
    ApiService api = new ApiService();
    ObservableList<Booking> list = FXCollections.observableArrayList();  // Use ObservableList

    // Declare the dynamic search input (will change based on selection)
    Control dynamicSearchField;

    public FindBookingControler(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene createContent() {
        // Back Button
        Button backButton = new Button("<");
        backButton.setOnAction(e -> {
            MenuController menuController = new MenuController(primaryStage);
            try {
                primaryStage.setScene(menuController.createContent());
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        // ComboBox for search criteria
        Label searchLabel = new Label("Search By:");
        ComboBox<String> searchOptions = new ComboBox<>();
        searchOptions.getItems().addAll(
                "Numer rezerwacji",
                "Nazwisko",
                "Data przyjazdu",
                "Data wyjazdu",
                "Status płatności"
        );
        searchOptions.setPromptText("Select Search Option");

        // Placeholder for the dynamic input field
        dynamicSearchField = new TextField();  // Default to TextField
        ((TextField) dynamicSearchField).setPromptText("Enter search value");

        // Change input field based on selected search option
        searchOptions.setOnAction(e -> {
            String selectedOption = searchOptions.getValue();
            updateSearchField(selectedOption);  // Dynamically update search field
        });

        // Button to initiate search
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            // Perform the search action here and update the bookingList
            try {
                performSearch(searchOptions.getValue(), getSearchValue());
            } catch (IOException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        // ListView to display booking results
        Label resultLabel = new Label("Bookings:");
        ListView<Booking> bookingList = new ListView<>(list);  // Bind ListView to ObservableList
        bookingList.setPrefHeight(200);  // Adjust size as needed

        // Layout setup
        VBox layout = new VBox(10);  // Spacing between elements
        layout.getChildren().addAll(
                backButton,
                searchLabel, searchOptions,
                dynamicSearchField,  // Initially a TextField
                searchButton,
                resultLabel, bookingList
        );

        bookingList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {  // Double-click to handle selection
                Booking selectedBooking = bookingList.getSelectionModel().getSelectedItem();
                if (selectedBooking != null) {
                   BookingInfoController bookingInfoController = new BookingInfoController(primaryStage);
                   primaryStage.setScene(bookingInfoController.createContent(selectedBooking.getId()));
                }
            }
        });

        Scene scene = new Scene(layout, 1000, 400);
        return scene;
    }

    // Method to perform search (you would implement this logic)
    private void performSearch(String searchBy, String searchValue) throws IOException, InterruptedException {
        list.clear();  // Clear previous search results
        // Here you can add additional search logic based on "searchBy"
        if(searchBy.equals("Nazwisko")) {
            list.addAll(api.findBookingByName(searchValue));  // Add new results to the ObservableList
        }else if(searchBy.equals("Data przyjazdu")){
            list.addAll(api.findReservationsByArrivalDate(LocalDate.parse(searchValue)));
        }else  if(searchBy.equals("Data wyjazdu")){
            list.addAll(api.findReservationsByDepartureDate(LocalDate.parse(searchValue)));
        }else if(searchBy.equals("Status płatności")){
            if(searchValue.equals("Zapłacone")){
                list.addAll(api.findBookingByPaymentStatus(true));
            }else if(searchValue.equals("Nie zapłacone")){

                list.addAll(api.findBookingByPaymentStatus(false));

            }
            System.out.println("Status płatności");
        }else if(searchBy.equals("Numer rezerwacji")){

            list.addAll(api.findBookingById(Integer.valueOf(searchValue)));
        }
    }

    // Helper function to dynamically update the search field
    private void updateSearchField(String searchOption) {
        VBox parent = (VBox) dynamicSearchField.getParent();  // Get parent VBox
        if (parent != null) {
            parent.getChildren().remove(dynamicSearchField);  // Remove the current search field
        }

        switch (searchOption) {
            case "Data przyjazdu":
            case "Data wyjazdu":
                // Use DatePicker for date-based searches
                dynamicSearchField = new DatePicker();
                break;
            case "Status płatności":
                // Use ComboBox for payment status
                ComboBox<String> statusComboBox = new ComboBox<>();
                statusComboBox.getItems().addAll("Zapłacone", "Nie zapłacone");
                dynamicSearchField = statusComboBox;
                break;
            default:
                // Use TextField for other options
                dynamicSearchField = new TextField();
                ((TextField) dynamicSearchField).setPromptText("Enter search value");
                break;
        }

        // Add the new search field to the layout
        if (parent != null) {
            parent.getChildren().add(2, dynamicSearchField);  // Add the field at the correct position
        }
    }

    // Helper function to get the value from the dynamic search field
    private String getSearchValue() {
        if (dynamicSearchField instanceof TextField) {
            return ((TextField) dynamicSearchField).getText();
        } else if (dynamicSearchField instanceof DatePicker) {
            return ((DatePicker) dynamicSearchField).getValue() != null
                    ? ((DatePicker) dynamicSearchField).getValue().toString()
                    : "";
        } else if (dynamicSearchField instanceof ComboBox) {
            return ((ComboBox<?>) dynamicSearchField).getValue() != null
                    ? ((ComboBox<?>) dynamicSearchField).getValue().toString()
                    : "";
        }
        return "";
    }
}
