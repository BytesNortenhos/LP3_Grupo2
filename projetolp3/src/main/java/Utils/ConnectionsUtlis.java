package Utils;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class ConnectionsUtlis{
    private static final String connStr = "jdbc:sqlserver://ctespbd.dei.isep.ipp.pt;databaseName=2024_LP3_G2_FEIRA;user=2024_LP3_G2_FEIRA;password=LP3g2!2024;encryption=true;trustServerCertificate=true;";
        private static Connection conn = null;

        public static Connection dbConnect() throws SQLException {
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

        public static void dbDisconnect() throws SQLException {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (Exception e) {
                throw e;
            }
        }

        public static CachedRowSet dbExecuteQuery(String queryStmt) throws SQLException {
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

        public static CachedRowSet dbExecuteQuery(String queryStmt, Object... params) throws SQLException {
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