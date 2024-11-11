package Dao;

import Models.*;
import Utils.ConnectionsUtlis;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import java.sql.ResultSet;


public class AthleteDao {

    public static List<Athlete> getAthletes() throws SQLException {
        List<Athlete> athletes = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblAthlete;");
        if (rs != null) {
            while (rs.next()) {
                int idAthlete = rs.getInt("idAthlete");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String idCountry = rs.getString("idCountry");
                Country country = CountryDao.getCountryById(idCountry);
                int idGender = rs.getInt("idGender");
                Gender gender = GenderDao.getGenderById(idGender);
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

    public static int addAthlete(Athlete athlete) throws SQLException {
        String query = "INSERT INTO tblAthlete (password, name, idCountry, idGender, height, weight, dateOfBirth) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null; // Para armazenar o id gerado
        int generatedId = -1; // Valor padrão caso não consiga pegar o id

        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); // Importante: Use Statement.RETURN_GENERATED_KEYS

            // Definir os parâmetros do statement
            stmt.setString(1, athlete.getPassword());
            stmt.setString(2, athlete.getName());
            stmt.setString(3, athlete.getCountry().getIdCountry());
            stmt.setInt(4, athlete.getGenre().getIdGender());
            stmt.setInt(5, athlete.getHeight());
            stmt.setFloat(6, athlete.getWeight());
            stmt.setDate(7, athlete.getDateOfBirth());

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

    public static Athlete getAthleteById(int idAthlete) throws SQLException {
        String query = "SELECT * FROM tblAthlete WHERE idAthlete = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idAthlete);
        if (rs != null && rs.next()) {
            String password = rs.getString("password");
            String name = rs.getString("name");
            String idCountry = rs.getString("idCountry");
            Country country = CountryDao.getCountryById(idCountry);
            int idGender = rs.getInt("idGender");
            Gender gender = GenderDao.getGenderById(idGender);
            int height = rs.getInt("height");
            float weight = rs.getFloat("weight");
            java.sql.Date dateOfBirth = rs.getDate("dateOfBirth");
            return new Athlete(idAthlete, password, name, country, gender, height, weight, dateOfBirth);
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


}
