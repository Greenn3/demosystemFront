package com.example.demosystemfront.Controllers;

import com.example.demosystemfront.AlertWindow;
import com.example.demosystemfront.ApiService;
import com.example.demosystemfront.Entities.Booking;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

public class MenuController {
    private Stage primaryStage;
    AlertWindow alertWindow = new AlertWindow();
    ApiService api = new ApiService();

    public MenuController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public VBox quickView(Booking booking) {
        // GridPane for booking details in a row-column format
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);

        // Add booking details in table-like structure
        gridPane.add(new Label("ID: "), 0, 0);
        gridPane.add(new Label(booking.getId().toString()), 1, 0);
        gridPane.add(new Label("Name: "), 0, 1);
        gridPane.add(new Label(booking.name), 1, 1);
        gridPane.add(new Label("Arrival: "), 0, 2);
        gridPane.add(new Label(booking.getArrivalDate().toString()), 1, 2);
        gridPane.add(new Label("Departure: "), 0, 3);
        gridPane.add(new Label(booking.getDepartureDate().toString()), 1, 3);

        // CheckBox and Button
        CheckBox didArrive = new CheckBox("Arrived");
        Button buttonInfo = new Button("Zobacz");
        buttonInfo.setOnAction(e -> {
            BookingInfoController bookingInfoController = new BookingInfoController(primaryStage);
            primaryStage.setScene(bookingInfoController.createContent(Integer.parseInt(booking.getId().toString())));
        });

        // Create a VBox to wrap the booking details and add borders
        VBox vBox = new VBox(10, gridPane, didArrive, buttonInfo);
        vBox.setPadding(new Insets(10));

        // Set constraints to make the boxes smaller
        vBox.setMaxWidth(180);  // Maximum width of each box
        vBox.setMinWidth(160);  // Minimum width of each box
        vBox.setStyle("-fx-border-color: rgb(128,128,128); -fx-border-width: 1; -fx-border-radius: 5;");

        return vBox;
    }

    public Scene createContent() throws IOException, InterruptedException {
        System.out.println("classpath=" + System.getProperty("java.class.path"));

        // Left panel with buttons (keep as is)
        VBox buttonPanel = new VBox(10);
        buttonPanel.setPrefWidth(200); // Set preferred width for the sliding panel

        // Create buttons to switch between scenes
        Button scene1Button = new Button("Nowa rezerwacja");
        scene1Button.setOnAction(e -> {
            AddBookingViewController addBookingViewController = new AddBookingViewController(primaryStage);
            primaryStage.setScene(addBookingViewController.createContent());
        });

        Button scene2Button = new Button("Kalendarz");
        scene2Button.setOnAction(e -> {
            CalendarViewController calendarViewController = new CalendarViewController(primaryStage);
            try {
                primaryStage.setScene(calendarViewController.createContent());
            } catch (IOException | InterruptedException | URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button scene3Button = new Button("Cennik");
        scene3Button.setOnAction(e -> {
            PriceListViewController priceListViewController = new PriceListViewController(primaryStage);
            primaryStage.setScene(priceListViewController.createContent());
        });

        Button scene4Button = new Button("Wyszukaj rezerwację");
        scene4Button.setOnAction(e -> {
            FindBookingControler findBookingControler = new FindBookingControler(primaryStage);
            primaryStage.setScene(findBookingControler.createContent());
        });

        buttonPanel.getChildren().addAll(scene1Button, scene2Button, scene3Button, scene4Button);
        buttonPanel.getStylesheets().add(getClass().getResource("/newStyle.css").toExternalForm());

        // Right panel for dynamic content
        VBox contentPanel1 = new VBox();
        VBox.setVgrow(contentPanel1, Priority.ALWAYS);

        // API calls to fetch data
        List<Booking> todayArrivals = api.findReservationsByArrivalDate(LocalDate.now());
        List<Booking> todayDepartures = api.findReservationsByDepartureDate(LocalDate.now());
        List<Booking> tomorrowArrivals = api.findReservationsByArrivalDate(LocalDate.now().plusDays(1));
        List<Booking> tomorrowDepartures = api.findReservationsByDepartureDate(LocalDate.now().plusDays(1));

        // Refresh button at the top
        Button refreshButton = new Button("Odśwież");
        refreshButton.setOnAction(event -> {
            try {
                createContent();  // Refresh the view
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // Header and Containers for "DZISIAJ"
        Label dzisiajHeader = new Label("DZISIAJ");
        dzisiajHeader.setStyle("-fx-font-weight: bold;");

        // "Przyjazdy" (Arrivals) for today
        Label dzisiajPrzyjazdyLabel = new Label("Przyjazdy:");
        VBox dzisiajPrzyjazdyBox = new VBox(10);  // 10 px spacing between items
        for (Booking booking : todayArrivals) {
            dzisiajPrzyjazdyBox.getChildren().add(quickView(booking));
        }

        // "Wyjazdy" (Departures) for today
        Label dzisiajWyjazdyLabel = new Label("Wyjazdy:");
        VBox dzisiajWyjazdyBox = new VBox(10);
        for (Booking booking : todayDepartures) {
            dzisiajWyjazdyBox.getChildren().add(quickView(booking));
        }

        // Group today's arrivals and departures in an HBox
        VBox dzisiajPrzyjazdyContainer = new VBox(5, dzisiajPrzyjazdyLabel, dzisiajPrzyjazdyBox);
        VBox dzisiajWyjazdyContainer = new VBox(5, dzisiajWyjazdyLabel, dzisiajWyjazdyBox);
        HBox dzisiajBox = new HBox(20, dzisiajPrzyjazdyContainer, dzisiajWyjazdyContainer);  // 20 px spacing between columns
        HBox.setHgrow(dzisiajPrzyjazdyContainer, Priority.ALWAYS);
        HBox.setHgrow(dzisiajWyjazdyContainer, Priority.ALWAYS);

        // Container for "DZISIAJ" with spacing and margin
        VBox dzisiajContainer = new VBox(15, dzisiajHeader, dzisiajBox);  // 15 px spacing between header and content
        dzisiajContainer.setPadding(new Insets(10));  // Padding around the "DZISIAJ" section

        // Header and Containers for "JUTRO"
        Label jutroHeader = new Label("JUTRO");
        jutroHeader.setStyle("-fx-font-weight: bold;");

        // "Przyjazdy" (Arrivals) for tomorrow
        Label jutroPrzyjazdyLabel = new Label("Przyjazdy:");
        VBox jutroPrzyjazdyBox = new VBox(10);
        for (Booking booking : tomorrowArrivals) {
            jutroPrzyjazdyBox.getChildren().add(quickView(booking));
        }

        // "Wyjazdy" (Departures) for tomorrow
        Label jutroWyjazdyLabel = new Label("Wyjazdy:");
        VBox jutroWyjazdyBox = new VBox(10);
        for (Booking booking : tomorrowDepartures) {
            jutroWyjazdyBox.getChildren().add(quickView(booking));
        }

        // Group tomorrow's arrivals and departures in an HBox
        VBox jutroPrzyjazdyContainer = new VBox(5, jutroPrzyjazdyLabel, jutroPrzyjazdyBox);
        VBox jutroWyjazdyContainer = new VBox(5, jutroWyjazdyLabel, jutroWyjazdyBox);
        HBox jutroBox = new HBox(20, jutroPrzyjazdyContainer, jutroWyjazdyContainer);  // 20 px spacing between columns
        HBox.setHgrow(jutroPrzyjazdyContainer, Priority.ALWAYS);
        HBox.setHgrow(jutroWyjazdyContainer, Priority.ALWAYS);

        // Container for "JUTRO" with spacing and margin
        VBox jutroContainer = new VBox(15, jutroHeader, jutroBox);  // 15 px spacing between header and content
        jutroContainer.setPadding(new Insets(10));  // Padding around the "JUTRO" section

        // Main VBox containing both "DZISIAJ" and "JUTRO" sections
        VBox mainBookingPanel = new VBox(20);
        mainBookingPanel.getChildren().addAll(dzisiajContainer, new Separator(), jutroContainer);
        mainBookingPanel.setPadding(new Insets(20));

        // Wrap the booking content in a ScrollPane for scrolling behavior
        ScrollPane scrollPane = new ScrollPane(mainBookingPanel);
        scrollPane.setFitToWidth(true);  // Make scroll pane fit to the width of the content
        scrollPane.setPadding(new Insets(5));  // Padding around the scrollable content

        // SplitPane to divide left and right panels
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(buttonPanel, scrollPane);  // Add the scroll pane here
        splitPane.setDividerPositions(0.2);  // Set initial divider position

        // Wrap in a scene
        Scene scene = new Scene(splitPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/newStyle.css").toExternalForm());

        return scene;
    }
}
