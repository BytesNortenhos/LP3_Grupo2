package Dao;

import Utils.ConnectionsUtlis;
import models.WinnerOlympic;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WinnerOlympicDao {
    public static List<WinnerOlympic> getWinnerOlympics() throws SQLException {
        List<WinnerOlympic> winners = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblWinnerOlympic;");
        if (rs != null) {
            while (rs.next()) {
                int idSport = rs.getInt("idSport");
                int year = rs.getInt("year");
                int idAthlete = rs.getInt("idAthlete");
                int idTeam = rs.getInt("idTeam");
                int timeMS = rs.getInt("timeMS");
                int idMedal = rs.getInt("idMedal");

                WinnerOlympic winner = new WinnerOlympic(idSport, year, idAthlete, idTeam, timeMS, idMedal);
                winners.add(winner);
            }
        } else {
            System.out.println("ResultSet is null. No results for Olympic Winner found.");
        }
        return winners;
    }

    public static void addWinnerOlympic(WinnerOlympic winner) throws SQLException {
        String query = "INSERT INTO tblWinnerOlympic (idSport, year, idAthlete, idTeam, timeMS, idMedal) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, winner.getIdSport());
            stmt.setInt(2, winner.getYear());
            stmt.setInt(3, winner.getIdAthlete());
            stmt.setInt(4, winner.getIdTeam());
            stmt.setInt(5, winner.getTimeMS());
            stmt.setInt(6, winner.getIdMedal());
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

    public static void removeWinnerOlympic(int idSport, int year) throws SQLException {
        String query = "DELETE FROM tblWinnerOlympic WHERE idSport = ? AND year = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idSport);
            stmt.setInt(2, year);
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

    public static void updateWinnerOlympic(WinnerOlympic winner) throws SQLException {
        String query = "UPDATE tblWinnerOlympic SET idAthlete = ?, idTeam = ?, timeMS = ?, idMedal = ? WHERE idSport = ? AND year = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, winner.getIdAthlete());
            stmt.setInt(2, winner.getIdTeam());
            stmt.setInt(3, winner.getTimeMS());
            stmt.setInt(4, winner.getIdMedal());
            stmt.setInt(5, winner.getIdSport());
            stmt.setInt(6, winner.getYear());
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

    public static WinnerOlympic getWinnerOlympicById(int idSport, int year) throws SQLException {
        String query = "SELECT * FROM tblWinnerOlympic WHERE idSport = ? AND year = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport, year);
        if (rs != null && rs.next()) {
            int idAthlete = rs.getInt("idAthlete");
            int idTeam = rs.getInt("idTeam");
            int timeMS = rs.getInt("timeMS");
            int idMedal = rs.getInt("idMedal");
            return new WinnerOlympic(idSport, year, idAthlete, idTeam, timeMS, idMedal);
        }
        return null;
    }
}
