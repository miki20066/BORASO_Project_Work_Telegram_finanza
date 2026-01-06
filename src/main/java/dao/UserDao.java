package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import db.DatabaseManager;

public class UserDao {

    public void saveUser(long chatId, String username, String firstName) {
        String sql = """
            INSERT OR IGNORE INTO users(chat_id, username, first_name)
            VALUES (?, ?, ?)
        """;

        try (Connection c = DatabaseManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, chatId);
            ps.setString(2, username);
            ps.setString(3, firstName);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
