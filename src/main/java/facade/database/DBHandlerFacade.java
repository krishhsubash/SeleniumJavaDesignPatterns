package facade.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Database Handler Facade - Facade Design Pattern Implementation
 *
 * Provides a simplified unified interface for complex database operations.
 * Hides the complexity of multiple database handling subsystems.
 *
 * Design Pattern: FACADE
 *
 * Purpose:
 * - Simplify complex database interactions
 * - Provide a unified interface for DB operations
 * - Hide JDBC complexity from clients
 * - Manage connections, queries, and result mapping
 *
 * Subsystems:
 * - DatabaseConnectionManager: Manages DB connections
 * - QueryExecutor: Executes SQL queries
 * - ResultSetMapper: Maps results to data structures
 *
 * Benefits:
 * - Single point of access for all DB operations
 * - Automatic connection management
 * - Simplified query execution
 * - Easy result mapping
 * - Transaction support
 */
public class DBHandlerFacade {

    // Subsystem components
    private final DatabaseConnectionManager connectionManager;
    private final QueryExecutor queryExecutor;
    private final ResultSetMapper resultMapper;

    /**
     * Constructor with connection details
     *
     * @param url Database URL
     * @param username Database username
     * @param password Database password
     */
    public DBHandlerFacade(String url, String username, String password) {
        this.connectionManager = new DatabaseConnectionManager(url, username, password);
        this.queryExecutor = new QueryExecutor(connectionManager);
        this.resultMapper = new ResultSetMapper();

        System.out.println("════════════════════════════════════════");
        System.out.println("🗄️  DATABASE HANDLER FACADE INITIALIZED");
        System.out.println("════════════════════════════════════════");
        System.out.println("✓ Connection Manager ready");
        System.out.println("✓ Query Executor ready");
        System.out.println("✓ ResultSet Mapper ready");
        System.out.println("════════════════════════════════════════");
    }

    // ============================================================
    // CONNECTION MANAGEMENT - Simplified Interface
    // ============================================================

    /**
     * Connect to database (facade method)
     *
     * @throws SQLException if connection fails
     */
    public void connect() throws SQLException {
        System.out.println("\n🔌 FACADE: Connecting to database...");
        connectionManager.connect();
    }

    /**
     * Disconnect from database (facade method)
     *
     * @throws SQLException if disconnect fails
     */
    public void disconnect() throws SQLException {
        System.out.println("\n🔌 FACADE: Disconnecting from database...");
        connectionManager.disconnect();
    }

    /**
     * Check connection status (facade method)
     *
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return connectionManager.isConnected();
    }

    // ============================================================
    // QUERY EXECUTION - Simplified Interface
    // ============================================================

    /**
     * Execute SELECT query and get results as List of Maps (facade method)
     *
     * @param sql SQL SELECT query
     * @return List of maps (column name -> value)
     * @throws SQLException if query fails
     */
    public List<Map<String, Object>> select(String sql) throws SQLException {
        System.out.println("\n📊 FACADE: Executing SELECT query...");
        ResultSet rs = queryExecutor.executeQuery(sql);
        return resultMapper.toListOfMaps(rs);
    }

    /**
     * Execute SELECT query and get first row (facade method)
     *
     * @param sql SQL SELECT query
     * @return Map of first row
     * @throws SQLException if query fails
     */
    public Map<String, Object> selectOne(String sql) throws SQLException {
        System.out.println("\n📊 FACADE: Executing SELECT ONE query...");
        ResultSet rs = queryExecutor.executeQuery(sql);
        return resultMapper.getFirstRow(rs);
    }

    /**
     * Execute SELECT query and get single value (facade method)
     *
     * @param sql SQL SELECT query
     * @return Single value
     * @throws SQLException if query fails
     */
    public Object selectValue(String sql) throws SQLException {
        System.out.println("\n📊 FACADE: Executing SELECT VALUE query...");
        ResultSet rs = queryExecutor.executeQuery(sql);
        return resultMapper.getSingleValue(rs);
    }

    /**
     * Execute INSERT query (facade method)
     *
     * @param sql SQL INSERT statement
     * @return Number of rows inserted
     * @throws SQLException if query fails
     */
    public int insert(String sql) throws SQLException {
        System.out.println("\n➕ FACADE: Executing INSERT...");
        return queryExecutor.executeUpdate(sql);
    }

    /**
     * Execute UPDATE query (facade method)
     *
     * @param sql SQL UPDATE statement
     * @return Number of rows updated
     * @throws SQLException if query fails
     */
    public int update(String sql) throws SQLException {
        System.out.println("\n✏️ FACADE: Executing UPDATE...");
        return queryExecutor.executeUpdate(sql);
    }

    /**
     * Execute DELETE query (facade method)
     *
     * @param sql SQL DELETE statement
     * @return Number of rows deleted
     * @throws SQLException if query fails
     */
    public int delete(String sql) throws SQLException {
        System.out.println("\n🗑️ FACADE: Executing DELETE...");
        return queryExecutor.executeUpdate(sql);
    }

    // ============================================================
    // PREPARED STATEMENTS - Simplified Interface
    // ============================================================

    /**
     * Execute prepared SELECT query (facade method)
     *
     * @param sql SQL with placeholders
     * @param params Parameters to bind
     * @return List of maps
     * @throws SQLException if query fails
     */
    public List<Map<String, Object>> selectPrepared(String sql, Object... params) throws SQLException {
        System.out.println("\n📊 FACADE: Executing prepared SELECT...");
        ResultSet rs = queryExecutor.executePreparedQuery(sql, params);
        return resultMapper.toListOfMaps(rs);
    }

    /**
     * Execute prepared INSERT/UPDATE/DELETE (facade method)
     *
     * @param sql SQL with placeholders
     * @param params Parameters to bind
     * @return Number of rows affected
     * @throws SQLException if query fails
     */
    public int executePrepared(String sql, Object... params) throws SQLException {
        System.out.println("\n⚡ FACADE: Executing prepared statement...");
        return queryExecutor.executePreparedUpdate(sql, params);
    }

    // ============================================================
    // BATCH OPERATIONS - Simplified Interface
    // ============================================================

    /**
     * Execute batch of SQL statements (facade method)
     *
     * @param sqlStatements List of SQL statements
     * @return Array of update counts
     * @throws SQLException if batch fails
     */
    public int[] executeBatch(List<String> sqlStatements) throws SQLException {
        System.out.println("\n📦 FACADE: Executing batch operations...");
        return queryExecutor.executeBatch(sqlStatements);
    }

    // ============================================================
    // TRANSACTION MANAGEMENT - Simplified Interface
    // ============================================================

    /**
     * Begin transaction (facade method)
     *
     * @throws SQLException if operation fails
     */
    public void beginTransaction() throws SQLException {
        System.out.println("\n🔄 FACADE: Beginning transaction...");
        connectionManager.beginTransaction();
    }

    /**
     * Commit transaction (facade method)
     *
     * @throws SQLException if operation fails
     */
    public void commit() throws SQLException {
        System.out.println("\n✅ FACADE: Committing transaction...");
        connectionManager.commit();
    }

    /**
     * Rollback transaction (facade method)
     *
     * @throws SQLException if operation fails
     */
    public void rollback() throws SQLException {
        System.out.println("\n↩️ FACADE: Rolling back transaction...");
        connectionManager.rollback();
    }

    // ============================================================
    // DDL OPERATIONS - Simplified Interface
    // ============================================================

    /**
     * Create table (facade method)
     *
     * @param sql CREATE TABLE statement
     * @throws SQLException if operation fails
     */
    public void createTable(String sql) throws SQLException {
        System.out.println("\n🏗️ FACADE: Creating table...");
        queryExecutor.executeDDL(sql);
    }

    /**
     * Drop table (facade method)
     *
     * @param tableName Table name
     * @throws SQLException if operation fails
     */
    public void dropTable(String tableName) throws SQLException {
        System.out.println("\n🗑️ FACADE: Dropping table: " + tableName);
        String sql = "DROP TABLE IF EXISTS " + tableName;
        queryExecutor.executeDDL(sql);
    }

    // ============================================================
    // UTILITY METHODS
    // ============================================================

    /**
     * Get table row count (facade method)
     *
     * @param tableName Table name
     * @return Number of rows
     * @throws SQLException if operation fails
     */
    public int getRowCount(String tableName) throws SQLException {
        System.out.println("\n🔢 FACADE: Getting row count for: " + tableName);
        String sql = "SELECT COUNT(*) FROM " + tableName;
        Object count = selectValue(sql);
        return count != null ? ((Number) count).intValue() : 0;
    }

    /**
     * Check if table exists (facade method)
     *
     * @param tableName Table name
     * @return true if table exists, false otherwise
     * @throws SQLException if operation fails
     */
    public boolean tableExists(String tableName) throws SQLException {
        try {
            String sql = "SELECT 1 FROM " + tableName + " LIMIT 1";
            queryExecutor.executeQuery(sql).close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Execute custom query and get raw ResultSet (facade method)
     * Use with caution - caller must close ResultSet
     *
     * @param sql SQL query
     * @return ResultSet
     * @throws SQLException if query fails
     */
    public ResultSet executeRawQuery(String sql) throws SQLException {
        System.out.println("\n⚠️ FACADE: Executing raw query (manual ResultSet handling required)");
        return queryExecutor.executeQuery(sql);
    }

    /**
     * Display facade summary
     */
    public void displaySummary() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("🗄️  DATABASE HANDLER FACADE SUMMARY");
        System.out.println("════════════════════════════════════════");
        System.out.println("Design Pattern: FACADE");
        System.out.println("Purpose: Simplify database operations");
        System.out.println();
        System.out.println("Supported Operations:");
        System.out.println("  ✓ SELECT - Query data");
        System.out.println("  ✓ INSERT - Add records");
        System.out.println("  ✓ UPDATE - Modify records");
        System.out.println("  ✓ DELETE - Remove records");
        System.out.println("  ✓ Transactions - ACID compliance");
        System.out.println("  ✓ Batch operations - Multiple queries");
        System.out.println("  ✓ DDL - Table management");
        System.out.println();
        System.out.println("Features:");
        System.out.println("  • Automatic connection management");
        System.out.println("  • Prepared statements support");
        System.out.println("  • Result mapping to Maps/Lists");
        System.out.println("  • Transaction control");
        System.out.println("  • Simplified API");
        System.out.println();
        System.out.println("Benefits:");
        System.out.println("  • Hides JDBC complexity");
        System.out.println("  • Easy to use and maintain");
        System.out.println("  • Loosely coupled design");
        System.out.println("  • Testable code");
        System.out.println("════════════════════════════════════════");
    }
}
