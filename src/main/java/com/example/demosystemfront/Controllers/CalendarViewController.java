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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


import java.time.format.DateTimeFormatter;


public class CalendarViewController {
    com.example.demosystemfront.Menu menu = new com.example.demosystemfront.Menu();
    private Stage primaryStage;
    public List<Booking> bookingsList;
    public List<Booking> ap1Bookings = new ArrayList<>();
    public List<Booking> ap2Bookings = new ArrayList<>();

    public List<Booking> ap3Bookings = new ArrayList<>();
    public List<Booking> campingBookings = new ArrayList<>();
    public Calendar bookings = new Calendar("Bookings");
    public Calendar ap1 = new Calendar("ap1");
    public Calendar ap2 = new Calendar("ap2");

    public Calendar ap3 = new Calendar("ap3");
    public Calendar camping = new Calendar("camping");
    ApiService apiService = new ApiService();
public HelperPDF helperPDF = new HelperPDF();
    public CalendarViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    protected VBox entryView(EntryPopOverContentPane pop) {
        VBox entryView = new VBox();

        entryView.setPrefSize(75, 15);
//        Label label = new Label();
//        TextField textFieldName = new TextField();
//        TextField textFieldArrivalDate = new TextField();
//        DatePicker datePickerArrivalDate = new DatePicker();
//        datePickerArrivalDate.setValue(pop.getEntry().getStartDate());
//        TextField textFieldDepartureDate = new TextField();
//        label.setText(pop.getEntry().getTitle());
//        textFieldName.setText("nazwisko: " + pop.getEntry().getTitle());
//        TextField textFieldName2 = new TextField();
//        textFieldName2.setText(pop.getEntry().getId());
//        textFieldArrivalDate.setText("data przyjazdu: " + pop.getEntry().getStartDate().toString());
//        textFieldDepartureDate.setText("data wyjazdu: " + pop.getEntry().getEndDate().toString());
        String id = pop.getEntry().getId();
//        Label labelId = new Label();
//        label.setText(pop.getEntry().getId());


        Button buttonAddEntry = new Button();
        buttonAddEntry.setText("Więcej");
       // buttonAddEntry.setPrefWidth(45);
        buttonAddEntry.setOnAction(event ->
                {
                    BookingInfoController bookingInfoController = new BookingInfoController(primaryStage);
                    primaryStage.setScene(bookingInfoController.createContent(Integer.parseInt(pop.getEntry().getId())));
                }
        );

    //    Button buttonGeneratePDF = new Button();

//        buttonGeneratePDF.setText("PDF");
//        buttonGeneratePDF.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                System.out.println(id);
//                Booking booking = null;
//                for (Booking b : bookingsList) {
//                    System.out.println(b.toString());
//                    if (b.getId().equals(Integer.valueOf(id))) {
//                        booking = b;
//                        System.out.println(b.toString());
//
//                    }
//                    else{
//                        //make alert window
//                    }
//                }
//                // helperPDF.generateConfirmationPDF(booking, primaryStage);
//                helperPDF.generateInvoicePDF(booking, primaryStage);
//
//            }
//        });

        entryView.getChildren().addAll( buttonAddEntry);

        return entryView;
    }

    protected HBox showBookingScreen() throws IOException {

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


        AllDayView dayViewAp1 = new AllDayView(7);
        AllDayView dayViewAp2 = new AllDayView(7);
        AllDayView dayViewAp3 = new AllDayView(7);
        AllDayView dayViewCamping = new AllDayView(7);

       dayViewAp1.bind(headerView, true);
       // dayViewAp1.bind(headerView, true);
        VBox vBoxEntryView = new VBox();
bookings.setStyle(Calendar.Style.STYLE2);
ap1.setStyle(Calendar.Style.STYLE3);
ap2.setStyle(Calendar.Style.STYLE4);
ap3.setStyle(Calendar.Style.STYLE5);
camping.setStyle(Calendar.Style.STYLE1);

        Label ebtryViewLabel = new Label();
        ebtryViewLabel.setText("this");
        vBoxEntryView.getChildren().add(ebtryViewLabel);

        HBox toolBarControls = new HBox();
        Button buttonBackwards = new Button();
        buttonBackwards.setText("<");
        buttonBackwards.setOnAction(event -> {
            for(int i = 0; i<5;i++){
                dayViewAp1.goBack();
                dayViewAp2.goBack();
                dayViewAp3.goBack();
                dayViewCamping.goBack();
            }
        });

        Button buttonForwards = new Button();
        buttonForwards.setText(">");

        buttonForwards.setOnAction( event -> {
            for(int i = 0; i<5; i++){
                dayViewAp1.goForward();
                dayViewAp2.goForward();
                dayViewAp3.goForward();
                dayViewCamping.goForward();
            }
        });

        toolBarControls.getChildren().add(buttonBackwards);
        toolBarControls.getChildren().add(buttonForwards);


     for(Booking b : bookingsList){
         if(b.accType.getId() == 1){
             ap1Bookings.add(b);
         }
         else if(b.accType.getId() == 2){
             ap2Bookings.add(b);
         }
         else if(b.accType.getId() == 3){
             ap3Bookings.add(b);
         }
         else{
             campingBookings.add(b);
         }
     }

     listToCalendar(ap1, ap1Bookings);
     listToCalendar(ap2, ap2Bookings);
     listToCalendar(ap2, ap3Bookings);
     listToCalendar(camping, campingBookings);



        CalendarSource sourceAp1 = new CalendarSource("SourceAp1");
        sourceAp1.getCalendars().add(ap1);

        CalendarSource sourceAp2 = new CalendarSource("SourceAp2");
        sourceAp2.getCalendars().add(ap2);

        CalendarSource sourceAp3 = new CalendarSource("SourceAp3");
        sourceAp3.getCalendars().add(ap3);

        CalendarSource sourceCamping = new CalendarSource("SourceCamping");
        sourceCamping.getCalendars().add(camping);



 dayViewAp1.getCalendarSources().add(sourceAp1);
 dayViewAp1.getStyleClass().add("calendar1");
 dayViewAp2.getCalendarSources().add(sourceAp2);
        dayViewAp2.getStyleClass().add("calendar1");
 dayViewAp3.getCalendarSources().add(sourceAp3);
        dayViewAp3.getStyleClass().add("calendar1");
 dayViewCamping.getCalendarSources().add(sourceCamping);
        dayViewCamping.getStyleClass().add("calendar1");

        Label labelAp1 = new Label("Apartment 1:");
        Label labelAp2 = new Label("Apartment 2:");
        Label labelAp3 = new Label("Apartment 3:");
        Label labelCamping = new Label("Camping:");

        // Set styling for the labels (bold for clarity)
//        labelAp1.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
//        labelAp2.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
//        labelAp3.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
//        labelCamping.setStyle("-fx-font-weight: bold; -fx-padding: 5;");

       VBox labels = new VBox();
       labels.setSpacing(25);
       labels.setPadding(new Insets(85, 10,10, 10));
       labels.getChildren().addAll(labelAp1, labelAp2, labelAp3, labelCamping);
       labels.setMinWidth(125);


      //popOver setting

        dayViewAp1.setEntryDetailsPopOverContentCallback(param -> {
            EntryPopOverContentPane pop = new EntryPopOverContentPane(param.getPopOver(), param.getDateControl(), param.getEntry());

            return entryView(pop);
        });
        dayViewAp2.setEntryDetailsPopOverContentCallback(param -> {
            EntryPopOverContentPane pop = new EntryPopOverContentPane(param.getPopOver(), param.getDateControl(), param.getEntry());

            return entryView(pop);
        });
        dayViewAp3.setEntryDetailsPopOverContentCallback(param -> {
            EntryPopOverContentPane pop = new EntryPopOverContentPane(param.getPopOver(), param.getDateControl(), param.getEntry());

            return entryView(pop);
        });
        dayViewCamping.setEntryDetailsPopOverContentCallback(param -> {
            EntryPopOverContentPane pop = new EntryPopOverContentPane(param.getPopOver(), param.getDateControl(), param.getEntry());

            return entryView(pop);
        });


        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {

                        dayViewAp1.setToday(LocalDate.now());
                        dayViewAp1.setTime(LocalTime.now());
                        dayViewAp2.setToday(LocalDate.now());
                        dayViewAp2.setTime(LocalTime.now());
                        dayViewAp3.setToday(LocalDate.now());
                        dayViewAp3.setTime(LocalTime.now());
                        dayViewCamping.setToday(LocalDate.now());
                        dayViewCamping.setTime(LocalTime.now());

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
        VBox calendarsBox = new VBox(10);
        calendarsBox.setPadding(new Insets(10));
        calendarsBox.setMinWidth(875);

        calendarsBox.setMaxWidth(Double.MAX_VALUE); // Allow it to stretch horizontally
        calendarsBox.setVgrow(calendarsBox, Priority.ALWAYS); // Allow it to stretch vertically



        calendarsBox.getChildren().addAll(toolBarControls, headerView,
                 dayViewAp1,
                 dayViewAp2,
                dayViewAp3,
              dayViewCamping);

HBox full = new HBox();

full.getChildren().addAll(labels, calendarsBox);

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();


//        Scene scene = new Scene(full);
//        Stage stage = new Stage();


//        stage.setTitle("Calendar");
//        stage.setScene(scene);
//        stage.setWidth(700);
//        stage.setHeight(500);
//        stage.centerOnScreen();
        // stage.show();

        return full;
    }

    public Scene createContent() throws IOException, URISyntaxException, InterruptedException {
   VBox menuBox = menu.showMenu(primaryStage);
   BorderPane mainPane = new BorderPane();
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
generate.getStyleClass().add("button-save");
generate.setOnAction(event -> {
    for(Booking b : bookingsList){
        if ((b.arrivalDate.isAfter(from.getValue()) || b.arrivalDate.equals(from.getValue())) && (b.departureDate.isBefore(to.getValue())|| b.departureDate.equals(to.getValue()))){
            selectedBookings.add(b);
        }
    }
    helperPDF.generateDocument(selectedBookings, primaryStage);
});
HBox hBox = new HBox(10);

hBox.getChildren().addAll( from, to, generate);
VBox generationVbox = new VBox(5,label,
     //   menuBar,
       hBox);


//generationVbox.getStyleClass().add("pane");
        Pane borderPane = new Pane();
        borderPane.getChildren().add(generationVbox);
        generationVbox.getStyleClass().add("generation-vbox");

     //   borderPane.getStyleClass().add("pane");

      //  helperPDF.generateDocumentXML(bookingsList, primaryStage);
       // showBookingScreen();
        HBox vBoxCalendar = showBookingScreen();
        vBoxCalendar.getStyleClass().add("calendar-box");

        layout.getChildren().addAll( vBoxCalendar,borderPane );
        //layout.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

      //  layout.getStylesheets().add(getClass().getResource("/nextStyle.css").toExternalForm());
        VBox smallerBox = new VBox();
        VBox cover = new VBox();
        cover.getChildren().add(smallerBox);
        cover.getStyleClass().add("cover");
        smallerBox.getChildren().add(layout);
        smallerBox.getStyleClass().add("smaller-box-cal");
layout.getStyleClass().add("calendar-layout");
mainPane.setLeft(menuBox);
mainPane.setCenter(cover);
        mainPane.setTop(menu.showTopPanel());
        Scene scene = new Scene(mainPane, 1300, 750);
        scene.getStylesheets().add(getClass().getResource("/nextStyle.css").toExternalForm());
        return scene;
    }

    public void listToCalendar(Calendar calendar, List<Booking> list){
        for(Booking b: list){
            Entry entry = new Entry();
            entry.setTitle(b.getName());
            entry.setId(b.getId().toString());
            entry.changeStartDate(b.arrivalDate);
            entry.changeEndDate(b.departureDate);
            entry.setFullDay(true);

            calendar.addEntry(entry);
        }
    }



}
