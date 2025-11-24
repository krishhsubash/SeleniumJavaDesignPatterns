package facade.database;

import java.sql.*;
import java.util.Properties;

/**
 * Database Connection Manager - Subsystem component for Facade pattern
 * Handles database connection creation and management
 */
public class DatabaseConnectionManager {

    private Connection connection;
    private final String url;
    private final String username;
    private final String password;

    /**
     * Constructor with connection details
     *
     * @param url Database URL
     * @param username Database username
     * @param password Database password
     */
    public DatabaseConnectionManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Establish database connection
     *
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public Connection connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("✓ Database connection established");
            System.out.println("  URL: " + url);
            System.out.println("  User: " + username);
        }
        return connection;
    }

    /**
     * Get active connection
     *
     * @return Connection object
     * @throws SQLException if no active connection
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            return connect();
        }
        return connection;
    }

    /**
     * Close database connection
     *
     * @throws SQLException if close fails
     */
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("✓ Database connection closed");
        }
    }

    /**
     * Check if connection is active
     *
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Get connection metadata
     *
     * @return DatabaseMetaData object
     * @throws SQLException if operation fails
     */
    public DatabaseMetaData getMetaData() throws SQLException {
        return getConnection().getMetaData();
    }

    /**
     * Begin transaction
     *
     * @throws SQLException if operation fails
     */
    public void beginTransaction() throws SQLException {
        getConnection().setAutoCommit(false);
        System.out.println("✓ Transaction started");
    }

    /**
     * Commit transaction
     *
     * @throws SQLException if operation fails
     */
    public void commit() throws SQLException {
        getConnection().commit();
        getConnection().setAutoCommit(true);
        System.out.println("✓ Transaction committed");
    }

    /**
     * Rollback transaction
     *
     * @throws SQLException if operation fails
     */
    public void rollback() throws SQLException {
        getConnection().rollback();
        getConnection().setAutoCommit(true);
        System.out.println("✓ Transaction rolled back");
    }
}

