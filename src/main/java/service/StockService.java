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

    public String getStockSummary(String ticker, int giorni) {
        String json = apiClient.getDailyPrices(ticker);

        try {
            Type type = new TypeToken<List<stockPrice>>() {}.getType();
            List<stockPrice> prices = gson.fromJson(json, type);

            if (prices == null || prices.isEmpty()) {
                return "Nessun dato trovato per " + ticker;
            }

            // Prendo gli ultimi 'giorni' record (max disponibile)
            StringBuilder sb = new StringBuilder(ticker + "\n");
            for (int i = 0; i < Math.min(giorni, prices.size()); i++) {
                stockPrice p = prices.get(i);
                sb.append("Data: ").append(p.date)
                        .append(" | Close: ").append(p.close)
                        .append("\n");
            }
            return sb.toString();

        } catch (Exception e) {
            return "Errore nel parsing dei dati per " + ticker;
        }
    }

}
