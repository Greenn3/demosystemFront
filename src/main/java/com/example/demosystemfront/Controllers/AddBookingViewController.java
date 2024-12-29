package com.example.demosystemfront.Controllers;

import com.example.demosystemfront.ApiService;
import com.example.demosystemfront.Entities.AccType;
import com.example.demosystemfront.Entities.Booking;
import com.example.demosystemfront.Menu;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;


public class AddBookingViewController {


    public Menu menu = new Menu();
    private Stage primaryStage;
    private ApiService apiService = new ApiService();

    // Form components
    private Label labelName = new Label("Imię i nazwisko:");
    private TextField textFieldName = new TextField();
    private Label labelArrivalDate = new Label("Data przyjazdu:");
    private DatePicker datePickerArrivalDate = new DatePicker();
    private Label labelDepartureDate = new Label("Data wyjazdu:");
    private DatePicker datePickerDepartureDate = new DatePicker();
    private Label labelType = new Label("Typ noclegu:");
    private ComboBox<AccType> comboBox = new ComboBox<>();
    private Label labelPhone = new Label("Numer telefonu:");
    private TextField textFieldPhone = new TextField();
    private Label labelEmail = new Label("Adres email:");
    private TextField textFieldEmail = new TextField();
    private Label labelInfo = new Label("Dodatkowe informacje:");
    private TextArea textAreaInfo = new TextArea();
    private TextField textFieldPrice = new TextField();
    private Label labelPrice = new Label("Cena:");

    public AddBookingViewController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void clearFields() {
        textFieldName.clear();
        datePickerArrivalDate.setValue(null);
        datePickerDepartureDate.setValue(null);
        comboBox.setValue(null);
        textFieldPhone.clear();
        textFieldEmail.clear();
        textAreaInfo.clear();
        textFieldPrice.clear();
    }

    public Scene createContent() {
        BorderPane mainPane = new BorderPane();
       VBox menuBox = menu.showMenu(primaryStage);
        Label labelSuccessInfo = new Label("");
        try {
            comboBox.getItems().addAll(apiService.loadAllAccTypes());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

// Customize the cells in the dropdown list
        comboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<AccType> call(ListView<AccType> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(AccType item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(item == null || empty ? null : item.getName());
                    }
                };
            }
        });

// Customize the display for the selected item in the ComboBox button
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(AccType item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null || empty ? null : item.getName());
            }
        });

//

        Button checkPriceButton = new Button("Sprawdź cenę");
        checkPriceButton.setOnAction(e -> {
            Booking booking = new Booking(textFieldName.getText(), datePickerArrivalDate.getValue(), datePickerDepartureDate.getValue(), comboBox.getValue());
            textFieldPrice.setText(apiService.getPrice(booking).toString());
        });

        Button saveButton = new Button("Zapisz");
        saveButton.getStyleClass().add("button-save");

        saveButton.setOnAction(e -> {
            boolean isOK = true;
            Booking booking = new Booking(
                    textFieldName.getText(),
                    datePickerArrivalDate.getValue(),
                    datePickerDepartureDate.getValue(),
                    comboBox.getValue(),
                    textFieldPhone.getText(),
                    textFieldEmail.getText(),
                    textAreaInfo.getText());

            if(datePickerArrivalDate.getValue() == null ){
                isOK = false;
                datePickerArrivalDate.getStyleClass().add("error");
                labelSuccessInfo.setText("Wypełnij wymagane pola");
            }
            if(datePickerDepartureDate.getValue() == null ) {
                isOK = false;
                datePickerDepartureDate.getStyleClass().add("error");
                labelSuccessInfo.setText("Wypełnij wymagane pola");
            }
            if (comboBox.getValue() == null) {
                isOK = false;
                comboBox.getStyleClass().add("error");
                labelSuccessInfo.setText("Wypełnij wymagane pola");
            }
            if (textFieldName.getText().isEmpty()) {
                isOK = false;
                textFieldName.getStyleClass().add("error");
                labelSuccessInfo.setText("Wypełnij wymagane pola");
            }
            if(datePickerArrivalDate.getValue().isAfter(datePickerDepartureDate.getValue())){
                isOK = false;
                datePickerArrivalDate.getStyleClass().add("error");
                datePickerDepartureDate.getStyleClass().add("error");
                labelSuccessInfo.setText("Data wyjazdu nie może być wcześniejsza niż data przyjazdu");
            }
if(comboBox.getValue().getId() == 1 || comboBox.getValue().getId() == 2 || comboBox.getValue().getId() == 3) {
    System.out.println("Id matches the type");
    System.out.println("bool: " + apiService.checkIfFree(datePickerArrivalDate.getValue(), datePickerDepartureDate.getValue(), comboBox.getValue().getId()));
    if (apiService.checkIfFree(datePickerArrivalDate.getValue(), datePickerDepartureDate.getValue(), comboBox.getValue().getId()) == false) {
        isOK = false;
        System.out.println("isOK: " + isOK);
        labelSuccessInfo.setText(".....");
        labelSuccessInfo.setText("Wybrany termin jest zajęty");
        labelSuccessInfo.getStyleClass().add("error-info");
    }
}

            if(isOK) {
                int response = apiService.saveOneBookingToDatabase(booking);
                if (response == 200) {
                    labelSuccessInfo.setText("Pomyślnie dodano rezerwację");
                    labelSuccessInfo.getStyleClass().add("success-info");
                    clearFields();

                } else {
                    labelSuccessInfo.setText("Coś poszło nie tak, rezerwacja nie została dodana");
                    labelSuccessInfo.getStyleClass().add("error-info");
                }
            }


        });

        // Split form fields into two columns
        VBox leftColumn = new VBox(10, labelName, textFieldName ,labelPhone, textFieldPhone, labelEmail, textFieldEmail);
        VBox rightColumn = new VBox(10,  labelArrivalDate, datePickerArrivalDate, labelDepartureDate, datePickerDepartureDate, labelType, comboBox);
        VBox thirdColumn = new VBox(10, labelPrice, textFieldPrice, labelInfo, textAreaInfo);
textAreaInfo.setMaxWidth(300);
leftColumn.getStyleClass().add("form-column");
rightColumn.getStyleClass().add("form-column");
thirdColumn.getStyleClass().add("form-column");

        datePickerArrivalDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && datePickerDepartureDate.getValue() != null && comboBox.getValue() != null) {
               textFieldPrice.setText(apiService.getPrice(new Booking(textFieldName.getText(), datePickerArrivalDate.getValue(), datePickerDepartureDate.getValue(), comboBox.getValue())).toString());
            }
        });
        datePickerDepartureDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && datePickerArrivalDate.getValue() != null && comboBox.getValue() != null) {
                textFieldPrice.setText(apiService.getPrice(new Booking(textFieldName.getText(), datePickerArrivalDate.getValue(), datePickerDepartureDate.getValue(), comboBox.getValue())).toString());
            }
        });
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && datePickerArrivalDate.getValue() != null && datePickerDepartureDate.getValue() != null) {
                textFieldPrice.setText(apiService.getPrice(new Booking(textFieldName.getText(), datePickerArrivalDate.getValue(), datePickerDepartureDate.getValue(), comboBox.getValue())).toString());
            }
        });
        // Add both columns into a horizontal container
        HBox formColumns = new HBox(20, leftColumn, rightColumn, thirdColumn);
        formColumns.setPadding(new Insets(20));

        VBox fullLayout = new VBox(10, formColumns,  saveButton, labelSuccessInfo);


VBox cover = new VBox();

        // Wrap the main layout in a ScrollPane
        HBox topPanel = Menu.showTopPanel();
        VBox smallerBox = new VBox();
        smallerBox.getChildren().add(fullLayout);
        ScrollPane scrollPane = new ScrollPane(fullLayout);
        fullLayout.getStyleClass().add("full-layout");
       scrollPane.getStyleClass().add("scroll-pane-add");
        scrollPane.setFitToWidth(true);
        cover.getChildren().add(smallerBox);
        smallerBox.getStyleClass().add("smaller-box");

        mainPane.setTop(topPanel);
        mainPane.setLeft(menuBox);
cover.getStyleClass().add("cover");

        mainPane.setCenter(cover);



        Scene scene = new Scene(mainPane, 1200, 750);
        scene.getStylesheets().add(getClass().getResource("/nextStyle.css").toExternalForm());

        return scene;
    }
}
