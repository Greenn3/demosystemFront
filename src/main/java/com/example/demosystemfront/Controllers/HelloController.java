package com.example.demosystemfront.Controllers;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.*;
import com.calendarfx.view.popover.EntryPopOverContentPane;
import com.example.demosystemfront.Entities.Booking;
import com.example.demosystemfront.Entities.AccType;
import com.example.demosystemfront.Entities.PricePerType;
import com.example.demosystemfront.Entities.PricePeriod;
import com.example.demosystemfront.HelloApplication;
import com.example.demosystemfront.LocalDateTypeAdapter;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private TextField textFieldName;
    @FXML
    private DatePicker datePickerArrivalDate;
    @FXML
    private DatePicker datePickerDepartureDate;
public List<AccType> accTypes;
public List<PricePeriod> pricePeriods;
public List<PricePerType> pricePerTypeList = new ArrayList<>();

    public List<Booking> bookingsList;
   public Calendar bookings = new Calendar("Bookings");
    @FXML
    protected void onHelloButtonClick() throws URISyntaxException, IOException, InterruptedException {
        welcomeText.setText("Welcome to JavaFX Application!");
        HttpClient client = HttpClient.newBuilder()
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/rezerwacje"))
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        Type listType = new TypeToken<List<Booking>>() {
        }.getType();
        bookingsList = gson.fromJson((String) response.body(), listType);


        showBookingScreen();
    }

    @FXML
    protected void addBookingScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addBooking.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);



        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();


    }
    public void loadPriceListData() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/types"))
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        Type listType = new TypeToken<List<AccType>>() {
        }.getType();
        accTypes = gson.fromJson((String) response.body(), listType);
        HttpClient client2 = HttpClient.newBuilder()
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/periods"))
                .build();
        HttpResponse response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Gson gson2 = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        Type listType2 = new TypeToken<List<PricePeriod>>() {
        }.getType();
        pricePeriods = gson2.fromJson((String) response2.body(), listType2);

        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getPrices"))
                .build();
        HttpResponse response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        Gson gson3 = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        Type listType3 = new TypeToken<List<PricePerType>>() {
        }.getType();
        pricePerTypeList = gson3.fromJson((String) response3.body(), listType3);




    }
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
    protected void showPriceListScreen() throws IOException, InterruptedException {
        loadPriceListData();
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
                count++;
                int finalRow = row;
                int finalCol = col;
                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue) {
                        System.out.println("Focus lost. Text is: " + textField.getText());
                        // Perform action here
                         update(textField.getText(), finalRow, finalCol);
                    }
                });

              //  PricePerType pricePerType = new PricePerType(id, pricePeriods.get(col), accTypes.get(row), Double.parseDouble(textField.getText()));

                //pricePerTypeList.add(pricePerType);
                textField.setPrefWidth(100);
                textField.setPrefHeight(50);
                mainGrid.add(textField, col, row);
            }
        }

        // Create the top header grid (1x5)
        GridPane topHeaderGrid = new GridPane();
        topHeaderGrid.setHgap(2);
        topHeaderGrid.setStyle("-fx-padding: 10px;");

        for (int col = 0; col < 5; col++) {
            TextField textField = new TextField(pricePeriods.get(col).getName());
            textField.setPrefWidth(100);
            textField.setPrefHeight(50);
            topHeaderGrid.add(textField, col, 0);
        }

        // Create the left header grid (5x1)
        GridPane leftHeaderGrid = new GridPane();
        leftHeaderGrid.setVgap(5);
        leftHeaderGrid.setStyle("-fx-padding: 10px;");

        for (int row = 0; row < 6; row++) {
            TextField textField = new TextField(accTypes.get(row).getName());
            textField.setPrefWidth(100);
            textField.setPrefHeight(50);
            leftHeaderGrid.add(textField, 0, row);
        }
        Button button = new Button();
        button.setText("Zapisz");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    updatePriceListToDataBase();
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
        Scene scene = new Scene(root, 700, 400);

        // Set the scene to the stage
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle("GridPane Example");
        primaryStage.show();
        testPrintLists();


    }








    protected VBox entryView(EntryPopOverContentPane pop) {
        VBox entryView = new VBox();

        entryView.setPrefSize(200, 200);
        Label label = new Label();
        TextField textFieldName = new TextField();
        TextField textFieldArrivalDate = new TextField();
        DatePicker datePickerArrivalDate = new DatePicker();
        datePickerArrivalDate.setValue(pop.getEntry().getStartDate());
        TextField textFieldDepartureDate = new TextField();
        label.setText(pop.getEntry().getTitle());
        textFieldName.setText("nazwisko: " + pop.getEntry().getTitle());
        TextField textFieldName2 = new TextField();
        textFieldName2.setText(pop.getEntry().getId());
        textFieldArrivalDate.setText("data przyjazdu: " + pop.getEntry().getStartDate().toString());
        textFieldDepartureDate.setText("data wyjazdu: " + pop.getEntry().getEndDate().toString());

        Label labelId = new Label();
        label.setText(pop.getEntry().getId());


        Button buttonAddEntry = new Button();
        buttonAddEntry.setText("Dodaj");
        buttonAddEntry.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Entry entry = pop.getEntry();
                entry.setTitle(textFieldName.getText());
                entry.changeStartDate(datePickerArrivalDate.getValue());
                bookings.addEntry(entry);
            }
        });
        entryView.getChildren().addAll(label, textFieldName, textFieldArrivalDate, textFieldDepartureDate, datePickerArrivalDate, labelId, buttonAddEntry);

        return entryView;
    }

    @FXML
    protected void showBookingScreen() throws IOException {

        WeekDayHeaderView headerView = new WeekDayHeaderView(7);

        AllDayView dayView = new AllDayView(7);
        dayView.bind(headerView, true);
        VBox vBoxEntryView = new VBox();


        Label ebtryViewLabel = new Label();
        ebtryViewLabel.setText("this");
        vBoxEntryView.getChildren().add(ebtryViewLabel);

        HBox toolBarControls = new HBox();
        Button buttonBackwards = new Button();
        buttonBackwards.setText("<");
        buttonBackwards.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dayView.goBack();
                dayView.goBack();
                dayView.goBack();
                dayView.goBack();


            }
        });
        Button buttonForwards = new Button();
        buttonForwards.setText(">");
        buttonForwards.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                dayView.goForward();
                dayView.goForward();
                dayView.goForward();
                dayView.goForward();

            }
        });
        toolBarControls.getChildren().add(buttonBackwards);
        toolBarControls.getChildren().add(buttonForwards);


        Calendar birthdays = new Calendar("Birthdays"); // (2)
        Calendar holidays = new Calendar("Holidays");



        for (Booking b : bookingsList) {
            Entry entry = new Entry();
            entry.setTitle(b.getName());
            entry.setId(b.getId().toString());
            entry.changeStartDate(b.arrivalDate);
            entry.changeEndDate(b.departureDate);
            entry.setFullDay(true);

            bookings.addEntry(entry);

        }


        birthdays.setStyle(Calendar.Style.STYLE1); // (3)
        holidays.setStyle(Calendar.Style.STYLE2);

        CalendarSource myCalendarSource = new CalendarSource("My Calendars"); // (4)
        myCalendarSource.getCalendars().addAll(bookings);


        dayView.getCalendarSources().add(myCalendarSource);
/*

        Entry entry = new Entry();
        entry.setTitle("Booking1");
        // entry.setInterval(LocalDate.of(2024, Month.MARCH, 18), LocalDate.of(2024, Month.MARCH, 20));
        entry.changeStartDate(LocalDate.of(2024, Month.MARCH, 18));
        entry.changeEndDate(LocalDate.of(2024, Month.MARCH, 20));
        entry.setFullDay(true);
        Entry entry1 = new Entry();
        entry1.setTitle("Booking2");
        // entry1.setInterval(LocalDate.of(2024, Month.MARCH, 17), LocalDate.of(2024, Month.MARCH, 22));
        entry1.changeStartDate(LocalDate.of(2024, Month.MARCH, 17));
        entry1.changeEndDate(LocalDate.of(2024, Month.MARCH, 22));
        entry1.setFullDay(true);

        Entry entry2 = new Entry();
        entry.setTitle("Booking2");
        // entry1.setInterval(LocalDate.of(2024, Month.MARCH, 17), LocalDate.of(2024, Month.MARCH, 22));
        entry2.changeStartDate(LocalDate.of(2024, Month.MARCH, 17));
        entry2.changeEndDate(LocalDate.of(2024, Month.MARCH, 22));
        entry2.setFullDay(true);

        Entry entry3 = new Entry();
        entry3.setTitle("Booking2");
        // entry1.setInterval(LocalDate.of(2024, Month.MARCH, 17), LocalDate.of(2024, Month.MARCH, 22));
        entry3.changeStartDate(LocalDate.of(2024, Month.MARCH, 17));
        entry3.changeEndDate(LocalDate.of(2024, Month.MARCH, 22));
        entry3.setFullDay(true);

        Entry entry4 = new Entry();
        entry4.setTitle("Booking2");
        entry4.changeStartDate(LocalDate.of(2024, Month.MARCH, 17));
        entry4.changeEndDate(LocalDate.of(2024, Month.MARCH, 22));
        entry4.setFullDay(true);
        birthdays.addEntry(entry);
        birthdays.addEntry(entry1);
        birthdays.addEntry(entry2);
        birthdays.addEntry(entry3);
        birthdays.addEntry(entry4);
        birthdays.addEntry(new Entry<>("dnvljsdnv"));

 */

        // entry.intervalProperty().addListener(weakEntryIntervalListener);
        //  EntryPopOverContentPane name = new EntryPopOverContentPane()
        VBox v = new VBox();
        dayView.setEntryDetailsPopOverContentCallback(param -> {
            EntryPopOverContentPane pop = new EntryPopOverContentPane(param.getPopOver(), param.getDateControl(), param.getEntry());

            return entryView(pop);



        });
        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {

                        dayView.setToday(LocalDate.now());
                        dayView.setTime(LocalTime.now());

                    });

                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        VBox vBox = new VBox();
        vBox.getChildren().addAll(toolBarControls, headerView, dayView);
        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();

        // FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addBooking.fxml"));

        Scene scene = new Scene(vBox);
        Stage stage = new Stage();


        stage.setTitle("Calendar");
        stage.setScene(scene);
        stage.setWidth(700);
        stage.setHeight(500);
        stage.centerOnScreen();
        stage.show();
    }
public void updatePriceListToDataBase() throws IOException, InterruptedException {

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();
    String json = gson.toJson(pricePerTypeList);
    HttpClient client = HttpClient.newBuilder()
            .build();

    System.out.println(json);

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/updatePriceList"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

    // Send the request and get the response
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    // Print response
    System.out.println("Response code: " + response.statusCode());
    System.out.println("Response body: " + response.body());
}

    @FXML
    protected void onAddButtonClick() throws IOException, InterruptedException {
        System.out.println(textFieldName.getText());
        System.out.println(datePickerDepartureDate.getValue());
        System.out.println(datePickerArrivalDate.getValue());

        Booking booking = new Booking(textFieldName.getText(), datePickerArrivalDate.getValue(), datePickerDepartureDate.getValue());
        // System.out.println(booking.getName());
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        String json = gson.toJson(booking);
        HttpClient client = HttpClient.newBuilder()
                .build();
        System.out.println(booking);
        System.out.println(json);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/dodajRezerwacje"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Print response
        System.out.println("Response code: " + response.statusCode());
        System.out.println("Response body: " + response.body());

    }
}


