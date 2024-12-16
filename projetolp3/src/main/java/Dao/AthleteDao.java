package Dao;

import Models.*;
import Utils.ConnectionsUtlis;
import Models.Athlete;
import Utils.PasswordUtils;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import java.sql.ResultSet;


public class AthleteDao {

    /**
     * Get all athletes
     * @return List<Athlete>
     * @throws SQLException
     */
    public List<Athlete> getAthletes() throws SQLException {
        List<Athlete> athletes = new ArrayList<>();
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery("SELECT * FROM tblAthlete;");
        if (rs != null) {
            while (rs.next()) {
                int idAthlete = rs.getInt("idAthlete");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String idCountry = rs.getString("idCountry");
                CountryDao countryDao = new CountryDao();
                Country country = countryDao.getCountryById(idCountry);
                int idGender = rs.getInt("idGender");
                GenderDao genderDao = new GenderDao();
                Gender gender = genderDao.getGenderById(idGender);
                int height = rs.getInt("height");
                float weight = rs.getFloat("weight");
                java.sql.Date dateOfBirth = rs.getDate("dateOfBirth");
                String image = rs.getString("image");

                Athlete athlete = new Athlete(idAthlete, password, name, country, gender, height, weight, dateOfBirth, image);
                athletes.add(athlete);
            }
        } else {
            System.out.println("ResultSet is null. No results for Athlete found.");
        }
        return athletes;
    }
    public List<List> getAthletesToShow() throws SQLException{
        List<List> athletes = new ArrayList<>();
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery("SELECT a.idAthlete ,a.name, a.height, a.weight,a.dateOfBirth, a.image, c.name as countryName, g.description as gender " +
                "FROM tblAthlete as a " +
                "INNER JOIN tblCountry as c ON a.idCountry = c.idCountry " +
                "INNER JOIN tblGender as g ON a.idGender = g.idGender");
        if (rs != null) {
            while (rs.next()) {
                List<String> athlete = new ArrayList<>();
                athlete.add(rs.getString("idAthlete"));
                athlete.add(rs.getString("name"));
                athlete.add(rs.getString("countryName"));
                athlete.add(rs.getString("gender"));
                athlete.add(rs.getString("height"));
                athlete.add(rs.getString("weight"));
                athlete.add(rs.getString("dateOfBirth"));
                athlete.add(rs.getString("image"));
                athletes.add(athlete);
            }
        } else {
            System.out.println("ResultSet is null. No results for Athlete found.");
        }
        return athletes;
    }

    /**
     * Add athlete
     * @param athlete {Athlete} Athlete
     * @return int
     * @throws SQLException
     */
    public static int addAthlete(Athlete athlete) throws SQLException {
        String query = "INSERT INTO tblAthlete (password, name, idCountry, idGender, height, weight, dateOfBirth, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        PasswordUtils passwordUtils = new PasswordUtils();
        String password = passwordUtils.encriptarPassword(athlete.getPassword());
        ResultSet rs = null;
        int generatedId = -1;


        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, password);
            stmt.setString(2, athlete.getName());
            stmt.setString(3, athlete.getCountry().getIdCountry());
            stmt.setInt(4, athlete.getGenre().getIdGender());
            stmt.setInt(5, athlete.getHeight());
            stmt.setFloat(6, athlete.getWeight());
            stmt.setDate(7, athlete.getDateOfBirth());
            stmt.setString(8, athlete.getImage());

            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
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

        return generatedId;
    }

    /**
     * Remove athlete
     * @param idAthlete {int} ID
     * @throws SQLException
     */
    public static void removeAthlete(int idAthlete) throws SQLException {
        String query = "DELETE FROM tblAthlete WHERE idAthlete = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
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

    /**
     * Update athlete
     * @param athlete {Athlete} Athlete
     * @throws SQLException
     */
    public static void updateAthlete(Athlete athlete) throws SQLException {
        String query = "UPDATE tblAthlete SET password = ?, name = ?, idCountry = ?, idGender = ?, height = ?, weight = ?, dateOfBirth = ?, image = ? WHERE idAthlete = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, athlete.getPassword());
            stmt.setString(2, athlete.getName());
            stmt.setString(3, athlete.getCountry().getIdCountry());
            stmt.setInt(4, athlete.getGenre().getIdGender());
            stmt.setInt(5, athlete.getHeight());
            stmt.setFloat(6, athlete.getWeight());
            stmt.setDate(7, athlete.getDateOfBirth());
            stmt.setString(8, athlete.getImage());
            stmt.setInt(9, athlete.getIdAthlete());
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
     * Get athlete by ID
     * @param idAthlete {int} ID
     * @return Athlete
     * @throws SQLException
     */
    public Athlete getAthleteById(int idAthlete) throws SQLException {
        String query = "SELECT * FROM tblAthlete WHERE idAthlete = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idAthlete);
        if (rs != null && rs.next()) {
            String password = rs.getString("password");
            String name = rs.getString("name");
            String idCountry = rs.getString("idCountry");
            CountryDao countryDao = new CountryDao();
            Country country = countryDao.getCountryById(idCountry);
            int idGender = rs.getInt("idGender");
            GenderDao genderDao = new GenderDao();
            Gender gender = genderDao.getGenderById(idGender);
            int height = rs.getInt("height");
            float weight = rs.getFloat("weight");
            java.sql.Date dateOfBirth = rs.getDate("dateOfBirth");
            String image = rs.getString("image");
            return new Athlete(idAthlete, password, name, country, gender, height, weight, dateOfBirth, image);
        }
        return null;
    }

    /**
     * Get athlete by ID (Minimalist)
     * @param idAthlete {int} ID
     * @return Athlete
     * @throws SQLException
     */
    public static Athlete getAthleteByIdMinimum(int idAthlete) throws SQLException {
        String query = "SELECT name, height, weight, dateOfBirth FROM tblAthlete WHERE idAthlete = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idAthlete);
        if (rs != null && rs.next()) {
            String name = rs.getString("name");
            int height = rs.getInt("height");
            float weight = rs.getFloat("weight");
            java.sql.Date dateOfBirth = rs.getDate("dateOfBirth");
            return new Athlete(idAthlete, name, height, weight, dateOfBirth);
        }
        return null;
    }

    /**
     * Update athlete password
     * @param idAthlete {int} ID
     * @param password {String} Password
     * @throws SQLException
     */
    public static void updateAthletePassword(int idAthlete, String password) throws SQLException {
        String query = "UPDATE tblAthlete SET password = ? WHERE idAthlete = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, password);
            stmt.setInt(2, idAthlete);

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
     * Get athlete name by ID
     * @param idAthlete {int} ID
     * @return String
     * @throws SQLException
     */
    public String getAthlheteNameByID(int idAthlete) throws SQLException {
        String query = "SELECT name FROM tblAthlete WHERE idAthlete = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idAthlete);
        if (rs != null && rs.next()) {
            return rs.getString("name");
        }
        return null;
    }

    /**
     * Update athlete image
     * @param idAthlete {int} ID
     * @param path {String} Path
     * @param extensao {String} Extension
     * @throws SQLException
     */
    public void updateAthleteImage(int idAthlete, String path, String extensao) throws SQLException {
        String query = "UPDATE tblAthlete SET image = ? WHERE idAthlete = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, path + idAthlete + extensao);
            stmt.setInt(2, idAthlete);
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
    public void updateHeightWeight(int idAthlete, int height, float weight) throws SQLException {
        String query = "UPDATE tblAthlete SET height = ?, weight = ? WHERE idAthlete = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, height);
            stmt.setFloat(2, weight);
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

    public List<List> getPartToShow(int idTeam) throws SQLException {
        List<List> athletes = new ArrayList<>();
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery("SELECT a.name, a.height, a.weight, a.dateOfBirth, a.image " +
                "FROM tblAthlete as a " +
                "INNER JOIN tblRegistration as r ON a.idAthlete = r.idAthlete " +
                "WHERE r.idTeam = ?", idTeam);
        if (rs != null) {
            while (rs.next()) {
                List<String> athlete = new ArrayList<>();
                athlete.add(rs.getString("name"));
                athlete.add(rs.getString("height"));
                athlete.add(rs.getString("weight"));
                athlete.add(rs.getString("dateOfBirth"));
                athlete.add(rs.getString("image"));
                athletes.add(athlete);
            }
        } else {
            System.out.println("ResultSet is null. No results for Athlete found.");
        }
        return athletes;
    }

    public String getImageAthlete(int idAthlete) throws SQLException{
        String query = "SELECT image FROM tblAthlete WHERE idAthlete = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idAthlete);
        if (rs != null && rs.next()) {
            return rs.getString("image");
        }
        return null;
    }
}
