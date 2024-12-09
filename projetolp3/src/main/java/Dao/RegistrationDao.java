package Dao;

import Models.*;
import Utils.ConnectionsUtlis;
import java.util.Map;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDao {
    /**
     * Get all registrations
     * @return {List<Registration>} List of registrations
     * @throws SQLException
     */
    public List<Registration> getRegistrations() throws SQLException {
        List<Registration> registrations = new ArrayList<>();

        String query = """
    SELECT r.idRegistration, r.idAthlete, r.idTeam, r.idSport, r.idStatus,
           a.name AS athleteName, a.height AS athleteHeight, a.weight AS athleteWeight, a.dateOfBirth AS athleteDOB, a.idCountry AS athleteCountryId, a.idGender AS athleteGenderId,
           t.name AS teamName, t.idCountry AS teamCountryId, t.yearFounded AS teamYearFounded,
           s.type AS sportType, s.name AS sportName, s.description AS sportDescription, s.idGender AS sportGenderId,
           rs.description AS statusDescription
    FROM tblRegistration r
    LEFT JOIN tblAthlete a ON r.idAthlete = a.idAthlete
    LEFT JOIN tblTeam t ON r.idTeam = t.idTeam
    LEFT JOIN tblSport s ON r.idSport = s.idSport
    LEFT JOIN tblRegistrationStatus rs ON r.idStatus = rs.idStatus 
    WHERE r.idStatus = 1
    """;

        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query);

        if (rs != null) {
            while (rs.next()) {
                int idRegistration = rs.getInt("idRegistration");
                int idAthlete = rs.getInt("idAthlete");
                int idTeam = rs.getInt("idTeam");
                int idSport = rs.getInt("idSport");
                int idStatus = rs.getInt("idStatus");

                Athlete athlete = new Athlete(
                        idAthlete,
                        rs.getString("athleteName"),
                        rs.getInt("athleteHeight"),
                        rs.getFloat("athleteWeight"),
                        rs.getDate("athleteDOB"),
                        rs.getString("athleteCountryId"),
                        rs.getInt("athleteGenderId")
                );

                Team team = new Team(
                        idTeam,
                        rs.getString("teamName"),
                        new Country(rs.getString("teamCountryId"), null),
                        new Gender(rs.getInt("sportGenderId"), null),
                        rs.getInt("idSport"), // ID do esporte
                        rs.getInt("teamYearFounded")
                );

                Sport sport = new Sport(
                        idSport,
                        rs.getString("sportType"),
                        rs.getInt("sportGenderId"),
                        rs.getString("sportName"),
                        rs.getString("sportDescription"),
                        0,
                        null,
                        null,
                        null,
                        null,
                        null
                );

                RegistrationStatus status = new RegistrationStatus(
                        idStatus,
                        rs.getString("statusDescription")
                );

                Registration registration = new Registration(idRegistration, athlete, team, sport, status);
                registrations.add(registration);
            }
        } else {
            System.out.println("ResultSet is null. No results found.");
        }

        return registrations;
    }


    /**
     * Add registration
     * @param registration {Registration} Registration
     * @return {int} Generated ID
     * @throws SQLException
     */
    public int addRegistrationTeam(Registration registration) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM tblRegistration WHERE idAthlete = ? AND idTeam = ? AND idSport = ? AND idStatus = ? AND year = ?";
        String insertQuery = "INSERT INTO tblRegistration (idAthlete, idTeam, idSport, idStatus, year) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;
        ResultSet generatedKeys = null;


        try {
            conn = ConnectionsUtlis.dbConnect();

            checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, registration.getAthlete().getIdAthlete());
            checkStmt.setInt(2, registration.getTeam().getIdTeam());
            checkStmt.setInt(3, registration.getStatus().getIdStatus());
            checkStmt.setInt(4, registration.getSport().getIdSport());
            checkStmt.setInt(5, registration.getYear());

            rs = checkStmt.executeQuery();

            if (rs != null && rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    System.out.println("A inscrição já existe!");
                    return -1;
                }
            }

            insertStmt = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            int idAthlete = registration.getAthlete().getIdAthlete();
            int idTeam = registration.getTeam().getIdTeam();
            int idStatus = registration.getStatus().getIdStatus();
            int idSport = registration.getSport().getIdSport();
            int year = registration.getYear();

            System.out.println("Inserindo nova inscrição:");
            System.out.println("idAthlete: " + idAthlete);
            System.out.println("idTeam: " + idTeam);
            System.out.println("idSport: " + idSport);
            System.out.println("idStatus: " + idStatus);
            System.out.println("year: " + year);

            insertStmt.setInt(1, idAthlete);
            insertStmt.setInt(2, idTeam);
            insertStmt.setInt(3, idSport);
            insertStmt.setInt(4, idStatus);
            insertStmt.setInt(5, year);

            insertStmt.executeUpdate();
            System.out.println("Inscrição realizada com sucesso!");

            generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idGenerated = generatedKeys.getInt(1);
                System.out.println("Generated ID: " + idGenerated);
                return idGenerated;
            } else {
                throw new SQLException("Insertion failed, no ID obtained.");
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (checkStmt != null) {
                checkStmt.close();
            }
            if (insertStmt != null) {
                insertStmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }


    /**
     * Add registration
     * @param registration {Registration} Registration
     * @return {int} Generated ID
     * @throws SQLException
     */
    public static int addRegistrationSolo(Registration registration) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM tblRegistration WHERE idAthlete = ? AND idSport = ? AND idStatus = 3 AND year = ?";
        String insertQuery = "INSERT INTO tblRegistration (idAthlete, idSport, idStatus, year) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;
        ResultSet generatedKeys = null;

        try {
            conn = ConnectionsUtlis.dbConnect();

            checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, registration.getAthlete().getIdAthlete());
            checkStmt.setInt(2, registration.getSport().getIdSport());
            checkStmt.setInt(3, registration.getYear());

            rs = checkStmt.executeQuery();

            if (rs != null && rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    System.out.println("The registration already exists!");
                    return -1;
                }
            }

            insertStmt = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            int idAthlete = registration.getAthlete().getIdAthlete();
            int idSport = registration.getSport().getIdSport();
            int idStatus = registration.getStatus().getIdStatus();
            int year = registration.getYear();

            System.out.println("Inserting new registration:");
            System.out.println("idAthlete: " + idAthlete);
            System.out.println("idSport: " + idSport);
            System.out.println("idStatus: " + idStatus);
            System.out.println("year: " + year);

            insertStmt.setInt(1, idAthlete);
            insertStmt.setInt(2, idSport);
            insertStmt.setInt(3, idStatus);
            insertStmt.setInt(4, year);

            insertStmt.executeUpdate();
            System.out.println("Registration successfully added!");

            generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idGenerated = generatedKeys.getInt(1);
                System.out.println("Generated ID: " + idGenerated);
                return idGenerated;
            } else {
                throw new SQLException("Insertion failed, no ID obtained.");
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (checkStmt != null) {
                checkStmt.close();
            }
            if (insertStmt != null) {
                insertStmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }



    /**
     * Remove registration
     * @param idRegistration {int} Registration ID
     * @throws SQLException
     */
    public static void removeRegistration(int idRegistration) throws SQLException {
        String query = "DELETE FROM tblRegistration WHERE idRegistration = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idRegistration);
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
     * Update registration
     * @param registration {Registration} Registration
     * @throws SQLException
     */
    public static void updateRegistration(Registration registration) throws SQLException {
        String query = "UPDATE tblRegistration SET idAthlete = ?, idTeam = ?, idSport = ?, idStatus = ? WHERE idRegistration = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, registration.getAthlete().getIdAthlete());
            stmt.setInt(2, registration.getTeam().getIdTeam());
            stmt.setInt(3, registration.getSport().getIdSport());
            stmt.setInt(4, registration.getStatus().getIdStatus());
            stmt.setInt(5, registration.getIdRegistration());
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
     * Update registration status
     * @param registrationId {int} Registration ID
     * @param newStatus {int} New status
     * @param idTeam {int} Team ID
     * @throws SQLException
     */
    public static void updateRegistrationStatus(int registrationId, int newStatus, int idTeam) throws SQLException {
        String query = "UPDATE tblRegistration SET idStatus = ?, idTeam = ? WHERE idRegistration = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, newStatus);
            stmt.setInt(2, idTeam);
            stmt.setInt(3, registrationId);

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
     * Get registration by ID
     * @param idRegistration {int} Registration ID
     * @return {Registration} Registration
     * @throws SQLException
     */
    public static Registration getRegistrationById(int idRegistration) throws SQLException {
        String query = "SELECT * FROM tblRegistration WHERE idRegistration = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idRegistration);
        if (rs != null && rs.next()) {
            int idAthlete = rs.getInt("idAthlete");
            int idTeam = rs.getInt("idTeam");
            int idSport = rs.getInt("idSport");
            int idStatus = rs.getInt("idStatus");

            AthleteDao athleteDao = new AthleteDao();
            Athlete athlete = athleteDao.getAthleteById(idAthlete);
            Team team = TeamDao.getTeamById(idTeam);
            SportDao sportDao = new SportDao();
            Sport sport = sportDao.getSportById(idSport);
            RegistrationStatusDao registrationStatusDao = new RegistrationStatusDao();
            RegistrationStatus status = registrationStatusDao.getRegistrationStatusById(idStatus);
            return new Registration(idRegistration, athlete, team, sport, status);
        }
        return null;
    }

    /**
     * Get pending registrations
     * @return {List<Registration>} List of registrations
     * @throws SQLException
     */
    public static List<Registration> getPendingRegistrations() throws SQLException {
        List<Registration> registrations = new ArrayList<>();

        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblRegistration WHERE idStatus = 1;");

        if (rs != null) {
            while (rs.next()) {
                int idRegistration = rs.getInt("idRegistration");
                int idAthlete = rs.getInt("idAthlete");
                int idTeam = rs.getInt("idTeam");
                int idSport = rs.getInt("idSport");
                int idStatus = rs.getInt("idStatus");

                AthleteDao athleteDao = new AthleteDao();
                Athlete athlete = athleteDao.getAthleteById(idAthlete);
                Team team = TeamDao.getTeamById(idTeam);
                SportDao sportDao = new SportDao();
                Sport sport = sportDao.getSportById(idSport);
                RegistrationStatusDao registrationStatusDao = new RegistrationStatusDao();
                RegistrationStatus status = registrationStatusDao.getRegistrationStatusById(idStatus);

                Registration registration = new Registration(idRegistration, athlete, team, sport, status);
                registrations.add(registration);
            }
        } else {
            System.out.println("ResultSet is null. No results found.");
        }
        return registrations;
    }

    /**
     * Get years
     * @return {List<String>} List of years
     * @throws SQLException
     */
    public List<String> getYears() throws SQLException {
        List<String> years = new ArrayList<>();
        String query = "SELECT DISTINCT year " +
                "FROM tblRegistration " +
                "ORDER BY year DESC;";

        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query);
        if (rs != null) {
            while (rs.next()) {
                String year = rs.getString("year");
                years.add(year);
            }
        } else {
            System.out.println("No sports found with the specified name.");
        }
        return years;
    }

    /**
     * Verify if the team is already registered
     * @param idCountry {String} Country ID
     * @param idSport {int} Sport ID
     * @return boolean
     * @throws SQLException
     */
    public boolean verfiyTeam(String idCountry, int idSport) throws SQLException{
        String query = "SELECT idTeam FROM tblTeam WHERE idCountry = ? AND idSport = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idCountry, idSport);
        try {
            if (rs != null && rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get team ID
     * @param idCountry {String} Country ID
     * @param idSport {int} Sport ID
     * @return int
     * @throws SQLException
     */
    public int getIdTeam(String idCountry, int idSport) throws SQLException{
        String query = "SELECT idTeam FROM tblTeam WHERE idCountry = ? AND idSport = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idCountry, idSport);
        try {
            if (rs != null && rs.next()) {
                return rs.getInt("idTeam");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get user registration
     * @param idAthlete {int} Athlete ID
     * @return List<List>
     * @throws SQLException
     */
    public List<List> getUserRegistration(int idAthlete) throws SQLException{
        List<List> userRegistrations = new ArrayList<>();
        String query = "SELECT r.*, s.idSport, s.name, s.type" +
                " FROM tblRegistration r" +
                " JOIN tblSport s ON r.idSport = s.idSport" +
                " WHERE r.idAthlete = ? AND r.idStatus = 3;";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idAthlete);
        if (rs != null) {
            while (rs.next()) {
                List<String> registration = new ArrayList<>();
                registration.add(rs.getString("idRegistration"));
                registration.add(rs.getString("idAthlete"));
                registration.add(rs.getString("idTeam"));
                registration.add(rs.getString("idSport"));
                registration.add(rs.getString("idStatus"));
                registration.add(rs.getString("year"));
                registration.add(rs.getString("name"));
                registration.add(rs.getString("type"));
                userRegistrations.add(registration);
            }
        } else {
            System.out.println("No sports found with the specified name.");
        }
        return userRegistrations;
    }

    /**
     * Get registered athletes
     * @param idSport {int} Sport ID
     * @param year {int} Year
     * @return List<Integer>
     * @throws SQLException
     */
    public List<Integer> getRegisteredAthletes(int idSport, int year) throws SQLException {
        List<Integer> athletes = new ArrayList<>();
        String query = "SELECT idAthlete " +
                "FROM tblRegistration " +
                "WHERE idStatus = 3 AND idSport = ? AND year = ?;";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport, year);
        if (rs != null) {
            while (rs.next()) {
                int IdAthlete = rs.getInt("idAthlete");
                athletes.add(IdAthlete);
            }
        }
        return athletes;
    }

    /**
     * Get registered teams
     * @param idSport {int} Sport ID
     * @param year {int} Year
     * @return List<Integer>
     * @throws SQLException
     */
    public List<Integer> getRegisteredTeams(int idSport, int year) throws SQLException {
        List<Integer> teams = new ArrayList<>();
        String query = "SELECT DISTINCT idTeam " +
                "FROM tblRegistration " +
                "WHERE idStatus = 3 AND idSport = ? AND year = ? ";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport, year);
        if (rs != null) {
            while (rs.next()) {
                int IdTeam = rs.getInt("idTeam");
                teams.add(IdTeam);
            }
        }
        return teams;
    }

    /**
     * Get athletes by team
     * @param idTeam {int} Team ID
     * @param idSport {int} Sport ID
     * @param year {int} Year
     * @return List<Integer>
     * @throws SQLException
     */
    public List<Integer> getAthletesByTeam(int idTeam, int idSport, int year) throws SQLException{
        List<Integer> athletes = new ArrayList<>();
        String query = "SELECT idAthlete " +
                "FROM tblRegistration " +
                "WHERE idAthlete IS NOT NULL AND idTeam = ? AND idSport = ? AND idStatus = 3 AND year = ?;";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idTeam, idSport, year);
        if (rs != null) {
            while (rs.next()) {
                int athlete = rs.getInt("idAthlete");
                athletes.add(athlete);
            }
        }
        return athletes;
    }

    /**
     * Get status finished
     * @param idSport {int} Sport ID
     * @param year {int} Year
     * @return boolean
     * @throws SQLException
     */
    public boolean setStatusFinished(int idSport, int year) throws SQLException{
        String query = "UPDATE tblRegistration SET idStatus = 4 WHERE idSport = ? AND year = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSport);
            stmt.setInt(2, year);
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

    /**
     * Set status rejected
     * @param idSport {int} Sport ID
     * @param year {int} Year
     * @return boolean
     * @throws SQLException
     */
    public boolean setStatusRejected(int idSport, int year) throws SQLException{
        String query = "UPDATE tblRegistration SET idStatus = 2 WHERE idSport = ? AND year = ? AND idStatus = 1";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSport);
            stmt.setInt(2, year);
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

    public static List<Registration> getRegistrationByIdAthlete(int idAthlete) throws SQLException {
        String query = "SELECT * FROM tblRegistration WHERE idAthlete = ?";
        List<Registration> registrations = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idAthlete);
        if (rs != null && rs.next()) {
            int idRegistration = rs.getInt("idRegistration");
            Athlete athlete = new AthleteDao().getAthleteById(rs.getInt("idAthlete"));
            Team team = TeamDao.getTeamById(rs.getInt("idTeam"));
            Sport sport = new SportDao().getSportById(rs.getInt("idSport"));
            RegistrationStatus regStatus = new RegistrationStatusDao().getRegistrationStatusById(rs.getInt("idStatus"));
            int year = rs.getInt("year");
            Registration reg = new Registration(idRegistration, athlete, team, sport, regStatus, year);
            registrations.add(reg);

        }
        return registrations;
    }
}