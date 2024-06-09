package com.example.demosystemfront.Controllers;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.AllDayView;
import com.calendarfx.view.WeekDayHeaderView;
import com.calendarfx.view.popover.EntryPopOverContentPane;
import com.example.demosystemfront.ApiService;
import com.example.demosystemfront.Entities.Booking;
import com.example.demosystemfront.LocalDateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CalendarViewController {
    private Stage primaryStage;
    public List<Booking> bookingsList;
    public Calendar bookings = new Calendar("Bookings");
    ApiService apiService = new ApiService();

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
        buttonAddEntry.setText("Dodaj");

        Button buttonGeneratePDF = new Button();
        buttonGeneratePDF.setText("PDF");
        buttonGeneratePDF.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
generatePDF(id);
            }
        });
        buttonAddEntry.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Entry entry = pop.getEntry();
                entry.setTitle(textFieldName.getText());
                entry.changeStartDate(datePickerArrivalDate.getValue());
                bookings.addEntry(entry);
            }
        });
        entryView.getChildren().addAll(label, textFieldName, textFieldArrivalDate, textFieldDepartureDate, datePickerArrivalDate, labelId, buttonAddEntry, buttonGeneratePDF);

        return entryView;
    }

    protected VBox showBookingScreen() throws IOException {

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
        vBox.getChildren().addAll(toolBarControls, headerView, dayView);
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

        Button backButton = new Button("<");
        backButton.setOnAction(e -> {
            MenuController menuScene = new MenuController(primaryStage);
            primaryStage.setScene(new Scene(menuScene.createContent(), 400, 300));
        });
        bookingsList = apiService.loadAllReservations();
        showBookingScreen();
        VBox vBoxCalendar = showBookingScreen();
        layout.getChildren().add(backButton);
        layout.getChildren().add(vBoxCalendar);

        return new Scene(layout, 900, 700);
    }

    public void generatePDF(String idS){
        int id = Integer.parseInt(idS);
        Booking booking = null;
        for(Booking b : bookingsList){
            if (b.getId() == id) booking = b;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                // Add labels as headers (omitted for brevity)


                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText("Potwierdzenie rezerwacji");


                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

                contentStream.newLineAtOffset(0, - 50);
                contentStream.showText("Nazwisko: ");
                contentStream.showText(booking.getName());

                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Rodzaj noclegu: ");
                contentStream.showText(booking.getAccType().getName());

                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Data przyjazdu: ");
                contentStream.showText(booking.arrivalDate.toString());

                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Data wyjazdu:");
                contentStream.showText(booking.departureDate.toString());

                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Cena:");
                contentStream.showText(apiService.getPrice(booking).toString());

                contentStream.endText();
                contentStream.close();

                // Save the document after adding content
                document.save(file);
                System.out.println("PDF created successfully");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    private void drawTextWithLineFeed(PDPageContentStream contentStream, String text) throws IOException {
        String[] lines = text.split("\n");
        for (String line : lines) {
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, -20); // Adjust spacing between lines as needed
        }
    }


}
