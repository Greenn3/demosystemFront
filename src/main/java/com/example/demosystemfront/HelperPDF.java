package com.example.demosystemfront;

import com.example.demosystemfront.Entities.Booking;
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
                writer.append("Name;Arrival Date;Departure Date;Price\n");
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
                writer.append("\nTotal Price,,,").append(String.valueOf(totalPrice)).append("\n");

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
                contentStream.setFont(serifBold, 24);
                contentStream.newLineAtOffset(200, yOffSet+ 700);
                contentStream.showText("Booking Confirmation");
                contentStream.endText();

                // Draw a horizontal line under the title
                contentStream.moveTo(50, yOffSet + 680);
                contentStream.lineTo(550, yOffSet + 680);
                contentStream.stroke();

                // Add booking details with a more professional layout
                contentStream.beginText();
                contentStream.setFont(serif, 12);
                contentStream.setLeading(14.5f); // Line spacing
                contentStream.newLineAtOffset(50, yOffSet+ 650);
                contentStream.showText("Guest Information:");
                contentStream.newLine();
                contentStream.setFont(serif, 12);
                contentStream.showText("Name: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getName());
                contentStream.newLine();
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Phone: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getPhone());
                contentStream.newLine();
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Email: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getEmail());

                contentStream.newLine();
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Accommodation Details:");
                contentStream.newLine();
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Type: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getAccType().getName());
                contentStream.newLine();
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Check-in: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getArrivalDate().toString());
                contentStream.newLine();
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Check-out: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getDepartureDate().toString());

                contentStream.newLine();
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Special Requests: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getInfo());

                contentStream.newLine();
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Total Price: ");
                contentStream.setFont(serif, 12);
                contentStream.showText(apiService.getPrice(booking).toString() + " EUR");

                contentStream.endText();

                // Draw another horizontal line to separate sections
                contentStream.moveTo(50, yOffSet + 500);
                contentStream.lineTo(550, yOffSet + 500);
                contentStream.stroke();

                // Add footer
                contentStream.beginText();
                contentStream.setFont(serif, 10);
                contentStream.newLineAtOffset(50, yOffSet + 200);
                contentStream.showText("Thank you for your booking! For any inquiries, contact us at info@hotel.com or call +123456789.");
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

        int yOffSet = -150;
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

                // Add a logo or header graphic (you need an image file for this)
                try (InputStream logoImageStream = new FileInputStream("C:\\Users\\Asia\\IdeaProjects\\demosystemFront\\src\\main\\resources\\header.png")) {
                    PDImageXObject logoImage = PDImageXObject.createFromByteArray(document, logoImageStream.readAllBytes(), "logo");
                    contentStream.drawImage(logoImage, 0, 590, 575, 200); // Position: (x, y), Size: (width, height)
                } catch (IOException e) {
                    System.out.println("Logo not found: " + e.getMessage());
                }

                // Set up the title for the invoice
                contentStream.beginText();
                PDFont serif = PDType0Font.load(document, new File(serifFontPath));
                PDFont serifBold = PDType0Font.load(document, new File(serifBoldFontPath));
                contentStream.setFont(serifBold, 24);
                contentStream.newLineAtOffset(200, yOffSet + 700);
                contentStream.showText("Rachunek ąćźżęł");
                contentStream.endText();

                // Draw a horizontal line under the title
                contentStream.moveTo(50, yOffSet + 680);
                contentStream.lineTo(550, yOffSet + 680);
                contentStream.stroke();

                // Add Company Information and Invoice Details
                contentStream.beginText();
                contentStream.setFont(serif, 12);
                contentStream.setLeading(14.5f); // Line spacing
                contentStream.newLineAtOffset(50, yOffSet + 650);
                contentStream.showText("Nazwa hotelu");
                contentStream.newLine();
                contentStream.showText("Adres linia 1");
                contentStream.newLine();
                contentStream.showText("Adres linia 2, Miejscowość, Kod pocztowy");
                contentStream.newLine();
                contentStream.showText("Numer telefonu: +123456789");
                contentStream.newLine();
                contentStream.showText("E-mail: billing@hotel.com");

                // Invoice number and Date
                contentStream.newLine();
                contentStream.newLine();
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Numer rachunku: ");
                contentStream.setFont(serif, 12);
                contentStream.showText("INV-" + System.currentTimeMillis());
                contentStream.newLine();
                contentStream.setFont(serif, 12);
                contentStream.showText("Data: ");
                contentStream.setFont(serifBold, 12);
                contentStream.showText(java.time.LocalDate.now().toString());

                // Add customer information
                contentStream.newLine();
                contentStream.newLine();
                contentStream.setFont(serifBold, 12);
                contentStream.showText("Imię i nazwisko gościa:");
                contentStream.newLine();
                contentStream.setFont(serif, 12);
                contentStream.showText(booking.getName());
                contentStream.newLine();
                contentStream.showText("Telefon: " + booking.getPhone());
                contentStream.newLine();
                contentStream.showText("E-mail: " + booking.getEmail());
                contentStream.endText();

                // Draw a line to separate sections
                contentStream.moveTo(50, yOffSet + 500);
                contentStream.lineTo(550, yOffSet + 500);
                contentStream.stroke();

                // Invoice table headers
                contentStream.beginText();
                contentStream.setFont(serifBold, 12);
                contentStream.newLineAtOffset(50, yOffSet + 470);
                contentStream.showText("Opis:");
                contentStream.newLineAtOffset(300, 0); // Move across horizontally
                contentStream.showText("Kwota (EUR)");
                contentStream.endText();

                // Draw a line under the headers
                contentStream.moveTo(50, yOffSet + 460);
                contentStream.lineTo(550, yOffSet + 460);
                contentStream.stroke();

                // Itemized list (modify this to match real services in your booking system)
                contentStream.beginText();
                contentStream.setFont(serif, 12);
                contentStream.newLineAtOffset(50, yOffSet + 440);
                contentStream.showText("Rodzaj usługi: usługa noclegowa - " + booking.getAccType().getName());
                contentStream.newLineAtOffset(300, 0);
                contentStream.showText(apiService.getPrice(booking).toString());
                contentStream.newLineAtOffset(-300, -20); // Back to left and move down

                // Example Service Fees
                contentStream.showText("Opłaty dodatkowe");
                contentStream.newLineAtOffset(300, 0);
                contentStream.showText("0.0");
                contentStream.newLineAtOffset(-300, -20);

                // Example Taxes
                double price = Double.parseDouble(apiService.getPrice(booking));
                double taxAmount = price * 0.10;
                contentStream.showText("Podatek (10%)");
                contentStream.showText("Kwota podatku: " + taxAmount);
                contentStream.newLineAtOffset(300, 0);

                contentStream.showText(String.format("%.2f", taxAmount));
                contentStream.newLineAtOffset(-300, -20);

                contentStream.endText();

                // Calculate Total
                double totalAmount = price;
                        // Double.parseDouble(apiService.getPrice(booking) + 50.0 + taxAmount);

                // Draw a line above the total
                contentStream.moveTo(50, yOffSet + 340);
                contentStream.lineTo(550, yOffSet + 340);
                contentStream.stroke();

                // Display Total Amount
                contentStream.beginText();
                contentStream.setFont(serifBold, 12);
                contentStream.newLineAtOffset(50, yOffSet + 320);
                contentStream.showText("Total Amount Due:");
                contentStream.newLineAtOffset(300, 0);
                contentStream.showText(String.format("%.2f EUR", totalAmount));
                contentStream.endText();

                // Draw another horizontal line to separate sections
                contentStream.moveTo(50, yOffSet + 300);
                contentStream.lineTo(550, yOffSet + 300);
                contentStream.stroke();

                // Footer Information
                contentStream.beginText();
                contentStream.setFont(serif, 10);
                contentStream.newLineAtOffset(50, yOffSet + 200);
                contentStream.showText("Thank you for your stay! For any billing inquiries, contact us at billing@hotel.com or call +123456789.");
                contentStream.newLine();
                contentStream.showText("Please pay within 30 days. Late payments may incur a 5% late fee.");
                contentStream.endText();

                // Close the content stream
                contentStream.close();

                // Save the document after adding content
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
