package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    private static final String BASE_URL =
            "https://financialdata.net/api/v1/stock-prices";

    private static final String API_KEY =
            System.getenv("FINANCIAL_API_KEY");

    public String getDailyPrices(String symbol) {

        if (API_KEY == null || API_KEY.isBlank()) {
            throw new RuntimeException("FINANCIAL_API_KEY non impostata");
        }

        String url = BASE_URL +
                "?symbol=" + symbol +
                "&key=" + API_KEY;

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();

        } catch (Exception e) {
            return "Errore nella richiesta API: " + e.getMessage();
        }
    }


}
