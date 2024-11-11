package Dao;

import Models.*;
import Utils.ConnectionsUtlis;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDao {
    public static List<Registration> getRegistrations() throws SQLException {
        List<Registration> registrations = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblRegistration;");
        if (rs != null) {
            while (rs.next()) {
                int idRegistration = rs.getInt("idRegistration");
                int idAthlete = rs.getInt("idAthlete");
                int idTeam = rs.getInt("idTeam");
                int idSport = rs.getInt("idSport");
                int idStatus = rs.getInt("idStatus");
                Athlete athlete = AthleteDao.getAthleteById(idAthlete);
                Team team = TeamDao.getTeamById(idTeam);
                Sport sport = SportDao.getSportById(idSport);
                RegistrationStatus status = RegistrationStatusDao.getRegistrationStatusById(idStatus);
                Registration registration = new Registration(idRegistration, athlete, team, sport, status);
                registrations.add(registration);
            }
        } else {
            System.out.println("ResultSet is null. No results found.");
        }
        return registrations;
    }

    public static void addRegistration(Registration registration) throws SQLException {
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

            Athlete athlete = AthleteDao.getAthleteById(idAthlete);
            Team team = TeamDao.getTeamById(idTeam);
            Sport sport = SportDao.getSportById(idSport);
            RegistrationStatus status = RegistrationStatusDao.getRegistrationStatusById(idStatus);
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

                Athlete athlete = AthleteDao.getAthleteById(idAthlete);
                Team team = TeamDao.getTeamById(idTeam);
                Sport sport = SportDao.getSportById(idSport);
                RegistrationStatus status = RegistrationStatusDao.getRegistrationStatusById(idStatus);

                Registration registration = new Registration(idRegistration, athlete, team, sport, status);
                registrations.add(registration);
            }
        } else {
            System.out.println("ResultSet is null. No results found.");
        }
        return registrations;
    }

}
