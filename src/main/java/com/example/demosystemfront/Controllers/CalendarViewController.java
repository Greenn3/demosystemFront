package com.example.demosystemfront.Controllers;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.AllDayView;
import com.calendarfx.view.WeekDayHeaderView;
import com.calendarfx.view.popover.EntryPopOverContentPane;
import com.example.demosystemfront.ApiService;

import com.example.demosystemfront.Entities.Booking;
import com.example.demosystemfront.HelperPDF;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


import java.time.format.DateTimeFormatter;


public class CalendarViewController {
    private Stage primaryStage;
    public List<Booking> bookingsList;
    public Calendar bookings = new Calendar("Bookings");
    ApiService apiService = new ApiService();
public HelperPDF helperPDF = new HelperPDF();
    public CalendarViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
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
        String id = pop.getEntry().getId();
        Label labelId = new Label();
        label.setText(pop.getEntry().getId());


        Button buttonAddEntry = new Button();
        buttonAddEntry.setText("Więcej");
        buttonAddEntry.setOnAction(event ->
                {
                    BookingInfoController bookingInfoController = new BookingInfoController(primaryStage);
                    primaryStage.setScene(bookingInfoController.createContent(Integer.parseInt(pop.getEntry().getId())));
                }
        );

        Button buttonGeneratePDF = new Button();

        buttonGeneratePDF.setText("PDF");
        buttonGeneratePDF.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(id);
                Booking booking = null;
                for (Booking b : bookingsList) {
                    System.out.println(b.toString());
                    if (b.getId().equals(Integer.valueOf(id))) {
                        booking = b;
                        System.out.println(b.toString());

                    }
                    else{
                        //make alert window
                    }
                }
                // helperPDF.generateConfirmationPDF(booking, primaryStage);
                helperPDF.generateInvoicePDF(booking, primaryStage);

            }
        });

        entryView.getChildren().addAll(label, textFieldName, textFieldArrivalDate, textFieldDepartureDate, datePickerArrivalDate, labelId, buttonAddEntry, buttonGeneratePDF);

        return entryView;
    }

    protected VBox showBookingScreen() throws IOException {

        // WeekDayHeaderView headerView = new WeekDayHeaderView(7);

        // header format
        DateTimeFormatter numericDateFormatter = DateTimeFormatter.ofPattern("dd-MM");

        // Create the WeekDayHeaderView and set the cell factory to use the custom formatter
        WeekDayHeaderView headerView = new WeekDayHeaderView(7);
        headerView.setCellFactory(new Callback<WeekDayHeaderView, WeekDayHeaderView.WeekDayHeaderCell>() {
            @Override
            public WeekDayHeaderView.WeekDayHeaderCell call(WeekDayHeaderView view) {
                WeekDayHeaderView.WeekDayHeaderCell cell = new WeekDayHeaderView.WeekDayHeaderCell(view);
                cell.setFormatter(numericDateFormatter); // Set the custom formatter
                return cell;
            }
        });

        // headerView.getCellFactory();



        AllDayView dayView = new AllDayView(7);
        AllDayView dayView2 = new AllDayView(7);
        dayView.bind(headerView, true);
        dayView2.bind(headerView, true);
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


        for (Booking b : bookingsList) {
            Entry entry = new Entry();
            entry.setTitle(b.getName());
            entry.setId(b.getId().toString());
            entry.changeStartDate(b.arrivalDate);
            entry.changeEndDate(b.departureDate);
            entry.setFullDay(true);

            bookings.addEntry(entry);

        }

        CalendarSource myCalendarSource = new CalendarSource("My Calendars"); // (4)
        myCalendarSource.getCalendars().addAll(bookings);


        dayView.getCalendarSources().add(myCalendarSource);


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
        vBox.getChildren().addAll(toolBarControls, headerView, dayView, dayView2);
        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();


        Scene scene = new Scene(vBox);
        Stage stage = new Stage();


        stage.setTitle("Calendar");
        stage.setScene(scene);
        stage.setWidth(700);
        stage.setHeight(500);
        stage.centerOnScreen();
        // stage.show();

        return vBox;
    }

    public Scene createContent() throws IOException, URISyntaxException, InterruptedException {
        VBox layout = new VBox(10);
        // layout.setStyle("-fx-background-color: lightcoral");
//back button
        Button backButton = new Button("<");
        backButton.setOnAction(e -> {
            MenuController menuScene = new MenuController(primaryStage);
            //  primaryStage.setScene(new Scene(menuScene.createContent(), 400, 300));
            try {
                primaryStage.setScene(menuScene.createContent());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        bookingsList = apiService.loadAllReservations();
      Label label = new Label("Wygeneruj zestawienie od - do");

MenuBar menuBar = new MenuBar();
        Menu viewMenu = new Menu("Rodzaj pliku:");

        // Create RadioMenuItems
        RadioMenuItem pdf = new RadioMenuItem("PDF");
        RadioMenuItem csv = new RadioMenuItem("CSV");

        // Add them to a ToggleGroup
        ToggleGroup viewGroup = new ToggleGroup();
        pdf.setToggleGroup(viewGroup);
        csv.setToggleGroup(viewGroup);
        viewMenu.getItems().addAll(pdf, csv);
menuBar.getMenus().add(viewMenu);
DatePicker from = new DatePicker();
DatePicker to = new DatePicker();
List<Booking> selectedBookings = new ArrayList<>();

Button generate  = new Button("Generuj");
generate.setOnAction(event -> {
    for(Booking b : bookingsList){
        if ((b.arrivalDate.isAfter(from.getValue()) || b.arrivalDate.equals(from.getValue())) && (b.departureDate.isBefore(to.getValue())|| b.departureDate.equals(to.getValue()))){
            selectedBookings.add(b);
        }
    }
    helperPDF.generateDocument(selectedBookings, primaryStage);
});
VBox generationVbox = new VBox(5,label, menuBar, from, to, generate);




      //  helperPDF.generateDocumentXML(bookingsList, primaryStage);
        showBookingScreen();
        VBox vBoxCalendar = showBookingScreen();

        layout.getChildren().addAll(backButton, vBoxCalendar,generationVbox );
        //layout.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        layout.getStylesheets().add(getClass().getResource("/newStyle.css").toExternalForm());
        return new Scene(layout, 900, 700);
    }



}
