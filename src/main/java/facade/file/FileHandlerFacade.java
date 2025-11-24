package facade.file;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * File Handler Facade - Facade Design Pattern Implementation
 *
 * Provides a simplified unified interface for complex file operations.
 * Hides the complexity of multiple file handling subsystems (CSV, JSON, Properties, Text).
 *
 * Design Pattern: FACADE
 *
 * Purpose:
 * - Simplify complex subsystem interactions
 * - Provide a unified interface for file operations
 * - Hide implementation details from clients
 * - Reduce coupling between client code and subsystems
 *
 * Subsystems:
 * - CSVFileHandler: Handles CSV file operations
 * - JSONFileHandler: Handles JSON file operations
 * - PropertiesFileHandler: Handles .properties file operations
 * - TextFileHandler: Handles plain text file operations
 *
 * Benefits:
 * - Single point of access for all file operations
 * - Easy to use API
 * - Shields clients from subsystem complexity
 * - Flexible - can add new file types without changing client code
 */
public class FileHandlerFacade {

    // Subsystem components
    private final CSVFileHandler csvHandler;
    private final JSONFileHandler jsonHandler;
    private final PropertiesFileHandler propertiesHandler;
    private final TextFileHandler textHandler;

    /**
     * Constructor - initializes all subsystem handlers
     */
    public FileHandlerFacade() {
        this.csvHandler = new CSVFileHandler();
        this.jsonHandler = new JSONFileHandler();
        this.propertiesHandler = new PropertiesFileHandler();
        this.textHandler = new TextFileHandler();

        System.out.println("════════════════════════════════════════");
        System.out.println("📁 FILE HANDLER FACADE INITIALIZED");
        System.out.println("════════════════════════════════════════");
        System.out.println("✓ CSV File Handler ready");
        System.out.println("✓ JSON File Handler ready");
        System.out.println("✓ Properties File Handler ready");
        System.out.println("✓ Text File Handler ready");
        System.out.println("════════════════════════════════════════");
    }

    // ============================================================
    // CSV OPERATIONS - Simplified Interface
    // ============================================================

    /**
     * Read CSV file (facade method)
     *
     * @param filePath Path to CSV file
     * @return List of string arrays
     * @throws IOException if operation fails
     */
    public List<String[]> readCSV(String filePath) throws IOException {
        System.out.println("\n📄 FACADE: Reading CSV file...");
        return csvHandler.readCSV(filePath);
    }

    /**
     * Read CSV with headers (facade method)
     *
     * @param filePath Path to CSV file
     * @return List of maps with column headers as keys
     * @throws IOException if operation fails
     */
    public List<Map<String, String>> readCSVWithHeaders(String filePath) throws IOException {
        System.out.println("\n📄 FACADE: Reading CSV file with headers...");
        return csvHandler.readCSVWithHeaders(filePath);
    }

    /**
     * Write CSV file (facade method)
     *
     * @param filePath Path to CSV file
     * @param data Data to write
     * @throws IOException if operation fails
     */
    public void writeCSV(String filePath, List<String[]> data) throws IOException {
        System.out.println("\n📝 FACADE: Writing CSV file...");
        csvHandler.writeCSV(filePath, data);
    }

    /**
     * Append to CSV file (facade method)
     *
     * @param filePath Path to CSV file
     * @param data Data to append
     * @throws IOException if operation fails
     */
    public void appendCSV(String filePath, List<String[]> data) throws IOException {
        System.out.println("\n➕ FACADE: Appending to CSV file...");
        csvHandler.appendCSV(filePath, data);
    }

    // ============================================================
    // JSON OPERATIONS - Simplified Interface
    // ============================================================

    /**
     * Read JSON file (facade method)
     *
     * @param filePath Path to JSON file
     * @param valueType Class to deserialize to
     * @param <T> Type parameter
     * @return Deserialized object
     * @throws IOException if operation fails
     */
    public <T> T readJSON(String filePath, Class<T> valueType) throws IOException {
        System.out.println("\n📄 FACADE: Reading JSON file...");
        return jsonHandler.readJSON(filePath, valueType);
    }

    /**
     * Read JSON as Map (facade method)
     *
     * @param filePath Path to JSON file
     * @return Map representation
     * @throws IOException if operation fails
     */
    public Map<String, Object> readJSONAsMap(String filePath) throws IOException {
        System.out.println("\n📄 FACADE: Reading JSON file as Map...");
        return jsonHandler.readJSONAsMap(filePath);
    }

    /**
     * Write JSON file (facade method)
     *
     * @param filePath Path to JSON file
     * @param data Object to serialize
     * @throws IOException if operation fails
     */
    public void writeJSON(String filePath, Object data) throws IOException {
        System.out.println("\n📝 FACADE: Writing JSON file...");
        jsonHandler.writeJSON(filePath, data);
    }

    /**
     * Convert object to JSON string (facade method)
     *
     * @param data Object to serialize
     * @return JSON string
     * @throws IOException if operation fails
     */
    public String toJSONString(Object data) throws IOException {
        System.out.println("\n🔄 FACADE: Converting to JSON string...");
        return jsonHandler.toJSONString(data);
    }

    // ============================================================
    // PROPERTIES OPERATIONS - Simplified Interface
    // ============================================================

    /**
     * Read properties file (facade method)
     *
     * @param filePath Path to properties file
     * @return Properties object
     * @throws IOException if operation fails
     */
    public Properties readProperties(String filePath) throws IOException {
        System.out.println("\n📄 FACADE: Reading properties file...");
        return propertiesHandler.readProperties(filePath);
    }

    /**
     * Get property value (facade method)
     *
     * @param filePath Path to properties file
     * @param key Property key
     * @return Property value
     * @throws IOException if operation fails
     */
    public String getProperty(String filePath, String key) throws IOException {
        System.out.println("\n🔍 FACADE: Getting property value...");
        return propertiesHandler.getProperty(filePath, key);
    }

    /**
     * Get property with default (facade method)
     *
     * @param filePath Path to properties file
     * @param key Property key
     * @param defaultValue Default value
     * @return Property value or default
     * @throws IOException if operation fails
     */
    public String getProperty(String filePath, String key, String defaultValue) throws IOException {
        System.out.println("\n🔍 FACADE: Getting property value with default...");
        return propertiesHandler.getProperty(filePath, key, defaultValue);
    }

    /**
     * Set property value (facade method)
     *
     * @param filePath Path to properties file
     * @param key Property key
     * @param value Property value
     * @throws IOException if operation fails
     */
    public void setProperty(String filePath, String key, String value) throws IOException {
        System.out.println("\n📝 FACADE: Setting property value...");
        propertiesHandler.setProperty(filePath, key, value);
    }

    // ============================================================
    // TEXT FILE OPERATIONS - Simplified Interface
    // ============================================================

    /**
     * Read text file (facade method)
     *
     * @param filePath Path to text file
     * @return File content
     * @throws IOException if operation fails
     */
    public String readTextFile(String filePath) throws IOException {
        System.out.println("\n📄 FACADE: Reading text file...");
        return textHandler.readFile(filePath);
    }

    /**
     * Read text file as lines (facade method)
     *
     * @param filePath Path to text file
     * @return List of lines
     * @throws IOException if operation fails
     */
    public List<String> readTextLines(String filePath) throws IOException {
        System.out.println("\n📄 FACADE: Reading text file as lines...");
        return textHandler.readLines(filePath);
    }

    /**
     * Write text file (facade method)
     *
     * @param filePath Path to text file
     * @param content Content to write
     * @throws IOException if operation fails
     */
    public void writeTextFile(String filePath, String content) throws IOException {
        System.out.println("\n📝 FACADE: Writing text file...");
        textHandler.writeFile(filePath, content);
    }

    /**
     * Write text lines (facade method)
     *
     * @param filePath Path to text file
     * @param lines Lines to write
     * @throws IOException if operation fails
     */
    public void writeTextLines(String filePath, List<String> lines) throws IOException {
        System.out.println("\n📝 FACADE: Writing text lines...");
        textHandler.writeLines(filePath, lines);
    }

    /**
     * Append to text file (facade method)
     *
     * @param filePath Path to text file
     * @param content Content to append
     * @throws IOException if operation fails
     */
    public void appendTextFile(String filePath, String content) throws IOException {
        System.out.println("\n➕ FACADE: Appending to text file...");
        textHandler.appendFile(filePath, content);
    }

    // ============================================================
    // UTILITY METHODS
    // ============================================================

    /**
     * Get file type from extension
     *
     * @param filePath File path
     * @return File type (csv, json, properties, txt, unknown)
     */
    public String getFileType(String filePath) {
        String extension = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();

        switch (extension) {
            case "csv":
                return "CSV";
            case "json":
                return "JSON";
            case "properties":
                return "Properties";
            case "txt":
                return "Text";
            default:
                return "Unknown";
        }
    }

    /**
     * Display facade summary
     */
    public void displaySummary() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("📁 FILE HANDLER FACADE SUMMARY");
        System.out.println("════════════════════════════════════════");
        System.out.println("Design Pattern: FACADE");
        System.out.println("Purpose: Simplify complex file operations");
        System.out.println();
        System.out.println("Supported File Types:");
        System.out.println("  ✓ CSV - Comma-separated values");
        System.out.println("  ✓ JSON - JavaScript Object Notation");
        System.out.println("  ✓ Properties - Java properties files");
        System.out.println("  ✓ Text - Plain text files");
        System.out.println();
        System.out.println("Benefits:");
        System.out.println("  • Unified interface for all file types");
        System.out.println("  • Hides subsystem complexity");
        System.out.println("  • Easy to use and maintain");
        System.out.println("  • Loosely coupled design");
        System.out.println("════════════════════════════════════════");
    }
}

