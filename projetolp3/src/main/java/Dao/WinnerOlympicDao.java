package Dao;

import Models.*;
import Utils.ConnectionsUtlis;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WinnerOlympicDao {
    /**
     * Get all Olympic winners
     * @return {List<WinnerOlympic>} List of Olympic winners
     * @throws SQLException
     */
    public List<WinnerOlympic> getWinnerOlympics() throws SQLException {
        List<WinnerOlympic> winners = new ArrayList<>();
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery("SELECT * FROM tblWinnerOlympic");
        if (rs != null) {
            while (rs.next()) {
                int idSport = rs.getInt("idSport");
                int year = rs.getInt("year");
                int idAthlete = rs.getInt("idAthlete");
                int idTeam = rs.getInt("idTeam");
                int result = rs.getInt("result");
                int idMedal = rs.getInt("medalId");
                SportDao sportDao = new SportDao();
                Sport sport = sportDao.getSportById(idSport);
                AthleteDao athleteDao = new AthleteDao();
                Athlete athlete = athleteDao.getAthleteById(idAthlete);
                Team team = TeamDao.getTeamById(idTeam);
                Medal medal = MedalDao.getMedalById(idMedal);

                WinnerOlympic winner = new WinnerOlympic(sport, year, athlete, team, result, medal);
                winners.add(winner);
            }
        } else {
            System.out.println("ResultSet is null. No results for Olympic Winner found.");
        }
        return winners;
    }

    /**
     * Add Olympic winner
     * @param winner {WinnerOlympic} Olympic winner
     * @throws SQLException
     */
    public void addWinnerOlympic(WinnerOlympic winner) throws SQLException {
        String query = "INSERT INTO tblWinnerOlympic (idSport, year, idAthlete, idTeam, result, idMedal) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, winner.getSport().getIdSport());
            stmt.setInt(2, winner.getYear());
            stmt.setInt(3, winner.getAthlete().getIdAthlete());
            stmt.setInt(4, winner.getTeam().getIdTeam());
            stmt.setInt(5, winner.getresult());
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

    /**
     * Remove Olympic winner
     * @param idSport {int} Id sport
     * @param year {int} Year
     * @throws SQLException
     */
    public static void removeWinnerOlympic(int idSport, int year) throws SQLException {
        String query = "DELETE FROM tblWinnerOlympic WHERE idSport = ? AND year = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
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

    /**
     * Update Olympic winner
     * @param winner {WinnerOlympic} Olympic winner
     * @throws SQLException
     */
    public void updateWinnerOlympic(WinnerOlympic winner) throws SQLException {
        String query = "UPDATE tblWinnerOlympic SET idAthlete = ?, idTeam = ?, result = ?, idMedal = ? WHERE idSport = ? AND year = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, winner.getAthlete().getIdAthlete());
            stmt.setInt(2, winner.getTeam().getIdTeam());
            stmt.setInt(3, winner.getresult());
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

    /**
     * Get Olympic winner by sport and year
     * @param idSport {int} Id sport
     * @param year {int} Year
     * @return {WinnerOlympic} Olympic winner
     * @throws SQLException
     */
    public WinnerOlympic getWinnerOlympicById(int idSport, int year) throws SQLException {
        String query = "SELECT * FROM tblWinnerOlympic WHERE idSport = ? AND year = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idSport, year);
        if (rs != null && rs.next()) {
            int idAthlete = rs.getInt("idAthlete");
            int idTeam = rs.getInt("idTeam");
            int result = rs.getInt("result");
            int idMedal = rs.getInt("idMedal");
            SportDao sportDao = new SportDao();
            Sport sport = sportDao.getSportById(idSport);
            AthleteDao athleteDao = new AthleteDao();
            Athlete athlete = athleteDao.getAthleteById(idAthlete);
            Team team = TeamDao.getTeamById(idTeam);
            Medal medal = MedalDao.getMedalById(idMedal);
            return new WinnerOlympic(sport, year, athlete, team, result, medal);
        }
        return null;
    }

    /**
     * Get Olympic winners by sport
     * @param idSport {int} Id sport
     * @return {List<WinnerOlympic>} List of Olympic winners
     * @throws SQLException
     */
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
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idSport);
        while (rs.next()) {
            int year = rs.getInt("year");
            int result = rs.getInt("result");

            SportDao sportDao = new SportDao();
            Sport sport = sportDao.getSportByIdV2(idSport);

            int idAthlete = rs.getInt("idAthlete");
            AthleteDao athleteDao = new AthleteDao();
            Athlete athlete = athleteDao.getAthleteById(idAthlete);

            int idTeam = rs.getInt("idTeam");
            TeamDao teamDao = new TeamDao();
            Team team = teamDao.getTeamById(idTeam);

            int idMedal = rs.getInt("idMedal");
            String descMedalType = rs.getString("descMedalType");
            MedalType medalType = new MedalType(idMedal, descMedalType);
            Medal medal = new Medal(idMedal, athlete, team, year, medalType);

            WinnerOlympic winnerOlympic = new WinnerOlympic(sport, year, athlete, team, result, medal);
            winnerOlympics.add(winnerOlympic);
        }
        return winnerOlympics;
    }

    /**
     * Get Olympic winners by sport V2
     * @param idSport {int} Id sport
     * @return {List<WinnerOlympic>} List of Olympic winners
     * @throws SQLException
     */
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
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idSport);

        while (rs.next()) {
            int year = rs.getInt("year");
            int result = rs.getInt("result");

            int idAthlete = rs.getInt("idAthlete");
            int idTeam = rs.getInt("idTeam");
            int idMedal = rs.getInt("idMedal");

            String descMedalType = rs.getString("descMedalType");
            MedalType medalType = new MedalType(idMedal, descMedalType);
            Medal medal = new Medal(idMedal, idAthlete, idTeam, year, medalType);

            WinnerOlympic winnerOlympic = new WinnerOlympic(idSport, year, idAthlete, idTeam, result, medal);
            winnerOlympics.add(winnerOlympic);
        }
        return winnerOlympics;
    }

    /**
     * Get medals by athlete
     * @param idSport {int} Id sport
     * @param idAthlete {int} Id athlete
     * @return int
     * @throws SQLException
     */
    public int getMedalsByAthlete(int idSport, int idAthlete) throws SQLException {
        int totalMedals = 0;
        String query = "SELECT COUNT(idAthlete) AS num_medals " +
                "FROM tblWinnerOlympic " +
                "WHERE idSport = ? AND idAthlete = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idSport, idAthlete);
        if (rs != null && rs.next()) {
            totalMedals = rs.getInt("num_medals");
            return totalMedals;
        }
        return totalMedals;
    }

    public int getMedalsByTeam(int idSport, int idTeam) throws SQLException {
        int totalMedals = 0;
        String query = "SELECT COUNT(idTeam) AS num_medals " +
                "FROM tblWinnerOlympic " +
                "WHERE idSport = ? AND idTeam = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idSport, idTeam);
        if (rs != null && rs.next()) {
            totalMedals = rs.getInt("num_medals");
            return totalMedals;
        }
        return totalMedals;
    }

    public void addWinnerOlympicTeamMultiple(int idSport, int year, int idTeam) throws SQLException {
        String query = "INSERT INTO tblWinnerOlympic (idSport, year, idTeam) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSport);
            stmt.setInt(2, year);
            stmt.setInt(3, idTeam);
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

    public void addWinnerOlympicTeamOne(int idSport, int year, int idTeam, int result) throws SQLException {
        String query = "INSERT INTO tblWinnerOlympic (idSport, year, idTeam, result) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSport);
            stmt.setInt(2, year);
            stmt.setInt(3, idTeam);
            stmt.setInt(4, result);
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

    public void addWinnerOlympicAthleteMultiple(int idSport, int year, int idAthlete) throws SQLException {
        String query = "INSERT INTO tblWinnerOlympic (idSport, year, idAthlete) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSport);
            stmt.setInt(2, year);
            stmt.setInt(3, idAthlete);
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

    public void addWinnerOlympicAthleteOne(int idSport, int year, int idAthlete, int result) throws SQLException {
        String query = "INSERT INTO tblWinnerOlympic (idSport, year, idAthlete, result) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSport);
            stmt.setInt(2, year);
            stmt.setInt(3, idAthlete);
            stmt.setInt(4, result);
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

}