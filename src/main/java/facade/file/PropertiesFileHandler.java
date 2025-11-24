package facade.file;

import java.io.*;
import java.util.Properties;

/**
 * Properties File Handler - Subsystem component for Facade pattern
 * Handles .properties file read/write operations
 */
public class PropertiesFileHandler {

    /**
     * Read properties file
     *
     * @param filePath Path to properties file
     * @return Properties object
     * @throws IOException if file operation fails
     */
    public Properties readProperties(String filePath) throws IOException {
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        }

        System.out.println("✓ Properties file read successfully: " + filePath);
        System.out.println("  Properties loaded: " + properties.size());
        return properties;
    }

    /**
     * Get property value
     *
     * @param filePath Path to properties file
     * @param key Property key
     * @return Property value
     * @throws IOException if file operation fails
     */
    public String getProperty(String filePath, String key) throws IOException {
        Properties properties = readProperties(filePath);
        String value = properties.getProperty(key);

        System.out.println("✓ Property retrieved: " + key + " = " + value);
        return value;
    }

    /**
     * Get property value with default
     *
     * @param filePath Path to properties file
     * @param key Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default
     * @throws IOException if file operation fails
     */
    public String getProperty(String filePath, String key, String defaultValue) throws IOException {
        Properties properties = readProperties(filePath);
        String value = properties.getProperty(key, defaultValue);

        System.out.println("✓ Property retrieved: " + key + " = " + value);
        return value;
    }

    /**
     * Write properties to file
     *
     * @param filePath Path to properties file
     * @param properties Properties object to write
     * @param comments Comments to add to file
     * @throws IOException if file operation fails
     */
    public void writeProperties(String filePath, Properties properties, String comments) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            properties.store(fos, comments);
        }

        System.out.println("✓ Properties file written successfully: " + filePath);
        System.out.println("  Properties written: " + properties.size());
    }

    /**
     * Set property value
     *
     * @param filePath Path to properties file
     * @param key Property key
     * @param value Property value
     * @throws IOException if file operation fails
     */
    public void setProperty(String filePath, String key, String value) throws IOException {
        Properties properties = readProperties(filePath);
        properties.setProperty(key, value);
        writeProperties(filePath, properties, "Updated by PropertiesFileHandler");

        System.out.println("✓ Property updated: " + key + " = " + value);
    }
}

