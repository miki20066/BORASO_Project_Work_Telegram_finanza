package service;

import api.ApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.stockPrice;



import java.lang.reflect.Type;
import java.util.List;

import api.ApiClient;

public class StockService {


    private final ApiClient apiClient = new ApiClient();
    private final Gson gson = new Gson();

    public String getStockSummary(String ticker) {

        System.out.println("DEBUG: StockService chiamato con " + ticker);

        String json = apiClient.getDailyPrices(ticker);
        System.out.println("DEBUG JSON: " + json);

        try {
            Type listType = new TypeToken<List<stockPrice>>() {}.getType();
            List<stockPrice> prices = gson.fromJson(json, listType);

            if (prices == null || prices.isEmpty()) {
                return "Nessun dato trovato per " + ticker;
            }

            stockPrice last = prices.get(0);

            return " " + last.trading_symbol +
                    "\nData: " + last.date +
                    "\nOpen: $" + last.open +
                    "\nHigh: $" + last.high +
                    "\nLow: $" + last.low +
                    "\nClose: $" + last.close +
                    "\nVolume: " + last.volume;

        } catch (Exception e) {
            return "Errore nel parsing dei dati per " + ticker;
        }
    }
}
