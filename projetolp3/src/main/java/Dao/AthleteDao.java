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

    public List<Athlete> getAthletes() throws SQLException {
        List<Athlete> athletes = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblAthlete;");
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
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT a.idAthlete ,a.name, a.height, a.weight,a.dateOfBirth, a.image, c.name as countryName, g.description as gender " +
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

    public static int addAthlete(Athlete athlete) throws SQLException {
        String query = "INSERT INTO tblAthlete (password, name, idCountry, idGender, height, weight, dateOfBirth, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        PasswordUtils passwordUtils = new PasswordUtils();
        String password = passwordUtils.encriptarPassword(athlete.getPassword());
        ResultSet rs = null;
        int generatedId = -1;


        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); // Importante: Use Statement.RETURN_GENERATED_KEYS

            // Definir os parâmetros do statement
            stmt.setString(1, password);
            stmt.setString(2, athlete.getName());
            stmt.setString(3, athlete.getCountry().getIdCountry());
            stmt.setInt(4, athlete.getGenre().getIdGender());
            stmt.setInt(5, athlete.getHeight());
            stmt.setFloat(6, athlete.getWeight());
            stmt.setDate(7, athlete.getDateOfBirth());
            stmt.setString(8, athlete.getImage());

            // Executa o insert
            stmt.executeUpdate();

            // Recuperar o id gerado
            rs = stmt.getGeneratedKeys(); // Obtém as chaves geradas
            if (rs.next()) {
                generatedId = rs.getInt(1); // O id gerado estará na primeira coluna
            }
        } finally {
            // Fechar recursos
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

        return generatedId; // Retorna o id gerado automaticamente
    }

    public static int addAthleteJunit(Athlete athlete) throws SQLException {
        String query = "INSERT INTO tblAthlete (password, name, idCountry, idGender, height, weight, dateOfBirth, image) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        PasswordUtils passwordUtils = new PasswordUtils();
        String password = passwordUtils.encriptarPassword(athlete.getPassword());
        ResultSet rs = null;
        int generatedId = -1;


        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); // Importante: Use Statement.RETURN_GENERATED_KEYS

            // Definir os parâmetros do statement
            stmt.setString(1, password);
            stmt.setString(2, athlete.getName());
            stmt.setString(3, athlete.getCountry().getIdCountry());
            stmt.setInt(4, athlete.getGenre().getIdGender());
            stmt.setInt(5, athlete.getHeight());
            stmt.setFloat(6, athlete.getWeight());
            stmt.setDate(7, athlete.getDateOfBirth());
            stmt.setString(8, athlete.getImage());

            // Executa o insert
            stmt.executeUpdate();

            // Recuperar o id gerado
            rs = stmt.getGeneratedKeys(); // Obtém as chaves geradas
            if (rs.next()) {
                generatedId = rs.getInt(1); // O id gerado estará na primeira coluna
            }
        } finally {
            // Fechar recursos
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

        return generatedId; // Retorna o id gerado automaticamente
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
            stmt.setString(3, athlete.getCountry().getIdCountry());
            stmt.setInt(4, athlete.getGenre().getIdGender());
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

    public static void updateAthleteJunit(Athlete athlete) throws SQLException {
        String query = "UPDATE tblAthlete SET password = ?, name = ?, idCountry = ?, idGender = ?, height = ?, weight = ?, dateOfBirth = ?, image = ? WHERE idAthlete = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
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

    public Athlete getAthleteById(int idAthlete) throws SQLException {
        String query = "SELECT * FROM tblAthlete WHERE idAthlete = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idAthlete);
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
    }   public static Athlete getAthleteByIdMinimum(int idAthlete) throws SQLException {
        String query = "SELECT name, height, weight, dateOfBirth FROM tblAthlete WHERE idAthlete = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idAthlete);
        if (rs != null && rs.next()) {
            String name = rs.getString("name");
            int height = rs.getInt("height");
            float weight = rs.getFloat("weight");
            java.sql.Date dateOfBirth = rs.getDate("dateOfBirth");
            return new Athlete(idAthlete, name, height, weight, dateOfBirth);
        }
        return null;
    }

    public static void updateAthletePassword(int idAthlete, String password) throws SQLException {
        String query = "UPDATE tblAthlete SET password = ? WHERE idAthlete = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            // Define a senha do atleta
            stmt.setString(1, password); // Senha a ser atualizada
            stmt.setInt(2, idAthlete);   // ID do atleta para identificar a linha

            // Executa a atualização
            stmt.executeUpdate();
        } finally {
            // Fechar recursos
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
    public String getAthlheteNameByID(int idAthlete) throws SQLException {
        String query = "SELECT name FROM tblAthlete WHERE idAthlete = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idAthlete);
        if (rs != null && rs.next()) {
            return rs.getString("name");
        }
        return null;
    }

    public void updateAthleteImage(int idAthlete, String path, String extensao) throws SQLException {
        String query = "UPDATE tblAthlete SET image = ? WHERE idAthlete = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionsUtlis.dbConnect();
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
            conn = ConnectionsUtlis.dbConnect();
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
}
