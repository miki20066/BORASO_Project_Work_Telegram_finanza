package config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigLoader.class
                .getClassLoader()
                .getResourceAsStream("config/config.properties")) { // ← deve esistere

            if (input == null) {
                throw new RuntimeException("config.properties non trovato");
            }

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Errore caricamento config.properties", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
