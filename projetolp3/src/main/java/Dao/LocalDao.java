package Dao;

import Utils.ConnectionsUtlis;
import Models.Local;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocalDao {
    /**
     * Get all locals
     * @return {List<Local>} List of locals
     * @throws SQLException
     */
    public static List<Local> getLocals() throws SQLException {
        List<Local> locals = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblLocal;");
        if (rs != null) {
            while (rs.next()) {
                int idLocal = rs.getInt("idLocal");
                String name = rs.getString("name");
                String type = rs.getString("type");
                String address = rs.getString("address");
                String city = rs.getString("city");
                int capacity = rs.getInt("capacity");
                int constructionYear = rs.getInt("constructionYear");

                Local local = new Local(idLocal, name, type, address, city, capacity, constructionYear);
                locals.add(local);
            }
        } else {
            System.out.println("ResultSet is null. No results for Local found.");
        }
        return locals;
    }

    public List<Local> getLocalsByYear(int year) throws SQLException{
        List<Local> locals = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblLocal WHERE constructionYear = ?;", year);
        if (rs != null) {
            while (rs.next()) {
                int idLocal = rs.getInt("idLocal");
                String name = rs.getString("name");
                String type = rs.getString("type");
                String address = rs.getString("address");
                String city = rs.getString("city");
                int capacity = rs.getInt("capacity");
                int constructionYear = rs.getInt("constructionYear");

                Local local = new Local(idLocal, name, type, address, city, capacity, constructionYear);
                locals.add(local);
            }
        } else {
            System.out.println("ResultSet is null. No results for Local found.");
        }
        return locals;
    }

    /**
     * Add local
     * @param local {Local} Local
     * @throws SQLException
     */
    public static void addLocal(Local local) throws SQLException {
        String query = "INSERT INTO tblLocal (name, type, address, city, capacity, constructionYear) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, local.getName());
            stmt.setString(2, local.getType());
            stmt.setString(3, local.getAddress());
            stmt.setString(4, local.getCity());
            stmt.setInt(5, local.getCapacity());
            stmt.setInt(6, local.getConstructionYear());
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Remove local
     * @param idLocal {int} Local ID
     * @throws SQLException
     */
    public static void removeLocal(int idLocal) throws SQLException {
        String query = "DELETE FROM tblLocal WHERE idLocal = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idLocal);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Update local
     * @param local {Local} Local
     * @throws SQLException
     */
    public static void updateLocal(Local local) throws SQLException {
        String query = "UPDATE tblLocal SET name = ?, type = ?, address = ?, city = ?, capacity = ?, constructionYear = ? " +
                "WHERE idLocal = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, local.getName());
            stmt.setString(2, local.getType());
            stmt.setString(3, local.getAddress());
            stmt.setString(4, local.getCity());
            stmt.setInt(5, local.getCapacity());
            stmt.setInt(6, local.getConstructionYear());
            stmt.setInt(7, local.getIdLocal());
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Get local by ID
     * @param idLocal {int} Local ID
     * @return {Local} Local
     * @throws SQLException
     */
    public static Local getLocalById(int idLocal) throws SQLException {
        String query = "SELECT * FROM tblLocal WHERE idLocal = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idLocal);
        if (rs != null && rs.next()) {
            String name = rs.getString("name");
            String type = rs.getString("type");
            String address = rs.getString("address");
            String city = rs.getString("city");
            int capacity = rs.getInt("capacity");
            int constructionYear = rs.getInt("constructionYear");
            return new Local(idLocal, name, type, address, city, capacity, constructionYear);
        }
        return null;
    }
}
