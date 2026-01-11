package dao;

import db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FavoriteStockDao {

    public void saveFavorite(long userId, String ticker) {
        String sql = """
            INSERT OR IGNORE INTO favorite_stocks (user_id, ticker)
            VALUES (?, ?)
        """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setString(2, ticker);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getFavoritesByUser(long userId) {
        List<String> favorites = new ArrayList<>();

        String sql = """
            SELECT ticker
            FROM favorite_stocks
            WHERE user_id = ?
            ORDER BY created_at DESC
        """;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                favorites.add(rs.getString("ticker"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return favorites;
    }
}
