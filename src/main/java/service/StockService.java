package service;

import api.ApiClient;

public class StockService {

    private final ApiClient apiClient = new ApiClient();

    public String getStockSummary(String ticker) {
        // qui in futuro:
        // 1) chiamata API
        // 2) parsing JSON
        // 3) logica
        return "Dati storici per " + ticker + " recuperati.";
    }
}
