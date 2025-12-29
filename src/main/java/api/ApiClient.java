package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    private static final String BASE_URL = "https://financialdata.net/api/v1/stock-prices";

    private final HttpClient httpClient;
    private final String apiKey;

    public ApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.apiKey = System.getenv("financialData").trim();

        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("API key non trovata. Imposta financialData.");
        }
    }

    public String getStockHistorical(String ticker) throws Exception {
        // URL con ticker e key
        String url = BASE_URL + "?identifier=" + ticker + "&format=json&key=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        return response.body();
    }
}
