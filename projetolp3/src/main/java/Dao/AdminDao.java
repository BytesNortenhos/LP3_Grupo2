package Dao;

import Utils.ConnectionsUtlis;
import models.Admin;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {
    public static List<Admin> getAdmins() throws SQLException {
        List<Admin> admins = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblAdmin;");
        if (rs != null) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String password = rs.getString("password");

                Admin admin = new Admin(id, password);
                admins.add(admin);
            }
        } else {
            System.out.println("ResultSet is null. No results for Admin found.");
        }
        return admins;
    }

    public static void addAdmin(Admin admin) throws SQLException {
        String query = "INSERT INTO tblAdmin (password) VALUES (?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, admin.getPassword());
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

    public static void removeAdmin(int id) throws SQLException {
        String query = "DELETE FROM tblAdmin WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
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

    public static void updateAdmin(Admin admin) throws SQLException {
        String query = "UPDATE tblAdmin SET password = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, admin.getPassword());
            stmt.setInt(2, admin.getId());
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

    public static Admin getAdminById(int id) throws SQLException {
        String query = "SELECT * FROM tblAdmin WHERE id = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, id);
        if (rs != null && rs.next()) {
            String password = rs.getString("password");
            return new Admin(id, password);
        }
        return null;
    }
}
