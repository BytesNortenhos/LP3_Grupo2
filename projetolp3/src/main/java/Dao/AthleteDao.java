package Dao;

import Models.Country;
import Models.Gender;
import Utils.ConnectionsUtlis;
import Models.Athlete;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AthleteDao {

    public static List<Athlete> getAthletes() throws SQLException {
        List<Athlete> athletes = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(
                "SELECT a.idAthlete, a.password, a.name, c.idCountry, c.name AS countryName, c.continent, " +
                        "g.idGender, g.description AS genderDescription, a.height, a.weight, a.dateOfBirth " +
                        "FROM tblAthlete a " +
                        "INNER JOIN tblCountry c ON a.idCountry = c.idCountry " +
                        "INNER JOIN tblGender g ON a.idGender = g.idGender;");
        if (rs != null) {
            while (rs.next()) {
                int idAthlete = rs.getInt("idAthlete");
                String password = rs.getString("password");
                String name = rs.getString("name");
                Country country = new Country(rs.getInt("idCountry"), rs.getString("countryName"), rs.getString("continent"));
                Gender gender = new Gender(rs.getInt("idGender"), rs.getString("genderDescription"));
                int height = rs.getInt("height");
                float weight = rs.getFloat("weight");
                java.sql.Date dateOfBirth = rs.getDate("dateOfBirth");
                Athlete athlete = new Athlete(idAthlete, password, name, country, gender, height, weight, dateOfBirth);
                athletes.add(athlete);
            }
        } else {
            System.out.println("ResultSet is null. No results for Athlete found.");
        }
        return athletes;
    }

    public static void addAthlete(Athlete athlete) throws SQLException {
        String query = "INSERT INTO tblAthlete (password, name, idCountry, idGender, height, weight, dateOfBirth) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, athlete.getPassword());
            stmt.setString(2, athlete.getName());
            stmt.setInt(3, athlete.getCountry().getIdCountry());
            stmt.setInt(4, athlete.getGender().getIdGender());
            stmt.setInt(5, athlete.getHeight());
            stmt.setFloat(6, athlete.getWeight());
            stmt.setDate(7, athlete.getDateOfBirth());
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

    public static void removeAthlete(int idAthlete) throws SQLException {
        String query = "DELETE FROM tblAthlete WHERE idAthlete = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idAthlete);
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

    public static void updateAthlete(Athlete athlete) throws SQLException {
        String query = "UPDATE tblAthlete SET password = ?, name = ?, idCountry = ?, idGender = ?, height = ?, weight = ?, dateOfBirth = ? WHERE idAthlete = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, athlete.getPassword());
            stmt.setString(2, athlete.getName());
            stmt.setInt(3, athlete.getCountry().getIdCountry());
            stmt.setInt(4, athlete.getGender().getIdGender());
            stmt.setInt(5, athlete.getHeight());
            stmt.setFloat(6, athlete.getWeight());
            stmt.setDate(7, athlete.getDateOfBirth());
            stmt.setInt(8, athlete.getIdAthlete());
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

    public static Athlete getAthleteById(int idAthlete) throws SQLException {
        String query = "SELECT a.idAthlete, a.password, a.name, " +
                "c.idCountry, c.name AS countryName, c.continent, " +
                "g.idGender, g.description AS genderDescription, " +
                "a.height, a.weight, a.dateOfBirth " +
                "FROM tblAthlete a " +
                "INNER JOIN tblCountry c ON a.idCountry = c.idCountry " +
                "INNER JOIN tblGender g ON a.idGender = g.idGender " +
                "WHERE a.idAthlete = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idAthlete);
        if (rs != null && rs.next()) {
            String password = rs.getString("password");
            String name = rs.getString("name");
            Country country = new Country(rs.getInt("idCountry"), rs.getString("countryName"), rs.getString("continent"));
            Gender gender = new Gender(rs.getInt("idGender"), rs.getString("genderDescription"));
            int height = rs.getInt("height");
            float weight = rs.getFloat("weight");
            java.sql.Date dateOfBirth = rs.getDate("dateOfBirth");
            return new Athlete(idAthlete, password, name, country, gender, height, weight, dateOfBirth);
        }
        return null;
    }
}
