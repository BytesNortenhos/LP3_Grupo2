package Dao;

import Models.*;
import Utils.ConnectionsUtlis;
import java.util.Map;
import java.util.HashMap;

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
           a.name AS athleteName, a.height AS athleteHeight, a.weight AS athleteWeight, a.dateOfBirth AS athleteDOB,
           t.name AS teamName, t.idCountry AS teamCountryId, t.yearFounded AS teamYearFounded,
           s.type AS sportType, s.name AS sportName, s.description AS sportDescription, s.idGender AS sportGenderId,
           rs.description AS statusDescription
    FROM tblRegistration r
    LEFT JOIN tblAthlete a ON r.idAthlete = a.idAthlete
    LEFT JOIN tblTeam t ON r.idTeam = t.idTeam
    LEFT JOIN tblSport s ON r.idSport = s.idSport
    LEFT JOIN tblRegistrationStatus rs ON r.idStatus = rs.idStatus
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
                        rs.getDate("athleteDOB")
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
        String query = "INSERT INTO tblRegistration (idAthlete, idTeam, idSport, idStatus) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, registration.getAthlete().getIdAthlete());
            stmt.setInt(2, registration.getTeam().getIdTeam());
            stmt.setInt(3, registration.getSport().getIdSport());
            stmt.setInt(4, registration.getStatus().getIdStatus());
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } }
        public static void addRegistrationSolo(Registration registration) throws SQLException {
            String query = "INSERT INTO tblRegistration (idAthlete, idSport, idStatus) VALUES (?, ?, ?)";
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                conn = ConnectionsUtlis.dbConnect();
                stmt = conn.prepareStatement(query);

                stmt.setInt(1, registration.getAthlete().getIdAthlete());
                stmt.setInt(2, registration.getSport().getIdSport());
                stmt.setInt(3, registration.getStatus().getIdStatus());
                stmt.executeUpdate();
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }

            } }

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
    public static void updateRegistrationStatus(int registrationId, int newStatus) throws SQLException {
        String query = "UPDATE tblRegistration SET idStatus = ? WHERE idRegistration = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, newStatus);  // novo status (2 para rejeitar, 3 para aceitar)
            stmt.setInt(2, registrationId); // id da inscrição

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

    public int getNumberParticipantsSport(int idSport) throws SQLException {
        String query = "SELECT COUNT(*) AS quantidade " +
                "FROM tblRegistration " +
                "WHERE idSport = ?;";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        int quantidade = 0;
        if (rs != null && rs.next()) {
            quantidade = rs.getInt("quantidade");
        }
        return quantidade;
    }
}