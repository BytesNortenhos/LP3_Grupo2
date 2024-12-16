package Dao;

import Utils.ConnectionsUtlis;
import Models.MedalType;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedalTypeDao {
    /**
     * Get all medal types
     * @return {List<MedalType>} List of medal types
     * @throws SQLException
     */
    public List<MedalType> getMedalTypes() throws SQLException {
        List<MedalType> medalTypes = new ArrayList<>();
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery("SELECT * FROM tblMedalType;");
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

    /**
     * Add medal type
     * @param medalType {MedalType} Medal type
     * @throws SQLException
     */
    public void addMedalType(MedalType medalType) throws SQLException {
        String query = "INSERT INTO tblMedalType (descMedalType) VALUES (?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
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

    /**
     * Remove medal type
     * @param idMedalType {int} Medal type id
     * @throws SQLException
     */
    public void removeMedalType(int idMedalType) throws SQLException {
        String query = "DELETE FROM tblMedalType WHERE idMedalType = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
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

    /**
     * Update medal type
     * @param medalType {MedalType} Medal type
     * @throws SQLException
     */
    public void updateMedalType(MedalType medalType) throws SQLException {
        String query = "UPDATE tblMedalType SET descMedalType = ? WHERE idMedalType = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, medalType.getDescMedalType());
            stmt.setInt(2, medalType.getId());
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
     * Get medal type by id
     * @param idMedalType {int} Medal type id
     * @return {MedalType} Medal type
     * @throws SQLException
     */
    public MedalType getMedalTypeById(int idMedalType) throws SQLException {
        String query = "SELECT * FROM tblMedalType WHERE idMedalType = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idMedalType);
        if (rs != null && rs.next()) {
            String descMedalType = rs.getString("descMedalType");
            return new MedalType(idMedalType, descMedalType);
        }
        return null;
    }
}
