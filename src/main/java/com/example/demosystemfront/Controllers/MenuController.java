package com.example.demosystemfront.Controllers;

import com.example.demosystemfront.ApiService;
import com.example.demosystemfront.Entities.Booking;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

public class MenuController {
    private Stage primaryStage;
    ApiService api = new ApiService();
    // Helper method to create VBox with Labels for each item in the list
    VBox createLabelVBox(String headerText, List<String> items) {
        VBox vbox = new VBox(5);
        Label header = new Label(headerText);
        vbox.getChildren().add(header);
        for (String item : items) {
            Label label = new Label(item);
            vbox.getChildren().add(label);
        }
        VBox.setVgrow(vbox, Priority.ALWAYS);
        return vbox;
    }
public VBox quickView(Booking booking){
        VBox vBox = new VBox();
        Label name = new Label(booking.name);
        Label id = new Label(booking.getId().toString());
    CheckBox didArrive = new CheckBox();
    Button buttonInfo = new Button("zobacz");

    buttonInfo.setOnAction(e -> {
        BookingInfoController bookingInfoController = new BookingInfoController(primaryStage);
            primaryStage.setScene(bookingInfoController.createContent(Integer.parseInt(id.getText())));
    });
    vBox.getChildren().addAll(name, didArrive, buttonInfo, id);
    vBox.setMinWidth(20);
        return vBox;
}
    public MenuController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene createContent() throws IOException, InterruptedException {
        System.out.println("classpath=" + System.getProperty("java.class.path"));

        // Left panel with buttons
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
            } catch (IOException | URISyntaxException | InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button scene3Button = new Button("Cennik");
        scene3Button.setOnAction(e -> {
            PriceListViewController priceListViewController = new PriceListViewController(primaryStage);
            primaryStage.setScene(priceListViewController.createContent());
        });

        buttonPanel.getChildren().addAll(scene1Button, scene2Button, scene3Button);
        buttonPanel.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // Right panel for dynamic content
        VBox contentPanel = new VBox();
        VBox.setVgrow(contentPanel, Priority.ALWAYS);


        // Helper method to create VBox with Labels for each item in the list


        // Header and Labels for "DZISIAJ"
        Label dzisiajHeader = new Label("DZISIAJ");

        List<Booking> todayArrivals = api.findReservationsByArrivalDate(LocalDate.now());
       // VBox dzisiajPrzyjazdyBox = createLabelVBox("PRZYJAZDY", List.of(todayArrivals.toString()));
VBox dzisiajPrzyjazdyBox = new VBox(20);
for(Booking booking : todayArrivals){
    dzisiajPrzyjazdyBox.getChildren().add(quickView(booking));
}
        List<Booking> todayDepartures = api.findReservationsByDepartureDate(LocalDate.now());
      //  VBox dzisiajWyjazdyBox = createLabelVBox("WYJAZDY", List.of(todayDepartures.toString()));
VBox dzisiajWyjazdyBox = new VBox(20);
        for(Booking booking : todayDepartures){
            dzisiajWyjazdyBox.getChildren().add(quickView(booking));
        }

        HBox dzisiajBox = new HBox(10, dzisiajPrzyjazdyBox, dzisiajWyjazdyBox);
        HBox.setHgrow(dzisiajPrzyjazdyBox, Priority.ALWAYS);
        HBox.setHgrow(dzisiajWyjazdyBox, Priority.ALWAYS);



        // Header and Labels for "JUTRO"
        Label jutroHeader = new Label("JUTRO");

        List<Booking> tomorrowArrivals = api.findReservationsByArrivalDate(LocalDate.now().plusDays(1));
      //  VBox jutroPrzyjazdyBox = createLabelVBox("PRZYJAZDY", List.of(tomorrowArrivals.toString()));
VBox jutroPrzyjazdyBox = new VBox();
for(Booking booking : tomorrowArrivals){
    jutroPrzyjazdyBox.getChildren().add(quickView(booking));
}
        List<Booking> tomorrowDepartures = api.findReservationsByDepartureDate(LocalDate.now().plusDays(1));
       // VBox jutroWyjazdyBox = createLabelVBox("WYJAZDY", List.of(tomorrowDepartures.toString()));
VBox jutroWyjazdyBox = new VBox();
for(Booking booking : tomorrowDepartures){
    jutroWyjazdyBox.getChildren().add(quickView(booking));
}
        HBox jutroBox = new HBox(10, jutroPrzyjazdyBox, jutroWyjazdyBox);
        HBox.setHgrow(jutroPrzyjazdyBox, Priority.ALWAYS);
        HBox.setHgrow(jutroWyjazdyBox, Priority.ALWAYS);

        // Add all to the content panel
        VBox dzisiajContainer = new VBox(10, dzisiajHeader, dzisiajBox);
        VBox jutroContainer = new VBox(10, jutroHeader, jutroBox);
        VBox.setVgrow(dzisiajContainer, Priority.ALWAYS);
        VBox.setVgrow(jutroContainer, Priority.ALWAYS);

        contentPanel.getChildren().addAll(dzisiajContainer, jutroContainer);



        // SplitPane to divide left and right panels
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(buttonPanel, contentPanel);
        splitPane.setDividerPositions(0.2); // Set initial divider position


        // Wrap in a scene
        Scene scene = new Scene(splitPane, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        return scene;
    }
}
