package org.example;

import api.ApiClient;

public class Main {

    public static void main(String[] args) {

        try {
            ApiClient client = new ApiClient();

            // Qui scegli il ticker da testare
            String ticker = "AAPL";

            String jsonResponse = client.getStockHistorical(ticker);

            System.out.println("Risposta API per " + ticker + ":");
            System.out.println(jsonResponse);

        } catch (Exception e) {
            System.err.println("Errore nell'esecuzione:");
            e.printStackTrace();
        }
    }
}
