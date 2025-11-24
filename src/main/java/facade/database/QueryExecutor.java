package facade.database;

import java.sql.*;
import java.util.List;

/**
 * Query Executor - Subsystem component for Facade pattern
 * Handles SQL query execution
 */
public class QueryExecutor {

    private final DatabaseConnectionManager connectionManager;

    /**
     * Constructor
     *
     * @param connectionManager Database connection manager
     */
    public QueryExecutor(DatabaseConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * Execute SELECT query
     *
     * @param sql SQL query
     * @return ResultSet
     * @throws SQLException if query fails
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        Connection conn = connectionManager.getConnection();
        Statement stmt = conn.createStatement();

        System.out.println("✓ Executing SELECT query:");
        System.out.println("  " + sql);

        return stmt.executeQuery(sql);
    }

    /**
     * Execute INSERT, UPDATE, DELETE query
     *
     * @param sql SQL statement
     * @return Number of rows affected
     * @throws SQLException if query fails
     */
    public int executeUpdate(String sql) throws SQLException {
        Connection conn = connectionManager.getConnection();
        Statement stmt = conn.createStatement();

        System.out.println("✓ Executing UPDATE query:");
        System.out.println("  " + sql);

        int rowsAffected = stmt.executeUpdate(sql);
        System.out.println("  Rows affected: " + rowsAffected);

        stmt.close();
        return rowsAffected;
    }

    /**
     * Execute prepared statement query
     *
     * @param sql SQL with placeholders
     * @param params Parameters to bind
     * @return ResultSet
     * @throws SQLException if query fails
     */
    public ResultSet executePreparedQuery(String sql, Object... params) throws SQLException {
        Connection conn = connectionManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Bind parameters
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }

        System.out.println("✓ Executing prepared SELECT query:");
        System.out.println("  " + sql);
        System.out.println("  Parameters: " + params.length);

        return pstmt.executeQuery();
    }

    /**
     * Execute prepared statement update
     *
     * @param sql SQL with placeholders
     * @param params Parameters to bind
     * @return Number of rows affected
     * @throws SQLException if query fails
     */
    public int executePreparedUpdate(String sql, Object... params) throws SQLException {
        Connection conn = connectionManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        // Bind parameters
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }

        System.out.println("✓ Executing prepared UPDATE query:");
        System.out.println("  " + sql);
        System.out.println("  Parameters: " + params.length);

        int rowsAffected = pstmt.executeUpdate();
        System.out.println("  Rows affected: " + rowsAffected);

        pstmt.close();
        return rowsAffected;
    }

    /**
     * Execute batch update
     *
     * @param sqlStatements List of SQL statements
     * @return Array of update counts
     * @throws SQLException if batch fails
     */
    public int[] executeBatch(List<String> sqlStatements) throws SQLException {
        Connection conn = connectionManager.getConnection();
        Statement stmt = conn.createStatement();

        System.out.println("✓ Executing batch update:");
        System.out.println("  Statements: " + sqlStatements.size());

        for (String sql : sqlStatements) {
            stmt.addBatch(sql);
        }

        int[] results = stmt.executeBatch();
        stmt.close();

        System.out.println("  Batch completed successfully");
        return results;
    }

    /**
     * Execute DDL statement (CREATE, ALTER, DROP)
     *
     * @param sql DDL statement
     * @throws SQLException if operation fails
     */
    public void executeDDL(String sql) throws SQLException {
        Connection conn = connectionManager.getConnection();
        Statement stmt = conn.createStatement();

        System.out.println("✓ Executing DDL statement:");
        System.out.println("  " + sql);

        stmt.execute(sql);
        stmt.close();

        System.out.println("  DDL executed successfully");
    }
}
