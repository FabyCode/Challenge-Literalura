package com.alurachallenges.literalura.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ConsumoAPI {
    private static final Logger logger = LoggerFactory.getLogger(ConsumoAPI.class);

    public String obtenerDatos(String URL) {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Status: " + response.statusCode());
            logger.info("Response: " + response.body());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RuntimeException("Failed to fetch data. HTTP Status Code: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error fetching data from API: " + e.getMessage(), e);
        }
    }
}
