package Dao;

import Models.Athlete;
import Models.Sport;
import Models.Team;
import Utils.ConnectionsUtlis;
import Models.OlympicRecord;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OlympicRecordDao {
    public static List<OlympicRecord> getOlympicRecords() throws SQLException {
        List<OlympicRecord> records = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblOlympicRecord;");
        if (rs != null) {
            while (rs.next()) {
                int idSport = rs.getInt("idSport");
                int year = rs.getInt("year");
                int idAthlete = rs.getInt("idAthlete");
                int idTeam = rs.getInt("idTeam");
                int timeMS = rs.getInt("timeMS");
                int medals = rs.getInt("medals");

                SportDao sportDao = new SportDao();
                Sport sport = sportDao.getSportById(idSport);
                AthleteDao athleteDao = new AthleteDao();
                Athlete athlete = athleteDao.getAthleteById(idAthlete);
                Team team = TeamDao.getTeamById(idTeam);

                OlympicRecord record = new OlympicRecord(sport, year, athlete, team, timeMS, medals);
                records.add(record);
            }
        } else {
            System.out.println("ResultSet is null. No results for Olympic Record found.");
        }
        return records;
    }

    public static void addOlympicRecord(OlympicRecord record) throws SQLException {
        String query = "INSERT INTO tblOlympicRecord (idSport, year, idAthlete, idTeam, timeMS, medals) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, record.getSport().getIdSport());
            stmt.setInt(2, record.getYear());
            stmt.setInt(3, record.getAthlete().getIdAthlete());
            stmt.setInt(4, record.getTeam().getIdTeam());
            stmt.setInt(5, record.getTimeMS());
            stmt.setInt(6, record.getMedals());
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

    public static void removeOlympicRecord(int idSport, int year) throws SQLException {
        String query = "DELETE FROM tblOlympicRecord WHERE idSport = ? AND year = ?";
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

    public static void updateOlympicRecord(OlympicRecord record) throws SQLException {
        String query = "UPDATE tblOlympicRecord SET idAthlete = ?, idTeam = ?, timeMS = ?, medals = ? WHERE idSport = ? AND year = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, record.getAthlete().getIdAthlete());
            stmt.setInt(2, record.getTeam().getIdTeam());
            stmt.setInt(3, record.getTimeMS());
            stmt.setInt(4, record.getMedals());
            stmt.setInt(5, record.getSport().getIdSport());
            stmt.setInt(6, record.getYear());
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

    public OlympicRecord getOlympicRecordById(int idSport, int year) throws SQLException {
        String query = "SELECT * FROM tblOlympicRecord WHERE idSport = ? AND year = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport, year);
        if (rs != null && rs.next()) {
            int idAthlete = rs.getInt("idAthlete");
            int idTeam = rs.getInt("idTeam");
            int timeMS = rs.getInt("timeMS");
            int medals = rs.getInt("medals");

            SportDao sportDao = new SportDao();
            Sport sport = sportDao.getSportByIdV2(idSport);
            AthleteDao athleteDao = new AthleteDao();
            Athlete athlete = athleteDao.getAthleteById(idAthlete);
            TeamDao teamDao = new TeamDao();
            Team team = teamDao.getTeamByIdV2(idTeam);
            return new OlympicRecord(sport, year, athlete, team, timeMS, medals);
        }
        return null;
    }

    public static OlympicRecord getOlympicRecordByIdV2(int idSport, int year) throws SQLException {
        String query = "SELECT * FROM tblOlympicRecord WHERE idSport = ? AND year = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport, year);
        if (rs != null && rs.next()) {
            int idAthlete = rs.getInt("idAthlete");
            int idTeam = rs.getInt("idTeam");
            int timeMS = rs.getInt("timeMS");
            int medals = rs.getInt("medals");

            return new OlympicRecord(idSport, year, idAthlete, idTeam, timeMS, medals);
        }
        return null;
    }

    public Integer getOlympicRecord(int idSport) throws SQLException {
        String query = "SELECT result FROM tblOlympicRecord WHERE idSport = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        try {
            if (rs != null && rs.next()) {
                return rs.getInt("result");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean setNewOlympicRecordAthleteOne(int idSport, int year, int idAthlete, int resultado) throws SQLException {
        String query = "UPDATE tblOlympicRecord SET year = ?, idAthlete = ?, result = ? WHERE idSport = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, year);
            stmt.setInt(2, idAthlete);
            stmt.setInt(3, resultado);
            stmt.setInt(4, idSport);
            stmt.executeUpdate();
            return true;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public boolean setNewOlympicRecordTeamOne(int idSport, int year, int idTeam, int resultado) throws SQLException {
        String query = "UPDATE tblOlympicRecord SET year = ?, idTeam = ?, result = ? WHERE idSport = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, year);
            stmt.setInt(2, idTeam);
            stmt.setInt(3, resultado);
            stmt.setInt(4, idSport);
            stmt.executeUpdate();
            return true;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public Integer getOlympicRecordMultipleGames(int idSport) throws SQLException {
        Integer olympicRecordMedals = 0;
        String query = "SELECT medals " +
                "FROM tblOlympicRecord " +
                "WHERE idSport = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        if (rs != null && rs.next()) {
            olympicRecordMedals = rs.getInt("medals");
            return olympicRecordMedals;
        }
        return olympicRecordMedals;
    }

    public boolean setNewOlympicRecordTeamMultiple(int idSport, int year, int idTeam, int totalMedals) throws SQLException {
        String query = "UPDATE tblOlympicRecord SET year = ?, idTeam = ?, medals = ? WHERE idSport = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, year);
            stmt.setInt(2, idTeam);
            stmt.setInt(3, totalMedals);
            stmt.setInt(4, idSport);
            stmt.executeUpdate();
            return true;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public boolean setNewOlympicRecordAthleteMultiple(int idSport, int year, int idAthlete, int totalMedals) throws SQLException {
        String query = "UPDATE tblOlympicRecord SET year = ?, idAthlete = ?, medals = ? WHERE idSport = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, year);
            stmt.setInt(2, idAthlete);
            stmt.setInt(3, totalMedals);
            stmt.setInt(4, idSport);
            stmt.executeUpdate();
            return true;
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