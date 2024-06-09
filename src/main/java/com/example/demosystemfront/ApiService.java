package com.example.demosystemfront;
import com.example.demosystemfront.Entities.AccType;
import com.example.demosystemfront.Entities.Booking;
import com.example.demosystemfront.Entities.PricePerType;
import com.example.demosystemfront.Entities.PricePeriod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http. HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApiService {
    public List<Booking> loadAllReservations() throws URISyntaxException, IOException, InterruptedException {
        List<Booking> bookingsList;

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

return  bookingsList;

    }

    public void saveOneBookingToDatabase(Booking booking) throws IOException, InterruptedException {



        System.out.println(booking.getAccType());
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
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // Send the request and get the response
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Print response
        System.out.println("Response code for sending data: " + response.statusCode());
        System.out.println("Response body fsd: " + response.body());


        HttpClient client2 = HttpClient.newBuilder()
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getPrice"))
                .build();
        HttpResponse response2;
        try {
            response2 = client2.send(request2, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Print response
        System.out.println("Response code: " + response.statusCode());
        System.out.println("Response body: " + response.body());
    }
    public List<PricePeriod> loadAllPricePeriods() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .build();

List<PricePeriod> pricePeriodList = new ArrayList<>();

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/periods"))
                .build();
        HttpResponse response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        Gson gson2 = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        Type listType2 = new TypeToken<List<PricePeriod>>() {
        }.getType();
        pricePeriodList = gson2.fromJson((String) response2.body(), listType2);
return  pricePeriodList;

    }
    public List<PricePerType> loadAllPricePerType() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .build();

        List<PricePerType> pricePerTypeList;
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
return  pricePerTypeList;


    }




}
