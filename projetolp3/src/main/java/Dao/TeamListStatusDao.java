package Dao;

import Models.TeamListStatus;
import Utils.ConnectionsUtlis;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamListStatusDao {
    public static List<TeamListStatus> getTeamListStatuses() throws SQLException {
        List<TeamListStatus> statuses = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblTeamListStatus;");
        if (rs != null) {
            while (rs.next()) {
                int idStatus = rs.getInt("idStatus");
                String description = rs.getString("description");

                TeamListStatus status = new TeamListStatus(idStatus, description);
                statuses.add(status);
            }
        } else {
            System.out.println("ResultSet is null. No results for Resgistration Status found.");
        }
        return statuses;
    }

    public static void addTeamListStatus(TeamListStatus status) throws SQLException {
        String query = "INSERT INTO tblTeamListStatus (description) VALUES (?)";
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

    public static void removeTeamListStatus(int idStatus) throws SQLException {
        String query = "DELETE FROM tblTeamListStatus WHERE idStatus = ?";
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

    public static void updateTeamListStatus(TeamListStatus status) throws SQLException {
        String query = "UPDATE tblTeamListStatus SET description = ? WHERE idStatus = ?";
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

    public static TeamListStatus getTeamListStatusById(int idStatus) throws SQLException {
        String query = "SELECT * FROM tblTeamListStatus WHERE idStatus = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idStatus);
        if (rs != null && rs.next()) {
            String description = rs.getString("description");
            return new TeamListStatus(idStatus, description);
        }
        return null;
    }
}
