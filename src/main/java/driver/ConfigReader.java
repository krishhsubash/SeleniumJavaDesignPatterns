package driver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration reader for loading properties from config.properties file
 */
public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";

    static {
        loadProperties();
    }

    /**
     * Load properties from config file
     */
    private static void loadProperties() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Error loading config file: " + e.getMessage());
            // Initialize with default properties
            properties = new Properties();
        }
    }

    /**
     * Get property value by key
     *
     * @param key property key
     * @return property value or null if not found
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get property value by key with default value
     *
     * @param key property key
     * @param defaultValue default value if property not found
     * @return property value or default value
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
