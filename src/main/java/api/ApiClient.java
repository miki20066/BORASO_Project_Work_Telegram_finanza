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

        // Legge la chiave dalla variabile d'ambiente (se richiesta)
        this.apiKey = System.getenv("financialData");
        // Alcuni endpoint Free non richiedono API key
        // Se la chiave è necessaria, decommenta il controllo:
        if(apiKey == null || apiKey.isBlank()) {
             throw new RuntimeException("API key non trovata (financialData)");
         }
    }

    public String getStockHistorical(String ticker) throws Exception {

        // Costruisce URL con ticker e formato JSON
        String url = BASE_URL + "?identifier=" + ticker + "&format=json";

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET();

        // Se l'endpoint richiede header API key, aggiungi:
        requestBuilder.header("Authorization", "Bearer " + apiKey);

        HttpRequest request = requestBuilder.build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        return response.body();
    }
}
