package com.example.demosystemfront.Controllers;

import com.example.demosystemfront.ApiService;
import com.example.demosystemfront.Entities.AccType;
import com.example.demosystemfront.Entities.Booking;
import com.example.demosystemfront.HelperPDF;
import com.example.demosystemfront.Menu;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.scene.control.ScrollPane;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

public class BookingInfoController {
    Menu menu = new Menu();
    HelperPDF helperPDF = new HelperPDF();
    ApiService api = new ApiService();
    Booking booking;
    ComboBox<AccType> comboBox = new ComboBox<>();
    private Stage primaryStage;
    private VBox firstColumn;

    public BookingInfoController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene createContent(Integer id) {
      VBox menuBox  = menu.showMenu(primaryStage);
        BorderPane mainPane = new BorderPane();
        BooleanProperty editable = new SimpleBooleanProperty(false);

        VBox mainContainer = new VBox(15);
        mainContainer.setPadding(new Insets(20));

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



        MenuButton generateDocumentMenu = new MenuButton("Wygeneruj dokument");
        MenuItem confirmation = new MenuItem("Potwierdzenie rezerwacji PDF");
        confirmation.setOnAction(event -> helperPDF.generateConfirmationPDF(booking, primaryStage));
        MenuItem invoice = new MenuItem("Rachunek PDF");
        invoice.setOnAction(event -> helperPDF.generateInvoicePDF(booking, primaryStage));
        generateDocumentMenu.getItems().addAll(confirmation, invoice);

        // Header Section with all buttons aligned horizontally


        // Editable Fields and Labels
        TextField name = new TextField(booking.name);
        name.editableProperty().bind(editable);

        DatePicker arrivalDatePicker = new DatePicker(booking.arrivalDate);
        arrivalDatePicker.editableProperty().bind(editable);
        arrivalDatePicker.disableProperty().bind(editable.not());

        DatePicker departureDatePicker = new DatePicker(booking.departureDate);
        departureDatePicker.editableProperty().bind(editable);
        departureDatePicker.disableProperty().bind(editable.not());

        try {
            List<AccType> list = api.loadAllAccTypes();
            comboBox.getItems().addAll(list);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        comboBox.setValue(booking.accType);
        comboBox.editableProperty().bind(editable);
        comboBox.disableProperty().bind(editable.not());

        comboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<AccType> call(ListView<AccType> param) {
                return new ListCell<>() {
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

        comboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(AccType object) {
                return object != null ? object.getName() : "";
            }

            @Override
            public AccType fromString(String string) {
                return comboBox.getItems().stream()
                        .filter(accType -> accType.getName().equals(string))
                        .findFirst().orElse(null);
            }
        });

        TextField price = new TextField(api.getPrice(booking));
        price.setEditable(false);

        CheckBox discount = new CheckBox();
        discount.disableProperty().bind(editable.not());

        TextField discountAmount = new TextField();


        TextArea addInfo = new TextArea(booking.getInfo());
        addInfo.editableProperty().bind(editable);

        TextField phone = new TextField(booking.getPhone());
        TextField email = new TextField(booking.getEmail());
        CheckBox paid = new CheckBox("Zapłacono");
        CheckBox arrived = new CheckBox("Przyjechał");
        CheckBox left = new CheckBox("Wyjechał");
        ToggleButton editModeButton = new ToggleButton("Włącz edytowanie");
        editModeButton.setOnAction(e -> editable.set(!editable.get()));
Button deleteButton = new Button("Usuń rezerwację");
deleteButton.setOnAction(e -> {
    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
    confirmationAlert.setTitle("Potwierdzenie");
    confirmationAlert.setHeaderText("Jesteś pewien?");
    confirmationAlert.setContentText("Czy napewno chcesz usunąć rezerwację?");

    // Show the alert and wait for a response
    Optional<ButtonType> result = confirmationAlert.showAndWait();

    if (result.isPresent() && result.get() == ButtonType.OK) {
        // User confirmed the deletion
        System.out.println("Usuwanie");
        int i = api.deleteBooking(id);
        System.out.println(i);
        System.out.println(id);

        // Navigate back to the menu
        MenuController menuController = new MenuController(primaryStage);
        try {
            primaryStage.setScene(menuController.createContent());
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    } else {
        // User canceled the deletion
        System.out.println("Deletion canceled by user.");
    }
});
        // Save and Generate PDF Buttons
        Button save = new Button("Zapisz zmiany");
        save.setOnAction(e -> {
            Booking editedBooking = new Booking(id,
                    name.getText(),
                    arrivalDatePicker.getValue(),
                    departureDatePicker.getValue(),
                    comboBox.getValue(),
                    addInfo.getText(),
                    Double.parseDouble(discountAmount.getText()),
                    phone.getText(),
                    email.getText(),
                    arrived.isSelected(),
                    paid.isSelected(),
                    left.isSelected());
            api.saveOneBookingToDatabase(editedBooking);
        });
        HBox header = new HBox();
        header.getStyleClass().add("info-header-hbox");

        header.setSpacing(10); // Adds spacing between elements


HBox controls = new HBox();
controls.getStyleClass().add("controls-hbox");
controls.getChildren().addAll( arrived,paid, left);

arrived.disableProperty().bind(editable.not());
paid.disableProperty().bind(editable.not());
left.disableProperty().bind(editable.not());
discountAmount.disableProperty().bind(editable.not());
// Spacer to push buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

deleteButton.getStyleClass().add("delete-button");
        header.getChildren().addAll(
                controls,
              //  spacer,
                editModeButton,
                save,
                generateDocumentMenu,
                deleteButton);
      //  mainContainer.getChildren().add(header);
email.editableProperty().bind(editable);
phone.editableProperty().bind(editable);
        // Columns for the main booking details
        VBox firstColumn = new VBox(10,
                new VBox(5, new Label("Nazwisko"), name),
              new VBox(5, new Label("E-mail"), email),
                new VBox(5, new Label("Telefon"), phone)

        );

        VBox secondColumn = new VBox(10,
                new VBox(5, new Label("Data przyjazdu"), arrivalDatePicker),
                new VBox(5, new Label("Data wyjazdu"), departureDatePicker),
                new VBox(5, new Label("Typ zakwaterowania"), comboBox)


        );
        TextField priceAfterDiscount = new TextField();
        Button checkPriceButton = new Button("zastosuj");
        checkPriceButton.setOnAction(e -> {
            Double discountValue = Double.parseDouble(discountAmount.getText());
            Double priceValue = Double.parseDouble(price.getText());
            Double finalPrice = priceValue * (1 - discountValue / 100);
         priceAfterDiscount.setText(finalPrice.toString());
        });

        VBox thirdColumn = new VBox(10,
                 new VBox(5, new Label("Cena"), price),
                new VBox(5, new Label("Zniżka"), new HBox(5, discount, discountAmount, checkPriceButton)),
                new VBox(5, priceAfterDiscount)
        );
        VBox forthColumn = new VBox(10,
                new VBox(5, new Label("Dodatkowe informacje"), addInfo)
        );
firstColumn.getStyleClass().add("form-column-info");
secondColumn.getStyleClass().add("form-column-info");
thirdColumn.getStyleClass().add("form-column-info");
forthColumn.getStyleClass().add("form-column-info");
        HBox mainColumns = new HBox(20, firstColumn, secondColumn, thirdColumn);
        VBox mainColumnsVbox = new VBox(20, firstColumn, secondColumn, thirdColumn);

        GridPane mainColumnsGrid = new GridPane();
        mainColumnsGrid.add(firstColumn, 0, 0);
        mainColumnsGrid.add(secondColumn, 1, 0);
        mainColumnsGrid.add(thirdColumn, 0, 1);
        mainColumnsGrid.add(forthColumn, 1, 1);
mainColumnsGrid.setHgap(40);
mainColumnsGrid.setVgap(40);
mainColumnsGrid.getStyleClass().add("main-columns-grid");

        mainContainer.getChildren().addAll(
                header,
                mainColumnsGrid
        );

        // Scrollable container for the content
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);

        VBox smallerBox = new VBox();
        smallerBox.getStyleClass().add("smaller-box");
        smallerBox.getChildren().add(mainContainer);
        mainContainer.getStyleClass().add("main-container");
        VBox cover = new VBox();
        cover.getChildren().add(smallerBox);
        cover.getStyleClass().add("cover");
        mainPane.setLeft(menuBox);
        mainPane.setCenter(cover);
        mainPane.setTop(menu.showTopPanel());
        Scene scene = new Scene(mainPane, 1200, 750);
        scene.getStylesheets().add(getClass().getResource("/nextStyle.css").toExternalForm());
        return scene;
    }
}
