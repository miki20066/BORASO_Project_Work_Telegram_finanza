package service;

import api.ApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.stockPrice;
import model.StockResult;

import java.lang.reflect.Type;
import java.util.List;

public class StockService {

    private final ApiClient apiClient = new ApiClient();
    private final Gson gson = new Gson();
    private final ChartService chartService = new ChartService();

    public StockResult getStockResult(String ticker, int giorni) {

        String json = apiClient.getDailyPrices(ticker);

        try {
            Type type = new TypeToken<List<stockPrice>>() {}.getType();
            List<stockPrice> prices = gson.fromJson(json, type);

            if (prices == null || prices.isEmpty()) {
                return new StockResult("Nessun dato trovato per " + ticker, null);
            }

            // limitiamo ai giorni richiesti
            List<stockPrice> subset =
                    prices.subList(0, Math.min(giorni, prices.size()));

            // 👉 1 solo giorno → TESTO
            if (giorni == 1) {
                stockPrice p = subset.get(0);
                String msg = """
                        📊 %s
                        Data: %s
                        Open: %.2f
                        High: %.2f
                        Low: %.2f
                        Close: %.2f
                        Volume: %d
                        """.formatted(
                        ticker, p.date, p.open, p.high, p.low, p.close, p.volume
                );
                return new StockResult(msg, null);
            }

            // 👉 più giorni → GRAFICO
            return new StockResult(
                    "📈 Andamento ultimi " + giorni + " giorni per " + ticker,
                    chartService.generatePriceChart(ticker, subset)
            );

        } catch (Exception e) {
            return new StockResult("Errore nel parsing dei dati per " + ticker, null);
        }
    }
}
