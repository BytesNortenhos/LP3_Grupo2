package Dao;

import Utils.ConnectionsUtlis;
import Models.Gender;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenderDao {
    public static List<Gender> getGenders() throws SQLException {
        List<Gender> genders = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblGender;");
        if (rs != null) {
            while (rs.next()) {
                int idGender = rs.getInt("idGender");
                String description = rs.getString("description");

                Gender gender = new Gender(idGender, description);
                genders.add(gender);
            }
        } else {
            System.out.println("ResultSet is null. No results for Gender found.");
        }
        return genders;
    }

    public static void addGender(Gender gender) throws SQLException {
        String query = "INSERT INTO tblGender (description) VALUES (?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, gender.getDesc());
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

    public static void removeGender(int idGender) throws SQLException {
        String query = "DELETE FROM tblGender WHERE idGender = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idGender);
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

    public static void updateGender(Gender gender) throws SQLException {
        String query = "UPDATE tblGender SET description = ? WHERE idGender = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, gender.getDesc());
            stmt.setInt(2, gender.getIdGender());
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

    public static Gender getGenderById(int idGender) throws SQLException {
        String query = "SELECT * FROM tblGender WHERE idGender = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idGender);
        if (rs != null && rs.next()) {
            String description = rs.getString("description");
            return new Gender(idGender, description);
        }
        return null;
    }
}
