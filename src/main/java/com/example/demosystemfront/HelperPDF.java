package com.example.demosystemfront;

import com.example.demosystemfront.Entities.Booking;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.util.List;

import java.io.IOException;

import java.io.*;

import static java.awt.Color.BLACK;
import static java.awt.Color.BLUE;
import static javafx.scene.paint.Color.BLUEVIOLET;

public class HelperPDF {
ApiService apiService = new ApiService();
  public String serifFontPath = "C:\\Users\\Asia\\IdeaProjects\\demosystemFront\\src\\main\\resources\\DejaVuLGCSerif.ttf";
  public String serifBoldFontPath = "C:\\Users\\Asia\\IdeaProjects\\demosystemFront\\src\\main\\resources\\DejaVuLGCSerif-Bold.ttf";

//    public void generatedocumentXML(List<Booking> list, Stage stage) {
//        // Define the filename and path
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save Excel File");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
//        File file = fileChooser.showSaveDialog(stage);
//
//        if (file != null) {
//            try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(file)) {
//                // Create a sheet
//                Sheet sheet = workbook.createSheet("Booking List");
//
//                // Create header row
//                Row headerRow = sheet.createRow(0);
//                String[] headers = {"Name", "Arrival Date", "Departure Date", "Price"};
//                for (int i = 0; i < headers.length; i++) {
//                    Cell cell = headerRow.createCell(i);
//                    cell.setCellValue(headers[i]);
//                }
//
//                // Add booking details to the sheet
//                double totalPrice = 0;
//                int rowIndex = 1;
//
//                for (Booking booking : list) {
//                    Row bookingRow = sheet.createRow(rowIndex++);
//                    bookingRow.createCell(0).setCellValue(booking.getName());
//                    bookingRow.createCell(1).setCellValue(booking.getArrivalDate().toString());
//                    bookingRow.createCell(2).setCellValue(booking.getDepartureDate().toString());
//
//                    double price = Double.parseDouble(apiService.getPrice(booking));
//                    bookingRow.createCell(3).setCellValue(price);
//                    totalPrice += price; // Accumulate total price
//                }
//
//                // Add total price at the bottom
//                Row totalRow = sheet.createRow(rowIndex);
//                totalRow.createCell(0).setCellValue("Total Price");
//                totalRow.createCell(3).setCellValue(totalPrice); // Add total price
//
//                // Write the workbook to the file
//                workbook.write(fileOut);
//                System.out.println("Excel file created successfully");
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
    public void generateDocument(List<Booking> bookingList, Stage stage) {
        // FileChooser to let the user choose where to save the file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Write CSV headers
                writer.append("Nazwisko;Data przyjazdu;Data wyjazdu;Cena\n");
                // Initialize total price
                double totalPrice = 0.0;

                // Iterate through each booking and write the details
                for (Booking booking : bookingList) {
                    double price = Double.parseDouble(apiService.getPrice(booking)); // Get price from the service
                    totalPrice += price; // Add to the total price

                    // Write each booking to the CSV file
                    writer.append(booking.getName()).append(";")
                            .append(booking.getArrivalDate().toString()).append(";")
                            .append(booking.getDepartureDate().toString()).append(";")
                            .append(String.valueOf(price)).append("\n");
                }

                // Write the total price at the bottom of the CSV file
                writer.append("\nSUma,,,").append(String.valueOf(totalPrice)).append("\n");

                System.out.println("CSV file created successfully at: " + file.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void generateConfirmationPDF(Booking booking, Stage stage) {

        int yOffSet = -150;
        ApiService apiService = new ApiService();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                PDFont serif = PDType0Font.load(document, new File(serifFontPath));
                PDFont serifBold = PDType0Font.load(document, new File(serifBoldFontPath));
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                // Add a logo or header graphic (you need an image file for this)
                try (InputStream logoImageStream = new FileInputStream("C:\\Users\\Asia\\IdeaProjects\\demosystemFront\\src\\main\\resources\\header.png")) {
                    PDImageXObject logoImage = PDImageXObject.createFromByteArray(document, logoImageStream.readAllBytes(), "logo");
                    contentStream.drawImage(logoImage, 0, 590, 575, 200); // Position: (x, y), Size: (width, height)
                } catch (IOException e) {
                    System.out.println("Logo not found: " + e.getMessage());
                }

                // Set up the title
                contentStream.beginText();
                contentStream.setNonStrokingColor(BLUE);
                contentStream.setFont(serifBold, 24);
                contentStream.newLineAtOffset(150, yOffSet+ 700);
                contentStream.newLine();
                contentStream.showText("");
                contentStream.showText("Potwierdzenie rezerwacji");
                contentStream.newLine();
                contentStream.showText("");
                contentStream.endText();

                // Draw a horizontal line under the title
                contentStream.setNonStrokingColor(BLACK);
                contentStream.setStrokingColor(BLUE);
                contentStream.moveTo(50, yOffSet + 680);
                contentStream.lineTo(550, yOffSet + 680);
                contentStream.stroke();

                // Add booking details with a more professional layout
                contentStream.beginText();
                contentStream.setFont(serifBold, 14);
                contentStream.setLeading(14.5f); // Line spacing
                contentStream.newLineAtOffset(50, yOffSet+ 650);
                contentStream.setNonStrokingColor(BLUE);
                contentStream.showText("Dane gościa:");
                contentStream.newLine();
                contentStream.showText("");
                contentStream.newLine();
                contentStream.setNonStrokingColor(BLACK);
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Imie i nazwisko: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getName());
                contentStream.newLine();
                contentStream.newLine();
                contentStream.showText("");
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Telefon: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getPhone());
                contentStream.newLine();
                contentStream.showText("");
                contentStream.newLine();
                contentStream.setFont(serifBold, 12);
                contentStream.showText("E-mail: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getEmail());
                contentStream.newLine();
                contentStream.showText("");

                contentStream.newLine();
                contentStream.setNonStrokingColor(BLUE);
                contentStream.setFont(serifBold, 14);
                contentStream.showText("Szczegóły noclegu:");
                contentStream.newLine();
                contentStream.showText("");
                contentStream.newLine();
                contentStream.setNonStrokingColor(BLACK);
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Rodzaj: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getAccType().getName());
                contentStream.newLine();
                contentStream.showText("");
                contentStream.newLine();
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Zameldowanie: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getArrivalDate().toString());
                contentStream.newLine();
                contentStream.showText("");
                contentStream.newLine();
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Wymeldowanie: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getDepartureDate().toString());
                contentStream.newLine();
                contentStream.showText("");

                contentStream.newLine();
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Dodatkowe informacje: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getInfo());
                contentStream.newLine();
                contentStream.showText("");

                contentStream.newLine();
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Cena całkowita: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(apiService.getPrice(booking).toString() + " PLN");

                contentStream.endText();

                // Draw another horizontal line to separate sections
               contentStream.setStrokingColor(BLUE);
                contentStream.moveTo(50, 200);
                contentStream.lineTo(550,  200);
                contentStream.stroke();

                // Add footer

                contentStream.beginText();
                contentStream.setFont(serif, 10);
                contentStream.newLineAtOffset(50, yOffSet + 200);
                contentStream.showText("Dziękujemy za dokonanie rezerwacji!");
                contentStream.newLine();
                contentStream.showText(" W razie jakichkolwiek pytań zapraszmy do kontaktu: info@hotel.com lub +123456789.");
                contentStream.endText();

                // Close the content stream
                contentStream.close();

                // Save the document after adding content
                document.save(file);
                System.out.println("PDF created successfully");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void generateInvoicePDF(Booking booking, Stage stage) {

        ApiService apiService = new ApiService();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                // Add a logo or header graphic
                try (InputStream logoImageStream = new FileInputStream("C:\\Users\\Asia\\IdeaProjects\\demosystemFront\\src\\main\\resources\\header.png")) {
                    PDImageXObject logoImage = PDImageXObject.createFromByteArray(document, logoImageStream.readAllBytes(), "logo");
                    contentStream.drawImage(logoImage, 40, 650, 500, 180); // Logo at the top
                } catch (IOException e) {
                    System.out.println("Logo not found: " + e.getMessage());
                }

                // Title
                contentStream.beginText();
                PDFont serif = PDType0Font.load(document, new File(serifFontPath));
                PDFont serifBold = PDType0Font.load(document, new File(serifBoldFontPath));
                contentStream.setFont(serifBold, 24);
                contentStream.newLineAtOffset(230, 620);
                contentStream.showText("Rachunek");
                contentStream.endText();

                // Horizontal line under the title
                contentStream.moveTo(50, 600);
                contentStream.lineTo(550, 600);
                contentStream.stroke();

                // Hotel Details
                contentStream.beginText();
                contentStream.setFont(serif, 12);
                contentStream.setLeading(16.5f);
                contentStream.newLineAtOffset(50, 580);
                contentStream.showText("Nazwa hotelu");
                contentStream.newLine();
                contentStream.showText("Adres linia 1");
                contentStream.newLine();
                contentStream.showText("Adres linia 2, Miejscowość, Kod pocztowy");
                contentStream.newLine();
                contentStream.showText("Numer telefonu: +123456789");
                contentStream.newLine();
                contentStream.showText("E-mail: billing@hotel.com");
                contentStream.endText();

                // Invoice Information
                contentStream.beginText();
                contentStream.setFont(serifBold, 12);
                contentStream.newLineAtOffset(50, 480);
                contentStream.showText("Numer rachunku: INV-" + System.currentTimeMillis());
                contentStream.newLine();
                contentStream.showText("Data: " + java.time.LocalDate.now());
                contentStream.endText();

                // Customer Information
                contentStream.beginText();
                contentStream.setFont(serifBold, 12);
                contentStream.newLineAtOffset(50, 435);
                contentStream.showText("Imię i nazwisko gościa:");
                contentStream.newLine();
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getName());
                contentStream.newLine();
                contentStream.showText("Telefon: " + booking.getPhone());
                contentStream.newLine();
                contentStream.showText("E-mail: " + booking.getEmail());
                contentStream.endText();

                // Horizontal line to separate sections
                contentStream.moveTo(50, 375);
                contentStream.lineTo(550, 375);
                contentStream.stroke();

                // Invoice Table Headers
                contentStream.beginText();
                contentStream.setFont(serifBold, 12);
                contentStream.newLineAtOffset(50, 365);
                contentStream.showText("Opis");
                contentStream.newLineAtOffset(300, 0); // Move to the right for amount
                contentStream.showText("Kwota (EUR)");
                contentStream.endText();

                // Line under headers
                contentStream.moveTo(50, 360);
                contentStream.lineTo(550, 360);
                contentStream.stroke();

                // Itemized List
                contentStream.beginText();
                contentStream.setFont(serif, 12);
                contentStream.newLineAtOffset(50, 345);
                contentStream.showText("Rodzaj usługi: usługa noclegowa - " + booking.getAccType().getName());
                contentStream.newLineAtOffset(300, 0);
                contentStream.showText(apiService.getPrice(booking).toString());
                contentStream.newLineAtOffset(-300, -20); // Back to the left and move down
                contentStream.showText("Opłaty dodatkowe");
                contentStream.newLineAtOffset(300, 0);
                contentStream.showText("0.0");
                contentStream.newLineAtOffset(-300, -20);
                contentStream.showText("Podatek (10%)");
                contentStream.newLineAtOffset(300, 0);
                double price = Double.parseDouble(apiService.getPrice(booking));
                double taxAmount = price * 0.10;
                contentStream.showText(String.format("%.2f", taxAmount));
                contentStream.endText();

                // Horizontal line before Total
                contentStream.moveTo(50, 280);
                contentStream.lineTo(550, 280);
                contentStream.stroke();

                // Total Amount
                contentStream.beginText();
                contentStream.setFont(serifBold, 12);
                contentStream.newLineAtOffset(50, 260);
                contentStream.showText("Całkowita kwota:");
                contentStream.newLineAtOffset(300, 0);
                contentStream.showText(String.format("%.2f EUR", price + taxAmount));
                contentStream.endText();

                // Footer
                contentStream.beginText();
                contentStream.setFont(serif, 10);
                contentStream.newLineAtOffset(50, 140);
                contentStream.showText("Dziękujemy za pobyt!");
                contentStream.newLine();
                contentStream.showText("W razie jakichkolwiek pytań zapraszmy do kontaktu: info@hotel.com lub +123456789.");
                contentStream.endText();

                // Close the content stream
                contentStream.close();

                // Save the document
                document.save(file);
                System.out.println("Invoice PDF created successfully");

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
