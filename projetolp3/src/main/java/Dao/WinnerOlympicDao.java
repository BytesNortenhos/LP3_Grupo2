package Dao;

import Models.*;
import Utils.ConnectionsUtlis;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WinnerOlympicDao {
    public static List<WinnerOlympic> getWinnerOlympics() throws SQLException {
        List<WinnerOlympic> winners = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblWinnerOlympic");
        if (rs != null) {
            while (rs.next()) {
                int idSport = rs.getInt("idSport");
                int year = rs.getInt("year");
                int idAthlete = rs.getInt("idAthlete");
                int idTeam = rs.getInt("idTeam");
                int timeMS = rs.getInt("timeMS");
                int idMedal = rs.getInt("medalId");
                Sport sport = SportDao.getSportById(idSport);
                Athlete athlete = AthleteDao.getAthleteById(idAthlete);
                Team team = TeamDao.getTeamById(idTeam);
                Medal medal = MedalDao.getMedalById(idMedal);

                WinnerOlympic winner = new WinnerOlympic(sport, year, athlete, team, timeMS, medal);
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

            stmt.setInt(1, winner.getSport().getIdSport());
            stmt.setInt(2, winner.getYear());
            stmt.setInt(3, winner.getAthlete().getIdAthlete());
            stmt.setInt(4, winner.getTeam().getIdTeam());
            stmt.setInt(5, winner.getTimeMS());
            stmt.setInt(6, winner.getMedal().getIdMedal());
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

            stmt.setInt(1, winner.getAthlete().getIdAthlete());
            stmt.setInt(2, winner.getTeam().getIdTeam());
            stmt.setInt(3, winner.getTimeMS());
            stmt.setInt(4, winner.getMedal().getIdMedal());
            stmt.setInt(5, winner.getSport().getIdSport());
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
            int idMedal = rs.getInt("medalId");
            Sport sport = SportDao.getSportById(idSport);
            Athlete athlete = AthleteDao.getAthleteById(idAthlete);
            Team team = TeamDao.getTeamById(idTeam);
            Medal medal = MedalDao.getMedalById(idMedal);
            return new WinnerOlympic(sport, year, athlete, team, timeMS, medal);
        }
        return null;
    }

    public static List<WinnerOlympic> getWinnerOlympicsBySport(int idSport) throws SQLException {
        List<WinnerOlympic> winnerOlympics = new ArrayList<>();
        String query = (" SELECT wo.*, a.idAthlete,a.name AS athleteName," +
                "t.idTeam, t.name AS teamName," +
                "m.idMedal, mt.descMedalType " +
                "FROM tblWinnerOlympic wo " +
                "JOIN tblAthlete a ON wo.idAthlete = a.idAthlete " +
                "JOIN tblTeam t ON wo.idTeam = t.idTeam " +
                "JOIN tblMedal m ON wo.idMedal = m.idMedal " +
                "JOIN tblMedalType mt ON m.idMedalType = mt.idMedalType " +
                "WHERE wo.idSport = ?");
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        while (rs.next()) {
            int year = rs.getInt("year");
            int timeMS = rs.getInt("timeMS");

            Sport sport = SportDao.getSportByIdV2(idSport);

            int idAthlete = rs.getInt("idAthlete");
            Athlete athlete = AthleteDao.getAthleteById(idAthlete);

            int idTeam = rs.getInt("idTeam");
            Team team = TeamDao.getTeamByIdV2(idTeam);

            int idMedal = rs.getInt("idMedal");
            String descMedalType = rs.getString("descMedalType");
            MedalType medalType = new MedalType(idMedal, descMedalType);
            Medal medal = new Medal(idMedal, athlete, team, year, medalType);

            WinnerOlympic winnerOlympic = new WinnerOlympic(sport, year, athlete, team, timeMS, medal);
            winnerOlympics.add(winnerOlympic);
        }
        return winnerOlympics;
    }
    public static List<WinnerOlympic> getWinnerOlympicsBySportV2(int idSport) throws SQLException {
        List<WinnerOlympic> winnerOlympics = new ArrayList<>();
        String query = ("SELECT wo.*, a.idAthlete, a.name AS athleteName, " +
                "t.idTeam, t.name AS teamName, " +
                "m.idMedal, mt.descMedalType " +
                "FROM tblWinnerOlympic wo " +
                "JOIN tblAthlete a ON wo.idAthlete = a.idAthlete " +
                "JOIN tblTeam t ON wo.idTeam = t.idTeam " +
                "JOIN tblMedal m ON wo.idMedal = m.idMedal " +
                "JOIN tblMedalType mt ON m.idMedalType = mt.idMedalType " +
                "WHERE wo.idSport = ?");
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);

        while (rs.next()) {
            int year = rs.getInt("year");
            int timeMS = rs.getInt("timeMS");

            int idAthlete = rs.getInt("idAthlete");
            int idTeam = rs.getInt("idTeam");
            int idMedal = rs.getInt("idMedal");

            // Ainda criamos o objeto Medal porque ele é um parâmetro no construtor
            String descMedalType = rs.getString("descMedalType");
            MedalType medalType = new MedalType(idMedal, descMedalType);
            Medal medal = new Medal(idMedal, idAthlete, idTeam, year, medalType);

            // Agora usamos o novo construtor que aceita IDs ao invés de objetos
            WinnerOlympic winnerOlympic = new WinnerOlympic(idSport, year, idAthlete, idTeam, timeMS, medal);
            winnerOlympics.add(winnerOlympic);
        }
        return winnerOlympics;
    }

}