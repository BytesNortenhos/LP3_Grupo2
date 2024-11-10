package Dao;

import Utils.ConnectionsUtlis;
import Models.RegistrationStatus;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationStatusDao {
    public static List<RegistrationStatus> getRegistrationStatuses() throws SQLException {
        List<RegistrationStatus> statuses = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblRegistrationStatus;");
        if (rs != null) {
            while (rs.next()) {
                int idStatus = rs.getInt("idStatus");
                String description = rs.getString("description");

                RegistrationStatus status = new RegistrationStatus(idStatus, description);
                statuses.add(status);
            }
        } else {
            System.out.println("ResultSet is null. No results for Resgistration Status found.");
        }
        return statuses;
    }

    public static void addRegistrationStatus(RegistrationStatus status) throws SQLException {
        String query = "INSERT INTO tblRegistrationStatus (description) VALUES (?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, status.getDesc());
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

    public static void removeRegistrationStatus(int idStatus) throws SQLException {
        String query = "DELETE FROM tblRegistrationStatus WHERE idStatus = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idStatus);
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

    public static void updateRegistrationStatus(RegistrationStatus status) throws SQLException {
        String query = "UPDATE tblRegistrationStatus SET description = ? WHERE idStatus = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, status.getDesc());
            stmt.setInt(2, status.getIdStatus());
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

    public static RegistrationStatus getRegistrationStatusById(int idStatus) throws SQLException {
        String query = "SELECT * FROM tblRegistrationStatus WHERE idStatus = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idStatus);
        if (rs != null && rs.next()) {
            String description = rs.getString("description");
            return new RegistrationStatus(idStatus, description);
        }
        return null;
    }
}
