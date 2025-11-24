package facade.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ResultSet Mapper - Subsystem component for Facade pattern
 * Handles mapping of ResultSet to various data structures
 */
public class ResultSetMapper {

    /**
     * Map ResultSet to list of maps
     * Each row becomes a map with column names as keys
     *
     * @param rs ResultSet to map
     * @return List of maps
     * @throws SQLException if mapping fails
     */
    public List<Map<String, Object>> toListOfMaps(ResultSet rs) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = rs.getObject(i);
                row.put(columnName, value);
            }
            rows.add(row);
        }

        System.out.println("✓ ResultSet mapped to List<Map>");
        System.out.println("  Rows: " + rows.size());
        System.out.println("  Columns: " + columnCount);

        rs.close();
        return rows;
    }

    /**
     * Map ResultSet to list of string arrays
     * Each row becomes a string array
     *
     * @param rs ResultSet to map
     * @return List of string arrays
     * @throws SQLException if mapping fails
     */
    public List<String[]> toListOfArrays(ResultSet rs) throws SQLException {
        List<String[]> rows = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            String[] row = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                Object value = rs.getObject(i);
                row[i - 1] = value != null ? value.toString() : null;
            }
            rows.add(row);
        }

        System.out.println("✓ ResultSet mapped to List<String[]>");
        System.out.println("  Rows: " + rows.size());

        rs.close();
        return rows;
    }

    /**
     * Get first row as map
     *
     * @param rs ResultSet to map
     * @return Map of first row, or empty map if no rows
     * @throws SQLException if mapping fails
     */
    public Map<String, Object> getFirstRow(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        Map<String, Object> row = new HashMap<>();

        if (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = rs.getObject(i);
                row.put(columnName, value);
            }
            System.out.println("✓ First row retrieved with " + columnCount + " columns");
        } else {
            System.out.println("⚠ No rows found in ResultSet");
        }

        rs.close();
        return row;
    }

    /**
     * Get single value from ResultSet
     *
     * @param rs ResultSet to map
     * @return Single value, or null if no result
     * @throws SQLException if mapping fails
     */
    public Object getSingleValue(ResultSet rs) throws SQLException {
        Object value = null;

        if (rs.next()) {
            value = rs.getObject(1);
            System.out.println("✓ Single value retrieved: " + value);
        } else {
            System.out.println("⚠ No value found in ResultSet");
        }

        rs.close();
        return value;
    }

    /**
     * Get column names from ResultSet
     *
     * @param rs ResultSet to analyze
     * @return List of column names
     * @throws SQLException if operation fails
     */
    public List<String> getColumnNames(ResultSet rs) throws SQLException {
        List<String> columns = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            columns.add(metaData.getColumnName(i));
        }

        System.out.println("✓ Column names retrieved: " + columns.size());
        return columns;
    }

    /**
     * Get row count from ResultSet
     *
     * @param rs ResultSet to count
     * @return Number of rows
     * @throws SQLException if operation fails
     */
    public int getRowCount(ResultSet rs) throws SQLException {
        int count = 0;
        while (rs.next()) {
            count++;
        }

        System.out.println("✓ Row count: " + count);
        rs.close();
        return count;
    }

    /**
     * Print ResultSet to console (for debugging)
     *
     * @param rs ResultSet to print
     * @throws SQLException if operation fails
     */
    public void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Print column names
        System.out.println("\n" + "=".repeat(80));
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(metaData.getColumnName(i) + "\t");
        }
        System.out.println();
        System.out.println("=".repeat(80));

        // Print rows
        int rowNum = 0;
        while (rs.next()) {
            rowNum++;
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rs.getObject(i) + "\t");
            }
            System.out.println();
        }

        System.out.println("=".repeat(80));
        System.out.println("Total rows: " + rowNum);
        System.out.println("=".repeat(80));

        rs.close();
    }
}

