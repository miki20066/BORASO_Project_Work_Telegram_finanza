package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:data/bot.db";

    // Metodo per ottenere la connessione
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}