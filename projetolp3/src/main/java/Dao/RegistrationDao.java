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

                // Cria o objeto Registration com as novas entidades carregadas
                Registration registration = new Registration(idRegistration, athlete, team, sport, status);
                registrations.add(registration);
            }
        } else {
            System.out.println("ResultSet is null. No results found.");
        }

        return registrations;
    }


    public static void addRegistrationTeam(Registration registration) throws SQLException {
        // Query para verificar se a inscrição já existe
        String checkQuery = "SELECT COUNT(*) FROM tblRegistration WHERE idAthlete = ? AND idTeam = ? AND idStatus = ? AND year = ?";
        String insertQuery = "INSERT INTO tblRegistration (idAthlete, idTeam, idStatus, year) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionsUtlis.dbConnect();

            // Preparar a consulta para verificar se a inscrição já existe
            checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, registration.getAthlete().getIdAthlete());
            checkStmt.setInt(2, registration.getTeam().getIdTeam());
            checkStmt.setInt(3, registration.getStatus().getIdStatus());
            checkStmt.setInt(4, registration.getYear());

            // Executa a consulta para verificar se a inscrição já existe
            rs = checkStmt.executeQuery();

            if (rs != null && rs.next()) {
                int count = rs.getInt(1);  // Obtém a contagem de registros existentes
                if (count > 0) {
                    System.out.println("A inscrição já existe!");  // Exibe mensagem se já existir
                    return;  // Sai do método para evitar inserir o registro duplicado
                }
            }

            // Se não houver registro existente, procede com a inserção
            insertStmt = conn.prepareStatement(insertQuery);

            int idAthlete = registration.getAthlete().getIdAthlete();
            int idTeam = registration.getTeam().getIdTeam();
            int idStatus = registration.getStatus().getIdStatus();
            int year = registration.getYear();

            // Debugging: Imprime os valores antes de inserir
            System.out.println("Inserindo nova inscrição:");
            System.out.println("idAthlete: " + idAthlete);
            System.out.println("idTeam: " + idTeam);
            System.out.println("idStatus: " + idStatus);
            System.out.println("year: " + year);

            // Set parameters correctly for the insert statement
            insertStmt.setInt(1, idAthlete);  // Corresponds to the first placeholder
            insertStmt.setInt(2, idTeam);     // Corresponds to the second placeholder
            insertStmt.setInt(3, idStatus);   // Corresponds to the third placeholder
            insertStmt.setInt(4, year);       // Corresponds to the fourth placeholder

            insertStmt.executeUpdate();
            System.out.println("Inscrição realizada com sucesso!");  // Mensagem de sucesso após a inserção

        } finally {
            // Fechando os recursos
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
            // Prepare the check statement to check if the record already exists
            checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, registration.getAthlete().getIdAthlete());
            checkStmt.setInt(2, registration.getSport().getIdSport());
            checkStmt.setInt(3, registration.getYear());

            // Execute the check query to see if the record already exists
            rs = checkStmt.executeQuery();

            if (rs != null && rs.next()) {
                int count = rs.getInt(1);  // Get the count of existing records
                if (count > 0) {
                    System.out.println("The registration already exists!"); // Show message if already exists
                    return -1;  // Exit the method to avoid inserting the duplicate
                }
            }

            // If no existing registration, proceed with the insertion
            insertStmt = conn.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            int idAthlete = registration.getAthlete().getIdAthlete();
            int idSport = registration.getSport().getIdSport();
            int idStatus = registration.getStatus().getIdStatus();
            int year = registration.getYear();

            // Debugging: Print the values before insertion
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
            System.out.println("Registration successfully added!"); // Show success message only if inserted

            generatedKeys = insertStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idGenerated = generatedKeys.getInt(1);
                System.out.println("Generated ID: " + idGenerated); // Debugging: Print the generated ID
                return idGenerated; // Return the generated ID
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

            stmt.executeUpdate(); // Executa a atualização
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

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
    public static List<Registration> getPendingRegistrations() throws SQLException {
        List<Registration> registrations = new ArrayList<>();
        // Query modificada para buscar apenas registros com idStatus = 1
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

    public List<Integer> getRegisteredTeams(int idSport, int year) throws SQLException {
        List<Integer> teams = new ArrayList<>();
        String query = "SELECT idTeam " +
                "FROM tblRegistration " +
                "WHERE idStatus = 3 AND idSport = ? AND year = ?;";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport, year);
        if (rs != null) {
            while (rs.next()) {
                int IdTeam = rs.getInt("idTeam");
                teams.add(IdTeam);
            }
        }
        return teams;
    }

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
}