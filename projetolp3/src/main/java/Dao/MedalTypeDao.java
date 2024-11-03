package Dao;

import Utils.ConnectionsUtlis;
import models.MedalType;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedalTypeDao {
    public static List<MedalType> getMedalTypes() throws SQLException {
        List<MedalType> medalTypes = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblMedalType;");
        if (rs != null) {
            while (rs.next()) {
                int idMedalType = rs.getInt("idMedalType");
                String descMedalType = rs.getString("descMedalType");

                MedalType medalType = new MedalType(idMedalType, descMedalType);
                medalTypes.add(medalType);
            }
        } else {
            System.out.println("ResultSet is null. No results for MedalType found.");
        }
        return medalTypes;
    }

    public static void addMedalType(MedalType medalType) throws SQLException {
        String query = "INSERT INTO tblMedalType (descMedalType) VALUES (?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, medalType.getDescMedalType());
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

    public static void removeMedalType(int idMedalType) throws SQLException {
        String query = "DELETE FROM tblMedalType WHERE idMedalType = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idMedalType);
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

    public static void updateMedalType(MedalType medalType) throws SQLException {
        String query = "UPDATE tblMedalType SET descMedalType = ? WHERE idMedalType = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, medalType.getDescMedalType());
            stmt.setInt(2, medalType.getIdMedalType());
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

    public static MedalType getMedalTypeById(int idMedalType) throws SQLException {
        String query = "SELECT * FROM tblMedalType WHERE idMedalType = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idMedalType);
        if (rs != null && rs.next()) {
            String descMedalType = rs.getString("descMedalType");
            return new MedalType(idMedalType, descMedalType);
        }
        return null;
    }
}
