package api;

import config.ConfigLoader;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ApiClient {

    private static final String BASE_URL =
            "https://financialdata.net/api/v1/stock-prices";

    // Legge la API key dal ConfigLoader invece che dalle variabili di sistema
    private static final String API_KEY = ConfigLoader.get("API_KEY_FINANCIALDATA");

    public String getDailyPrices(String symbol) {

        System.out.println("DEBUG API: inizio chiamata");

        if (API_KEY == null || API_KEY.isBlank()) {
            System.out.println("DEBUG API: API KEY NULLA");
            return "[]";
        }

        String url = BASE_URL
                + "?identifier=" + symbol
                + "&format=json"
                + "&key=" + API_KEY;

        System.out.println("DEBUG API URL: " + url);

        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            System.out.println("DEBUG API: request creata");

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("DEBUG API: risposta ricevuta");
            System.out.println("DEBUG STATUS: " + response.statusCode());
            System.out.println("DEBUG JSON: " + response.body());

            return response.body();

        } catch (Exception e) {
            System.out.println("DEBUG API ERROR:");
            e.printStackTrace();
            return "[]";
        }
    }
}
