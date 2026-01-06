package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StockRequestDao {

    public void saveRequest(Long chatId, String ticker, int giorni) {

        String sql = "INSERT INTO stock_requests (chat_id, ticker, giorni) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, chatId);
            stmt.setString(2, ticker);
            stmt.setInt(3, giorni);

            stmt.executeUpdate();

            System.out.println("DEBUG DB: richiesta salvata -> " + ticker);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
