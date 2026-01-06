package service;

import api.ApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import db.DatabaseManager;
import model.stockPrice;
import model.StockResult;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;


import java.sql.SQLException;
import java.sql.Statement;
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
                        %s
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

    // Statistiche generali
    public String getGeneralStats() {
        StringBuilder sb = new StringBuilder();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs1 = stmt.executeQuery("SELECT COUNT(*) AS total FROM stock_requests");
            if (rs1.next()) {
                sb.append("Totale richieste: ").append(rs1.getInt("total")).append("\n");
            }

            ResultSet rs2 = stmt.executeQuery(
                    "SELECT ticker, COUNT(*) AS count FROM stock_requests " +
                            "GROUP BY ticker ORDER BY count DESC LIMIT 3"
            );
            sb.append("Ticker più richiesti:\n");
            while (rs2.next()) {
                sb.append(rs2.getString("ticker"))
                        .append(" - ")
                        .append(rs2.getInt("count"))
                        .append(" richieste\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Errore nel recupero delle statistiche generali." + e.getMessage();
        }

        return sb.toString();
    }

    // Statistiche personali
    public String getUserStats(Long chatId) {
        StringBuilder sb = new StringBuilder();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs1 = stmt.executeQuery(
                    "SELECT COUNT(*) AS total FROM stock_requests WHERE chat_id = " + chatId
            );
            if (rs1.next()) {
                sb.append("Totale richieste: ").append(rs1.getInt("total")).append("\n");
            }

            ResultSet rs2 = stmt.executeQuery(
                    "SELECT ticker, COUNT(*) AS count FROM stock_requests " +
                            "WHERE chat_id = " + chatId + " " +
                            "GROUP BY ticker ORDER BY count DESC LIMIT 3"
            );
            sb.append("I tuoi ticker più richiesti:\n");
            while (rs2.next()) {
                sb.append(rs2.getString("ticker"))
                        .append(" - ")
                        .append(rs2.getInt("count"))
                        .append(" richieste\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Errore nel recupero delle statistiche personali.";
        }

        return sb.toString();
    }
}
