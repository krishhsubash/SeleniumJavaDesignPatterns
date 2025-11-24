package facade.file;

import java.io.*;
import java.util.*;

/**
 * CSV File Handler - Subsystem component for Facade pattern
 * Handles CSV file read/write operations
 */
public class CSVFileHandler {

    private static final String COMMA_DELIMITER = ",";

    /**
     * Read CSV file and return as list of string arrays
     *
     * @param filePath Path to CSV file
     * @return List of string arrays (each array is a row)
     * @throws IOException if file operation fails
     */
    public List<String[]> readCSV(String filePath) throws IOException {
        List<String[]> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.add(values);
            }
        }

        System.out.println("✓ CSV file read successfully: " + filePath);
        System.out.println("  Records loaded: " + records.size());
        return records;
    }

    /**
     * Read CSV file with headers and return as list of maps
     *
     * @param filePath Path to CSV file
     * @return List of maps (key=column name, value=cell value)
     * @throws IOException if file operation fails
     */
    public List<Map<String, String>> readCSVWithHeaders(String filePath) throws IOException {
        List<Map<String, String>> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                return records;
            }

            String[] headers = headerLine.split(COMMA_DELIMITER);

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                Map<String, String> record = new HashMap<>();

                for (int i = 0; i < headers.length && i < values.length; i++) {
                    record.put(headers[i].trim(), values[i].trim());
                }
                records.add(record);
            }
        }

        System.out.println("✓ CSV file with headers read successfully: " + filePath);
        System.out.println("  Records loaded: " + records.size());
        return records;
    }

    /**
     * Write data to CSV file
     *
     * @param filePath Path to CSV file
     * @param data List of string arrays to write
     * @throws IOException if file operation fails
     */
    public void writeCSV(String filePath, List<String[]> data) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                pw.println(String.join(COMMA_DELIMITER, row));
            }
        }

        System.out.println("✓ CSV file written successfully: " + filePath);
        System.out.println("  Records written: " + data.size());
    }

    /**
     * Append data to existing CSV file
     *
     * @param filePath Path to CSV file
     * @param data List of string arrays to append
     * @throws IOException if file operation fails
     */
    public void appendCSV(String filePath, List<String[]> data) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            for (String[] row : data) {
                pw.println(String.join(COMMA_DELIMITER, row));
            }
        }

        System.out.println("✓ Data appended to CSV file: " + filePath);
        System.out.println("  Records appended: " + data.size());
    }
}
