package Dao;

import Models.*;
import Utils.ConnectionsUtlis;
import java.sql.ResultSet;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SportDao {
    /**
     * Get all sports
     * @return {List<Sport>} List of sports
     * @throws SQLException
     */
    public List<Sport> getSports() throws SQLException {
        List<Sport> sports = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT s.*," +
                "g.description AS genderDescription," +
                "r.year AS olympicYear," +
                "r.result," +
                "r.medals " +
                "FROM tblSport s " +
                "JOIN tblGender g ON s.idGender = g.idGender " +
                "LEFT JOIN tblOlympicRecord r ON s.idSport = r.idSport;");
        if (rs != null) {
            while (rs.next()) {
                int idSport = rs.getInt("idSport");
                String type = rs.getString("type");
                int idGender = rs.getInt("idGender");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int minParticipants = rs.getInt("minParticipants");
                String scoringMeasure = rs.getString("scoringMeasure");
                String oneGame = rs.getString("oneGame");

                String genderDescription = rs.getString("genderDescription");
                Gender gender = new Gender(idGender, genderDescription);

                OlympicRecord olympicRecord = OlympicRecordDao.getOlympicRecordByIdV2(idSport, rs.getInt("olympicYear"));

                List<WinnerOlympic> winnerOlympics = WinnerOlympicDao.getWinnerOlympicsBySportV2(idSport);
                List<Rule> rules = RuleDao.getRulesBySportV2(idSport);

                int resultMin = rs.getInt("resultMin");
                int resultMax = rs.getInt("resultMax");

                Sport sport = new Sport(idSport, type, gender, name, description, minParticipants, scoringMeasure, oneGame, olympicRecord, winnerOlympics, rules, resultMin, resultMax);
                sports.add(sport);
            }
        } else {
            System.out.println("ResultSet is null. No results for Sport found.");
        }
        return sports;
    }

    /**
     * Get all sports to show
     * @return {List<List>} List of sports
     * @throws SQLException
     */
    public List<List> getSportsToShow() throws SQLException {
        List<List> sports = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT s.*, " +
                "g.description AS genderDescription " +
                "FROM tblSport s " +
                "JOIN tblGender g ON s.idGender = g.idGender;");
        if (rs != null) {
            while (rs.next()) {
                List<String> sport = new ArrayList<>();
                sport.add(rs.getString("idSport"));
                sport.add(rs.getString("type"));
                sport.add(rs.getString("genderDescription"));
                sport.add(rs.getString("name"));
                sport.add(rs.getString("description"));
                sport.add(rs.getString("minParticipants"));
                sport.add(rs.getString("scoringMeasure"));
                sport.add(rs.getString("oneGame"));
                sports.add(sport);
            }
        } else {
            System.out.println("ResultSet is null. No results for Sport found.");
        }
        return sports;
    }

    /**
     * Get sports to start
     * @param year {int} Year
     * @return {List<List>} List of sports
     * @throws SQLException
     */
    public List<List> getSportsToStart(int year) throws SQLException {
        List<List> sports = new ArrayList<>();
        String query = "SELECT DISTINCT s.idSport, s.type, s.idGender, s.name, s.description," +
                " s.minParticipants, s.scoringMeasure, s.oneGame, r.idStatus, " +
                "g.description AS genderDescription " +
                "FROM tblSport s " +
                "JOIN tblGender g ON s.idGender = g.idGender " +
                "JOIN tblRegistration r ON s.idSport = r.idSport " +
                "WHERE r.idStatus >= 3 AND r.year = ?;";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, year);
        if (rs != null) {
            while (rs.next()) {
                List<String> sport = new ArrayList<>();
                sport.add(rs.getString("idSport"));
                sport.add(rs.getString("type"));
                sport.add(rs.getString("genderDescription"));
                sport.add(rs.getString("name"));
                sport.add(rs.getString("description"));
                sport.add(rs.getString("minParticipants"));
                sport.add(rs.getString("scoringMeasure"));
                sport.add(rs.getString("oneGame"));
                sport.add(rs.getString("idStatus"));
                sports.add(sport);
            }
        } else {
            System.out.println("ResultSet is null. No results for Sport found.");
        }
        return sports;
    }

    /**
     * Get number of participants in a sport
     * @param idSport {int} Id sport
     * @param year {int} Year
     * @return int
     * @throws SQLException
     */
    public int getNumberParticipantsSport(int idSport, int year) throws SQLException {
        String query = "SELECT COUNT(*) AS quantidade " +
                "FROM tblRegistration " +
                "WHERE idSport = ? " +
                "AND year = ? " +
                "AND (idStatus = 3 OR idStatus = 4) ;";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport, year);
        int quantidade = 0;
        if (rs != null && rs.next()) {
            quantidade = rs.getInt("quantidade");
        }
        return quantidade;
    }

    /**
     * Verify ranges
     * @param idSport {int} Id sport
     * @return boolean
     * @throws SQLException
     */
    public boolean verifyRanges(int idSport) throws SQLException {
        String query = "SELECT resultMin, resultMax " +
                "FROM tblSport " +
                "WHERE idSport = ? AND (resultMin IS NOT NULL AND resultMax IS NOT NULL)";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        if (rs != null && rs.next()) {
            return true;
        }
        return false;
    }

    /**
     * Booted sport
     * @param idSport {int} Id sport
     * @return boolean
     * @throws SQLException
     */
    public boolean BootedSport(int idSport) throws SQLException {
        String query = "SELECT COUNT(*) AS quantidade " +
                "FROM tblResult " +
                "WHERE idSport = ?;";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        if (rs != null && rs.next()) {
            if (rs.getInt("quantidade") == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add sport
     * @param sport {Sport} Sport
     * @return int
     * @throws SQLException
     */
    public static int addSport(Sport sport) throws SQLException {
        String query = "INSERT INTO tblSport (type, idGender, name, description, minParticipants, scoringMeasure, oneGame) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, sport.getType());
            stmt.setInt(2, sport.getGenre().getIdGender());
            stmt.setString(3, sport.getName());
            stmt.setString(4, sport.getDesc());
            stmt.setInt(5, sport.getMinParticipants());
            stmt.setString(6, sport.getScoringMeasure());
            stmt.setString(7, sport.getOneGame());

            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                System.out.println("Generated Sport ID: " + generatedId);
                return generatedId;
            } else {
                throw new SQLException("Failed to retrieve the generated ID for the sport.");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Remove sport
     * @param idSport {int} Id sport
     * @throws SQLException
     */
    public static void removeSport(int idSport) throws SQLException {
        String query = "DELETE FROM tblSport WHERE idSport = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idSport);
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
     * Update sport
     * @param sport {Sport} Sport
     * @throws SQLException
     */
    public static void updateSport(Sport sport) throws SQLException {
        String query = "UPDATE tblSport SET type = ?, idGender = ?, name = ?, description = ?, minParticipants = ?, scoringMeasure = ?, oneGame = ?, resultMin = ?, resultMax = ? WHERE idSport = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, sport.getType());
            stmt.setInt(2, sport.getGenre().getIdGender());
            stmt.setString(3, sport.getName());
            stmt.setString(4, sport.getDesc());
            stmt.setInt(5, sport.getMinParticipants());
            stmt.setString(6, sport.getScoringMeasure());
            stmt.setString(7, sport.getOneGame());
            stmt.setInt(8, sport.getResultMin());
            stmt.setInt(9, sport.getResultMax());
            stmt.setInt(10, sport.getIdSport());

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
     * Get sport by id
     * @param idSport {int} Id sport
     * @return {Sport} Sport
     * @throws SQLException
     */
    public Sport getSportById(int idSport) throws SQLException {
        String query = "SELECT s.*, " +
                "g.description AS genderDescription, " +
                "r.year AS olympicYear, " +
                "r.result, " +
                "r.medals " +
                "FROM tblSport s " +
                "JOIN tblGender g ON s.idGender = g.idGender " +
                "LEFT JOIN tblOlympicRecord r ON s.idSport = r.idSport " +
                "WHERE s.idSport = ?";

        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        if (rs != null && rs.next()) {
            int idSportResult = rs.getInt("idSport");
            String type = rs.getString("type");
            int idGender = rs.getInt("idGender");
            String name = rs.getString("name");
            String description = rs.getString("description");
            int minParticipants = rs.getInt("minParticipants");
            String scoringMeasure = rs.getString("scoringMeasure");
            String oneGame = rs.getString("oneGame");
            String genderDescription = rs.getString("genderDescription");
            Gender gender = new Gender(idGender, genderDescription);

            OlympicRecordDao olympicRecordDao = new OlympicRecordDao();
            OlympicRecord olympicRecord = olympicRecordDao.getOlympicRecordById(idSportResult, rs.getInt("olympicYear"));

            WinnerOlympicDao winnerOlympicDao = new WinnerOlympicDao();
            List<WinnerOlympic> winnerOlympics = winnerOlympicDao.getWinnerOlympicsBySport(idSportResult);

            List<Rule> rules = RuleDao.getRulesBySport(idSportResult);

            return new Sport(idSportResult, type, gender, name, description, minParticipants, scoringMeasure, oneGame, olympicRecord, winnerOlympics, rules);
        }

        return null;
    }

    /**
     * Get sport by id V2
     * @param idSport {int} Id sport
     * @return {Sport} Sport
     * @throws SQLException
     */
    public Sport getSportByIdV2(int idSport) throws SQLException {
        String query = """
                    SELECT s.idSport, s.type, s.idGender, s.name, s.description, 
                           s.minParticipants, s.scoringMeasure, s.oneGame, 
                           g.description AS genderDescription, 
                           r.year AS olympicYear, r.result, r.medals 
                    FROM tblSport s 
                    JOIN tblGender g ON s.idGender = g.idGender 
                    LEFT JOIN tblOlympicRecord r ON s.idSport = r.idSport 
                    WHERE s.idSport = ?
                """;

        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        if (rs != null && rs.next()) {
            int idSportResult = rs.getInt("idSport");
            String type = rs.getString("type");
            int idGender = rs.getInt("idGender");
            String name = rs.getString("name");
            String description = rs.getString("description");
            int minParticipants = rs.getInt("minParticipants");
            String scoringMeasure = rs.getString("scoringMeasure");
            String oneGame = rs.getString("oneGame");

            OlympicRecord olympicRecord = OlympicRecordDao.getOlympicRecordByIdV2(idSportResult, rs.getInt("olympicYear"));

            List<WinnerOlympic> winnerOlympics = WinnerOlympicDao.getWinnerOlympicsBySportV2(idSportResult);
            List<Rule> rules = RuleDao.getRulesBySportV2(idSportResult);

            return new Sport(idSportResult, type, idGender, name, description,
                    minParticipants, scoringMeasure, oneGame, olympicRecord,
                    winnerOlympics, rules);
        }

        return null;
    }

    /**
     * Get sports by name
     * @param sportName {String} Sport name
     * @return {List<Sport>} List of sports
     * @throws SQLException
     */
    public List<Sport> getSportsByName(String sportName) throws SQLException {
        List<Sport> sports = new ArrayList<>();
        // Consulta otimizada para pegar somente o nome e id do esporte
        String query = "SELECT idSport, name, type FROM tblSport WHERE name = ?";

        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, sportName);
        if (rs != null) {
            while (rs.next()) {
                int idSport = rs.getInt("idSport");
                String name = rs.getString("name");
                String type = rs.getString("type");
                Sport sport = new Sport(idSport, name, type);
                sports.add(sport);
            }
        } else {
            System.out.println("No sports found with the specified name.");
        }
        return sports;
    }

    /**
     * Get all sports V2
     * @return {List<Sport>} List of sports
     * @throws SQLException
     */
    public static List<Sport> getAllSportsV2() throws SQLException {
        List<Sport> sports = new ArrayList<>();

        String query = "SELECT s.idSport, s.name, s.type, s.idGender, g.description AS genderDescription, " +
                "s.minParticipants, s.description AS sportDescription, s.scoringMeasure, s.oneGame, " +
                "s.resultMin, s.resultMax " +
                "FROM tblSport s " +
                "INNER JOIN tblGender g ON s.idGender = g.idGender " +
                "LEFT JOIN tblTeam t ON s.idSport = t.idSport " +
                "LEFT JOIN tblRegistration r ON s.idSport = r.idSport " +
                "WHERE t.idSport IS NULL AND r.idSport IS NULL";

        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query);

        if (rs != null) {
            while (rs.next()) {
                int idSport = rs.getInt("idSport");
                String name = rs.getString("name");
                String type = rs.getString("type");
                int idGender = rs.getInt("idGender");
                String genderDescription = rs.getString("genderDescription");
                int minParticipants = rs.getInt("minParticipants");
                String sportDescription = rs.getString("sportDescription");
                String scoringMeasure = rs.getString("scoringMeasure");
                String oneGame = rs.getString("oneGame");
                int resultMin = rs.getInt("resultMin");
                int resultMax = rs.getInt("resultMax");

                Gender gender = new Gender(idGender, genderDescription);

                Sport sport = new Sport(idSport, name, type, gender, sportDescription, scoringMeasure);
                sport.setMinParticipants(minParticipants);
                sport.setOneGame(oneGame);
                sport.setResultMin(resultMin);
                sport.setResultMax(resultMax);

                sports.add(sport);
            }
        } else {
            System.out.println("ResultSet is null. No results for Sport found.");
        }

        return sports;
    }

    /**
     * Update sport V2
     * @param sport {Sport} Sport
     * @throws SQLException
     */
    public static void updateSportV2(Sport sport) throws SQLException {
        String query = "UPDATE tblSport SET name = ?, description = ?, minParticipants = ? WHERE idSport = ?";

        try {
            Connection conn = ConnectionsUtlis.dbConnect();

            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, sport.getName());
            stmt.setString(2, sport.getDesc());
            stmt.setInt(3, sport.getMinParticipants());
            stmt.setInt(4, sport.getIdSport());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Sport updated successfully!");
            } else {
                System.out.println("No sport found with the provided ID.");
            }

            stmt.close();

        } catch (SQLException e) {
            System.err.println("Error updating sport: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Get sport type
     * @param idSport {int} Id sport
     * @return String
     * @throws SQLException
     */
    public String getType(int idSport) throws SQLException {
        String tipo = "";
        String query = "SELECT type " +
                "FROM tblSport " +
                "WHERE idSport = ?;";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        if (rs != null && rs.next()) {
            tipo = rs.getString("type");
        }
        return tipo;
    }

    /**
     * Get one game
     * @param idSport {int} Id sport
     * @return String
     * @throws SQLException
     */
    public String getOneGame(int idSport) throws SQLException {
        String oneGame = "";
        String query = "SELECT oneGame " +
                "FROM tblSport " +
                "WHERE idSport = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        if (rs != null && rs.next()) {
            oneGame = rs.getString("oneGame");
        }
        return oneGame;
    }

    /**
     * Get range
     * @param idSport {int} Id sport
     * @return List<Integer>
     * @throws SQLException
     */
    public List<Integer> getRange(int idSport) throws SQLException {
        List<Integer> range = new ArrayList<>();
        String query = "SELECT resultMin, resultMax " +
                "FROM tblSport " +
                "WHERE idSport = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        if (rs != null && rs.next()) {
            int min = rs.getInt("resultMin");
            int max = rs.getInt("resultMax");
            range.add(min);
            range.add(max);
        }
        return range;
    }
}


