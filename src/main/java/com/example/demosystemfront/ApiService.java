package com.example.demosystemfront;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http. HttpRequest;
public class ApiService {
   HttpRequest request;

    {
        try {
            request = (HttpRequest) HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/rezerwacje"))
                    .GET();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
