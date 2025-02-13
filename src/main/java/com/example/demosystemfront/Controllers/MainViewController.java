package com.example.demosystemfront.Controllers;

import com.example.demosystemfront.ApiService;
import com.example.demosystemfront.Entities.Booking;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class MainViewController {
    private Stage primaryStage;

    ApiService api = new ApiService();

    public MainViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void addBookingsToGrid(List<Booking> bookingList, GridPane gridPane, String status) {
        int row = 0;
        int col = 0;

        for (Booking booking : bookingList) {
            // Create a VBox for the booking

            VBox bookingView = quickView(booking, status);

            // Add the VBox to the GridPane at the current row and column
            gridPane.add(bookingView, col, row);

            // Increment the column and row counters
            col++;
            if (col == 2) { // Move to the next row after every 2 columns
                col = 0;
                row++;
            }
        }
    }

    public VBox quickView(Booking booking, String staus) {
Label warning = new Label("Nie opłacone");
warning.getStyleClass().add("unpaid-warning");

VBox quickViewCard = new VBox();
    //    Button buttonInfo = new Button("Zobacz więcej");
quickViewCard.getChildren().addAll(new Label("ID: " + booking.getId().toString()));
quickViewCard.getChildren().addAll(new Label(booking.name));
quickViewCard.getChildren().addAll(new Label(booking.getArrivalDate().toString() + " - " + booking.getDepartureDate().toString()));
if(!booking.isPaid){
    quickViewCard.getChildren().add(warning);
}
        quickViewCard.setOnMouseClicked(event -> {
            BookingInfoController bookingInfoController = new BookingInfoController(primaryStage);
            primaryStage.setScene(bookingInfoController.createContent(Integer.parseInt(booking.getId().toString())));
        });




        quickViewCard.getStyleClass().add("quick-view-card");
if(staus.equals("arrival") && booking.isHasArrived()){
    quickViewCard.getStyleClass().add("quick-view-card-arrived");
}
if(staus.equals("departure") && booking.isHasLeft()){
    quickViewCard.getStyleClass().add("quick-view-card-left");
}
if(staus.equals("arrival") && !booking.isHasArrived()){
    quickViewCard.getStyleClass().add("quick-view-card-not-arrived");
}
if(staus.equals("departure") && !booking.isHasLeft()){
    quickViewCard.getStyleClass().add("quick-view-card-not-left");
}



        return quickViewCard;
    }

    public Scene createContent() throws IOException, InterruptedException {


        VBox menuBox = MenuController.showMenu(primaryStage);


        List<Booking> todayArrivals = api.findReservationsByArrivalDate(LocalDate.now());
        List<Booking> todayDepartures = api.findReservationsByDepartureDate(LocalDate.now());
        List<Booking> tomorrowArrivals = api.findReservationsByArrivalDate(LocalDate.now().plusDays(1));
        List<Booking> tomorrowDepartures = api.findReservationsByDepartureDate(LocalDate.now().plusDays(1));

        GridPane gridPane = new GridPane();
        VBox mainBookingPanel = new VBox();
        mainBookingPanel.setPadding(new Insets(20));
        mainBookingPanel.getChildren().add(gridPane);
//        gridPane.setStyle("-fx-border-color: white");

        VBox todayArrivalsContainer = new VBox(10, new Label("Dzisiaj - Przyjazdy"));
        todayArrivalsContainer.getStyleClass().add("booking-box");
        GridPane taGridPane = new GridPane();
        taGridPane.getStyleClass().add("inner-grid");
        todayArrivalsContainer.getChildren().add(taGridPane);

        VBox todayDeparturesContainer = new VBox(10, new Label("Dzisiaj - Wyjazdy"));
        todayDeparturesContainer.getStyleClass().add("booking-box");
        GridPane tdGridPane = new GridPane();
        tdGridPane.getStyleClass().add("inner-grid");
        todayDeparturesContainer.getChildren().add(tdGridPane);

        VBox tomorrowArrivalsContainer = new VBox(10, new Label("Jutro - Przyjazdy"));
        tomorrowArrivalsContainer.getStyleClass().add("booking-box");
        GridPane tmaGridPane = new GridPane();
        tmaGridPane.getStyleClass().add("inner-grid");
        tomorrowArrivalsContainer.getChildren().add(tmaGridPane);

        VBox tomorrowDeparturesContainer = new VBox(10, new Label("Jutro - Wyjazdy"));
        tomorrowDeparturesContainer.getStyleClass().add("booking-box");
        GridPane tmdGridPane = new GridPane();
        tmdGridPane.getStyleClass().add("inner-grid");
        tomorrowDeparturesContainer.getChildren().add(tmdGridPane);

        gridPane.add(todayArrivalsContainer, 0, 0);
        gridPane.add(todayDeparturesContainer, 1, 0);
        gridPane.add(tomorrowArrivalsContainer, 0, 1);
        gridPane.add(tomorrowDeparturesContainer, 1, 1);
addBookingsToGrid(todayArrivals, taGridPane, "arrival");
addBookingsToGrid(todayDepartures, tdGridPane, "departure");
addBookingsToGrid(tomorrowArrivals, tmaGridPane, "arrival");
addBookingsToGrid(tomorrowDepartures, tmdGridPane, "departure");


VBox cover = new VBox();
VBox smallerBox = new VBox();
smallerBox.getStyleClass().add("smaller-box-home");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(mainBookingPanel);  // Set the VBox as the content of the ScrollPane
        scrollPane.setFitToWidth(true); // Makes the VBox take up the full width of the ScrollPane
        smallerBox.getChildren().add(scrollPane);
        cover.getChildren().add(smallerBox);
        scrollPane.getStyleClass().add("scroll-pane-main");
        mainBookingPanel.getStyleClass().add("main-pane");
BorderPane mainPane = new BorderPane();
mainPane.setTop(MenuController.showTopPanel());
mainPane.setLeft(menuBox);

cover.getStyleClass().add("cover");
mainPane.setCenter(cover);
        Scene scene = new Scene(mainPane, 1200, 750);
        scene.getStylesheets().add(getClass().getResource("/nextStyle.css").toExternalForm());

        return scene;
    }
}
