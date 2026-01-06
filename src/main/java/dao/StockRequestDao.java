package dao;

import db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StockRequestDao {

    public void saveRequest(long chatId, String ticker, int days) {
        String sql = """
            INSERT INTO stock_requests(chat_id, ticker, days_requested)
            VALUES (?, ?, ?)
        """;

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, chatId);
            ps.setString(2, ticker);
            ps.setInt(3, days);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
