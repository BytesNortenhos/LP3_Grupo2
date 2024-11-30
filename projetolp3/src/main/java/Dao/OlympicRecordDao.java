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
    /**
     * Get all Olympic records
     * @return {List<OlympicRecord>} List of Olympic records
     * @throws SQLException
     */
    public static List<OlympicRecord> getOlympicRecords() throws SQLException {
        List<OlympicRecord> records = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblOlympicRecord;");
        if (rs != null) {
            while (rs.next()) {
                int idSport = rs.getInt("idSport");
                int year = rs.getInt("year");
                int idAthlete = rs.getInt("idAthlete");
                int idTeam = rs.getInt("idTeam");
                int result = rs.getInt("result");
                int medals = rs.getInt("medals");

                SportDao sportDao = new SportDao();
                Sport sport = sportDao.getSportById(idSport);
                AthleteDao athleteDao = new AthleteDao();
                Athlete athlete = athleteDao.getAthleteById(idAthlete);
                Team team = TeamDao.getTeamById(idTeam);

                OlympicRecord record = new OlympicRecord(sport, year, athlete, team, result, medals);
                records.add(record);
            }
        } else {
            System.out.println("ResultSet is null. No results for Olympic Record found.");
        }
        return records;
    }

    /**
     * Add Olympic record
     * @param record {OlympicRecord} Olympic record
     * @throws SQLException
     */
    public static void addOlympicRecord(OlympicRecord record) throws SQLException {
        String query = "INSERT INTO tblOlympicRecord (idSport, year, idAthlete, idTeam, result, medals) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, record.getSport().getIdSport());
            stmt.setInt(2, record.getYear());
            stmt.setInt(3, record.getAthlete().getIdAthlete());
            stmt.setInt(4, record.getTeam().getIdTeam());
            stmt.setInt(5, record.getresult());
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

    /**
     * Remove Olympic record
     * @param idSport {int} Sport id
     * @param year {int} Year
     * @throws SQLException
     */
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

    /**
     * Update Olympic record
     * @param record {OlympicRecord} Olympic record
     * @throws SQLException
     */
    public static void updateOlympicRecord(OlympicRecord record) throws SQLException {
        String query = "UPDATE tblOlympicRecord SET idAthlete = ?, idTeam = ?, result = ?, medals = ? WHERE idSport = ? AND year = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, record.getAthlete().getIdAthlete());
            stmt.setInt(2, record.getTeam().getIdTeam());
            stmt.setInt(3, record.getresult());
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

    /**
     * Get Olympic record by id
     * @param idSport {int} Sport id
     * @param year {int} Year
     * @return {OlympicRecord} Olympic record
     * @throws SQLException
     */
    public OlympicRecord getOlympicRecordById(int idSport, int year) throws SQLException {
        String query = "SELECT * FROM tblOlympicRecord WHERE idSport = ? AND year = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport, year);
        if (rs != null && rs.next()) {
            int idAthlete = rs.getInt("idAthlete");
            int idTeam = rs.getInt("idTeam");
            int result = rs.getInt("result");
            int medals = rs.getInt("medals");

            SportDao sportDao = new SportDao();
            Sport sport = sportDao.getSportByIdV2(idSport);
            AthleteDao athleteDao = new AthleteDao();
            Athlete athlete = athleteDao.getAthleteById(idAthlete);
            TeamDao teamDao = new TeamDao();
            Team team = teamDao.getTeamByIdV2(idTeam);
            return new OlympicRecord(sport, year, athlete, team, result, medals);
        }
        return null;
    }

    /**
     * Get Olympic record by id V2
     * @param idSport {int} Sport id
     * @param year {int} Year
     * @return {OlympicRecord} Olympic record
     * @throws SQLException
     */
    public static OlympicRecord getOlympicRecordByIdV2(int idSport, int year) throws SQLException {
        String query = "SELECT * FROM tblOlympicRecord WHERE idSport = ? AND year = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport, year);
        if (rs != null && rs.next()) {
            int idAthlete = rs.getInt("idAthlete");
            int idTeam = rs.getInt("idTeam");
            int result = rs.getInt("result");
            int medals = rs.getInt("medals");

            return new OlympicRecord(idSport, year, idAthlete, idTeam, result, medals);
        }
        return null;
    }
}