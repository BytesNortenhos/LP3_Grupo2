package Dao;

import Models.MedalType;
import Utils.ConnectionsUtlis;
import Models.Medal;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedalDao {
    public static List<Medal> getMedals() throws SQLException {
        List<Medal> medals = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(
                "SELECT m.idMedal, m.idAthlete, m.idTeam, m.year, " +
                        "mt.idMedalType, mt.description AS medalTypeDescription " +
                        "FROM tblMedal m " +
                        "INNER JOIN tblMedalType mt ON m.idMedalType = mt.idMedalType;");
        if (rs != null) {
            while (rs.next()) {
                int idMedal = rs.getInt("idMedal");
                int idAthlete = rs.getInt("idAthlete");
                int idTeam = rs.getInt("idTeam");
                int year = rs.getInt("year");
                MedalType medalType = new MedalType(rs.getInt("idMedalType"), rs.getString("medalTypeDescription"));
                Medal medal = new Medal(idMedal, idAthlete, idTeam, year, medalType);
                medals.add(medal);
            }
        } else {
            System.out.println("ResultSet is null. No results for Medal found.");
        }
        return medals;
    }

    public static void addMedal(Medal medal) throws SQLException {
        String query = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, medal.getIdAthlete());
            stmt.setInt(2, medal.getIdTeam());
            stmt.setInt(3, medal.getYear());
            stmt.setInt(4, medal.getMedalType().getId());
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

    public static void removeMedal(int idMedal) throws SQLException {
        String query = "DELETE FROM tblMedal WHERE idMedal = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idMedal);
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

    public static void updateMedal(Medal medal) throws SQLException {
        String query = "UPDATE tblMedal SET idAthlete = ?, idTeam = ?, year = ?, idMedalType = ? WHERE idMedal = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, medal.getIdAthlete());
            stmt.setInt(2, medal.getIdTeam());
            stmt.setInt(3, medal.getYear());
            stmt.setInt(4, medal.getMedalType().getId());
            stmt.setInt(5, medal.getIdMedal());
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

    public static Medal getMedalById(int idMedal) throws SQLException {
        String query = "SELECT m.idMedal, m.idAthlete, m.idTeam, m.year, " +
                "mt.idMedalType, mt.description AS medalTypeDescription " +
                "FROM tblMedal m " +
                "INNER JOIN tblMedalType mt ON m.idMedalType = mt.idMedalType " +
                "WHERE m.idMedal = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idMedal);
        if (rs != null && rs.next()) {
            int idAthlete = rs.getInt("idAthlete");
            int idTeam = rs.getInt("idTeam");
            int year = rs.getInt("year");
            int idMedalType = rs.getInt("idMedalType");
            MedalType medalType = new MedalType(rs.getInt("idMedalType"), rs.getString("medalTypeDescription"));
            return new Medal(idMedal, idAthlete, idTeam, year, medalType);
        }
        return null;
    }
}
