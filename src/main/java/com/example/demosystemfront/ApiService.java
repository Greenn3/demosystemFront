package com.example.demosystemfront;
import com.example.demosystemfront.Entities.AccType;
import com.example.demosystemfront.Entities.Booking;
import com.example.demosystemfront.Entities.PricePerType;
import com.example.demosystemfront.Entities.PricePeriod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http. HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApiService {

    //dla daty, po dacie przyjazdu

AlertWindow alertWindow = new AlertWindow();


private static final String key = "rjfghreohgaojfjeorjpw45i945jijJDI3J";
    public List<Booking> findReservationsByArrivalDate(LocalDate arrivalDate) {
        List<Booking> arrivingReservations;

        try {
            // Encode the date parameter
            String encodedDate = URLEncoder.encode(arrivalDate.toString(), StandardCharsets.UTF_8);

            // Create HttpClient instance
            HttpClient client = HttpClient.newBuilder().build();

            // Create request URI with date parameter
            String url = "http://localhost:8080/getBookingByArrivalDate?date=" + encodedDate;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + key)
                    .build();

            // Send request and get response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Print the JSON response for debugging
            System.out.println("JSON Response: " + response.body());

            // Check if the response body is empty
            if (response.body().isEmpty()) {
                return List.of();
            }

            // Configure Gson with a LocalDate adapter
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                    .create();

            // Define the type for the list of bookings
            Type listType = new TypeToken<List<Booking>>() {}.getType();

            // Deserialize the response body to a list of Booking objects
            arrivingReservations = gson.fromJson(response.body(), listType);

            return arrivingReservations;

       } catch (IOException | InterruptedException  e) {
          //  showAutoCloseErrorPopup("Error", "Something went wrong!", 5); // Shows for 3 seconds
            alertWindow.showNotification("Brak połączenia z serwerem", 5);
        }

      //  Return an empty list in case of an exception
        return List.of();
    }

    public List<Booking> findReservationsByDepartureDate(LocalDate departureDate) throws IOException, InterruptedException {
        List<Booking> departingReservations;
try {
    // Encode the date parameter
    String encodedDate = URLEncoder.encode(departureDate.toString(), StandardCharsets.UTF_8);

    // Create HttpClient instance
    HttpClient client = HttpClient.newBuilder().build();

    // Create request URI with date parameter
    String url = "http://localhost:8080/getBookingByDepartureDate?date=" + encodedDate;
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + key)
            .build();

    // Send request and get response
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    // Print the JSON response for debugging
    System.out.println("JSON Response: " + response.body());

    // Check if the response body is empty
    if (response.body().isEmpty()) {
        return List.of();
    }

    // Configure Gson with a LocalDate adapter
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .create();

    // Define the type for the list of bookings
    Type listType = new TypeToken<List<Booking>>() {
    }.getType();

    // Deserialize the response body to a list of Booking objects
    departingReservations = gson.fromJson(response.body(), listType);

    return departingReservations;
}
 catch (IOException | InterruptedException  e) {
     alertWindow.showNotification("Brak połączenia z serwerem", 5);
        }

        // Return an empty list in case of an exception
        return List.of();
    }



  //lista wszystkich rezerwacji
   public List<Booking> loadAllReservations() throws URISyntaxException, IOException, InterruptedException {
        try {
            List<Booking> bookingsList;

            HttpClient client = HttpClient.newBuilder()
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/rezerwacje"))
                    .header("Authorization", "Bearer " + key)
                    .build();

            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                    .create();
            Type listType = new TypeToken<List<Booking>>() {
            }.getType();
            bookingsList = gson.fromJson((String) response.body(), listType);
            int responseCode = response.statusCode();

            // Check for success (status code 200 OK)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Handle successful response
                System.out.println("Data fetched successfully!");
                // Process and display data...
            } else {
                // Handle non-200 response codes
                Alert alert = new Alert(Alert.AlertType.ERROR);
                System.out.println("here " + responseCode);
                alert.setContentText("Błąd serwera, kod: "+ responseCode);
            }

            return bookingsList;
        }
        catch (IOException e){
            alertWindow.getServerConnectionError();
        }
return List.of();
    }


    public int saveOneBookingToDatabase(Booking booking)  {
        System.out.println(booking.getAccType());
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        String json = gson.toJson(booking);
        System.out.println("json: " + json);
        HttpClient client = HttpClient.newBuilder()
                .build();
        System.out.println(booking);
        System.out.println(json);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/dodajRezerwacje"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + key)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // Send the request and get the response
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setContentText("Nie nie utworzono rezerwacji. Brak połączenia z serwerem!");
           alert.show();

        }
        // Print response
        System.out.println("Response code: " + response.statusCode());
        System.out.println("Response body: " + response.body());
        System.out.println(booking.isPaid);
        return response.statusCode();
    }

    public String getPrice(Booking booking){

   Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        String json = gson.toJson(booking);
        HttpClient client = HttpClient.newBuilder()
                .build();
        System.out.println(booking);
        System.out.println(json);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/sendPriceData"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + key)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // Send the request and get the response
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
int responseCode = response.statusCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Handle successful response
                System.out.println("Data fetched successfully!");
                // Process and display data...
            } else {
                // Handle non-200 response codes
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Błąd serwera, kod: "+ responseCode);
            }
        } catch (IOException | InterruptedException e) {
           alertWindow.getServerConnectionError();
        }
        // Print response
        System.out.println("Response code for sending data: " + response.statusCode());
        System.out.println("Response body fsd: " + response.body());


        HttpClient client2 = HttpClient.newBuilder()
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getPrice"))
                .header("Authorization", "Bearer " + key)
                .build();
        HttpResponse response2 = null;
        try {
            response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
            int responseCode = response.statusCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Handle successful response
                System.out.println("Data fetched successfully!");
                // Process and display data...
            } else {
                // Handle non-200 response codes
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Błąd serwera, kod: "+ responseCode);
            }
        } catch (IOException | InterruptedException e) {
            alertWindow.getServerConnectionError();
        }
        System.out.println("rspBod: " + response2.body());
        Gson gson2 = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        Type type = new TypeToken<String>() {
        }.getType();
        String price = gson2.fromJson((String) response2.body(), type);
        return price;
    }
    public List<AccType> loadAllAccTypes() throws IOException, InterruptedException {

            List<AccType> accTypeList = new ArrayList<>();
            HttpClient client = HttpClient.newBuilder()
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/types"))
                    .header("Authorization", "Bearer " + key)
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                    .create();
            Type listType = new TypeToken<List<AccType>>() {
            }.getType();
            accTypeList = gson.fromJson((String) response.body(), listType);
            return accTypeList;

    }
    public void updatePriceListToDataBase(List<PricePerType> pricePerTypeList) throws IOException, InterruptedException {

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
                .header("Authorization", "Bearer " + key)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int responseCode = response.statusCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Handle successful response
            System.out.println("Data fetched successfully!");
            // Process and display data...
        } else {
            // Handle non-200 response codes
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Błąd serwera, kod: "+ responseCode);
        }

    }
    public List<PricePeriod> loadAllPricePeriods() throws IOException, InterruptedException {

            HttpClient client = HttpClient.newBuilder()
                    .build();

            List<PricePeriod> pricePeriodList = new ArrayList<>();

            HttpRequest request2 = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/periods"))
                    .header("Authorization", "Bearer " + key)
                    .build();
            HttpResponse response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
            Gson gson2 = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                    .create();
            Type listType2 = new TypeToken<List<PricePeriod>>() {
            }.getType();
            pricePeriodList = gson2.fromJson((String) response2.body(), listType2);
            return pricePeriodList;

    }
    public List<PricePerType> loadAllPricePerType() throws IOException, InterruptedException {

            HttpClient client = HttpClient.newBuilder()
                    .build();

            List<PricePerType> pricePerTypeList;
            HttpRequest request3 = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/getPrices"))
                    .header("Authorization", "Bearer " + key)
                    .build();
            HttpResponse response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
            Gson gson3 = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                    .create();
            Type listType3 = new TypeToken<List<PricePerType>>() {
            }.getType();
            pricePerTypeList = gson3.fromJson((String) response3.body(), listType3);

            return pricePerTypeList;




    }
    public Booking findBookingById(Integer id) throws IOException, InterruptedException {
        try {
            String idS = id.toString();
            HttpClient client = HttpClient.newBuilder()
                    .build();
            Booking booking;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/getBookingById?id=" + id))
                    .header("Authorization", "Bearer " + key)
                    .build();
            System.out.println(request);
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int responseCode = response.statusCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Handle successful response
                System.out.println("Data fetched successfully!");
                // Process and display data...
            } else {
                // Handle non-200 response codes
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Błąd serwera, kod: "+ responseCode);
            }

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                    .create();
            System.out.println(response.body());
            Type type = new TypeToken<Booking>() {
            }.getType();
            booking = gson.fromJson((String) response.body(), type);
            System.out.println(booking.toString());
            return booking;
        }catch (IOException e){

        }
        return null;



    }

    public String checkApi() {

            HttpClient client = HttpClient.newBuilder()
                    .build();
            Booking booking;
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/check"))
                        .header("Authorization", "Bearer " + key)
                        .build();
                HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String responseString = (String) response.body();
                return responseString;
            }catch (Exception e){
                return e.toString();
            }

    }

    public List<Booking> findBookingByName(String name) throws IOException, InterruptedException {
        try {

            HttpClient client = HttpClient.newBuilder()
                    .build();
           List<Booking> bookingList;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/getBookingByName?name=" + name))
                    .header("Authorization", "Bearer " + key)
                    .build();
            System.out.println(request);
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int responseCode = response.statusCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Handle successful response
                System.out.println("Data fetched successfully!");
                // Process and display data...
            } else {
                // Handle non-200 response codes
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Błąd serwera, kod: "+ responseCode);
            }

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                    .create();
            System.out.println(response.body());
            Type type = new TypeToken<List<Booking>>() {
            }.getType();
            bookingList = gson.fromJson((String) response.body(), type);
            System.out.println(bookingList.toString());
            return bookingList;
        }catch (IOException | InterruptedException e){

        }
        return null;
    }

    public List<Booking> findBookingByPaymentStatus(Boolean isPaid) throws IOException, InterruptedException {
        try {
            System.out.println(isPaid);
            HttpClient client = HttpClient.newBuilder()
                    .build();
            List<Booking> bookingList;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/getBookingByPaymentStatus?paid=" + isPaid))
                    .header("Authorization", "Bearer " + key)
                    .build();
            System.out.println(request);
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int responseCode = response.statusCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Handle successful response
                System.out.println("Data fetched successfully!");
                // Process and display data...
            } else {
                // Handle non-200 response codes
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Błąd serwera, kod: "+ responseCode);
            }

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                    .create();
            System.out.println(response.body());
            Type type = new TypeToken<List<Booking>>() {
            }.getType();
            bookingList = gson.fromJson((String) response.body(), type);
            System.out.println(bookingList.toString());
            return bookingList;
        }catch (IOException | InterruptedException e){

        }
        return null;
    }
    public boolean checkIfFree(LocalDate arrivalDate, LocalDate departureDate, Integer typeId) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/checkIfFree?arrivalDate=" + arrivalDate + "&departureDate=" + departureDate + "&type=" + typeId.toString()))
                    .header("Authorization", "Bearer " + key)
                    .build();
            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                    .create();
            Type type1 = new TypeToken<Boolean>() {
            }.getType();
           // Gson gson = new Gson();
            Boolean isFree =  gson.fromJson((String) response.body(), Boolean.class);
            return isFree;
        }catch (IOException| InterruptedException e){

        }
        return false;
    }
    public int deleteBooking(Integer id){

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/delete?id=" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + key)
                .DELETE()
                .build();


        // Send the request and get the response
        HttpResponse<String> response = null;
        try {
            HttpClient client = HttpClient.newBuilder()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Nie nie utworzono rezerwacji. Brak połączenia z serwerem!");
            alert.show();

        }
        // Print response
        System.out.println("Response code: " + response.statusCode());
        System.out.println("Response body: " + response.body());

        return response.statusCode();
    }




}
