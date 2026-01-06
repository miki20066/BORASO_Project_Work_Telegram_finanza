package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class DatabaseManager {

        private static final String DB_URL = "\"C:\\Users\\Michelangelo\\Desktop\\BORASO_Project_Work_Telegram_finanza\\data\\bot.db\"";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(DB_URL);
        }
    }
