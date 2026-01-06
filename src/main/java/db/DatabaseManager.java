package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class DatabaseManager {

        // Percorso assoluto al database SQLite
        private static final String DB_URL = "jdbc:sqlite:C:/Users/Michelangelo/Desktop/BORASO_Project_Work_Telegram_finanza/data/bot.db";

        // Metodo per ottenere la connessione
        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(DB_URL);
        }

        // Metodo per creare tabelle se non esistono
        public static void initializeDatabase() {
            try (Connection conn = getConnection();
                 var stmt = conn.createStatement()) {

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                        "chat_id INTEGER PRIMARY KEY, " +
                        "username TEXT, " +
                        "first_name TEXT" +
                        ");");

                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS stock_requests (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "chat_id INTEGER, " +
                        "ticker TEXT, " +
                        "giorni INTEGER, " +
                        "request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY(chat_id) REFERENCES users(chat_id)" +
                        ");");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
