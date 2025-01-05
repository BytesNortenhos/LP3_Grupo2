package Utils;

import io.github.cdimascio.dotenv.Dotenv;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class ConnectionsUtlis {
    private static Dotenv dotenv = Dotenv.load();
    private static String dbHost = dotenv.get("DB_HOST");
    private static String dbName = dotenv.get("DB_NAME");
    private static String dbUser = dotenv.get("DB_USER");
    private static String dbPass = dotenv.get("DB_PASS");

    private static final String connStr = "jdbc:sqlserver://" + dbHost + ";databaseName=" + dbName + ";user=" + dbUser + ";password=" + dbPass + ";encryption=true;trustServerCertificate=true;";
    private static Connection conn = null;

    /**
     * Connect to DB
     * @return
     * @throws SQLException
     */
    public Connection dbConnect() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                conn = DriverManager.getConnection(connStr);
            } catch (SQLException e) {
                System.out.println("Connection Failed! Check output console" + e);
                e.printStackTrace();
                throw e;
            }
        }
        return conn;
    }

    /**
     * Disconnect from DB
     * @throws SQLException
     */
    public void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Execute query
     * @param queryStmt {String} Query
     * @return CachedRowSet
     * @throws SQLException
     */
    public CachedRowSet dbExecuteQuery(String queryStmt) throws SQLException {
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSet crs = null;
        try {
            dbConnect();
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(queryStmt);

            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(resultSet);

        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
        return crs;
    }

    /**
     * Execute query
     * @param queryStmt {String} Query
     * @param params {Object[]} Params
     * @return CachedRowSet
     * @throws SQLException
     */
    public CachedRowSet dbExecuteQuery(String queryStmt, Object... params) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        CachedRowSet crs = null;
        try {
            dbConnect();
            stmt = conn.prepareStatement(queryStmt);

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            resultSet = stmt.executeQuery();

            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(resultSet);

        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
        return crs;
    }
}