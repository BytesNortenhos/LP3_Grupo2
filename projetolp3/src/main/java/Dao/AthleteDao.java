package Dao;

import Models.*;
import Utils.ConnectionsUtlis;

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
                        "g.idGender, g.description AS genderDescription, a.height, a.weight, a.dateOfBirth, " +
                        "p.year, p.gold, p.silver, p.bronze, p.certificate " +
                        "FROM tblAthlete a " +
                        "INNER JOIN tblCountry c ON a.idCountry = c.idCountry " +
                        "INNER JOIN tblGender g ON a.idGender = g.idGender " +
                        "LEFT JOIN ( " +
                        "   SELECT m.idAthlete, e.year, " +
                        "       SUM(CASE WHEN m.idMedalType = 1 THEN 1 ELSE 0 END) AS gold, " +
                        "       SUM(CASE WHEN m.idMedalType = 2 THEN 1 ELSE 0 END) AS silver, " +
                        "       SUM(CASE WHEN m.idMedalType = 3 THEN 1 ELSE 0 END) AS bronze, " +
                        "       SUM(CASE WHEN m.idMedalType IS NULL THEN 1 ELSE 0 END) AS certificate " +
                        "   FROM tblMedal m " +
                        "   LEFT JOIN tblEvent e ON m.year = e.year " +
                        "   GROUP BY m.idAthlete, e.year " +
                        ") AS p ON a.idAthlete = p.idAthlete " +
                        "ORDER BY a.idAthlete, p.year;");

        if (rs != null) {
            int previousId = -1; // Para rastrear mudanças no idAthlete
            Athlete currentAthlete = null;
            List<Participation> participations = null;

            while (rs.next()) {
                int idAthlete = rs.getInt("idAthlete");

                // Verifica se estamos em um novo atleta
                if (idAthlete != previousId) {
                    // Adiciona o atleta anterior com suas participações completas à lista, se houver
                    if (currentAthlete != null) {
                        currentAthlete.setOlympicParticipations(participations);
                        athletes.add(currentAthlete);
                    }

                    // Cria um novo atleta e uma nova lista de participações
                    String password = rs.getString("password");
                    String name = rs.getString("name");
                    Country country = new Country(rs.getInt("idCountry"), rs.getString("countryName"), rs.getString("continent"));
                    Gender gender = new Gender(rs.getInt("idGender"), rs.getString("genderDescription"));
                    int height = rs.getInt("height");
                    float weight = rs.getFloat("weight");
                    java.sql.Date dateOfBirth = rs.getDate("dateOfBirth");

                    participations = new ArrayList<>(); // Inicializa uma nova lista de participações
                    currentAthlete = new Athlete(idAthlete, password, name, country, gender, height, weight, dateOfBirth, participations);
                    previousId = idAthlete;
                }

                // Adiciona a participação ao atleta atual
                int year = rs.getInt("year");
                if (!rs.wasNull()) {
                    int gold = rs.getInt("gold");
                    int silver = rs.getInt("silver");
                    int bronze = rs.getInt("bronze");
                    int certificate = rs.getInt("certificate");
                    participations.add(new Participation(year, gold, silver, bronze, certificate));
                }
            }

            // Adiciona o último atleta processado com suas participações completas à lista
            if (currentAthlete != null) {
                currentAthlete.setOlympicParticipations(participations);
                athletes.add(currentAthlete);
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
            stmt.setInt(4, athlete.getGenre().getIdGender());
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
        String query = "SELECT a.idAthlete, a.password, a.name, " +
                "c.idCountry, c.name AS countryName, c.continent, " +
                "g.idGender, g.description AS genderDescription, " +
                "a.height, a.weight, a.dateOfBirth, " +
                "p.year AS participationYear " +
                "FROM tblAthlete a " +
                "INNER JOIN tblCountry c ON a.idCountry = c.idCountry " +
                "INNER JOIN tblGender g ON a.idGender = g.idGender " +
                "LEFT JOIN tblMedal p ON a.idAthlete = p.idAthlete " +
                "WHERE a.idAthlete = ? " +
                "ORDER BY p.year";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idAthlete);
        if (rs != null && rs.next()) {
            String password = rs.getString("password");
            String name = rs.getString("name");
            Country country = new Country(rs.getInt("idCountry"), rs.getString("countryName"), rs.getString("continent"));
            Gender gender = new Gender(rs.getInt("idGender"), rs.getString("genderDescription"));
            int height = rs.getInt("height");
            float weight = rs.getFloat("weight");
            java.sql.Date dateOfBirth = rs.getDate("dateOfBirth");
//            List<Medal> medals = new ArrayList<>();
//            do {
//                int participationYear = rs.getInt("year");
//                if (!rs.wasNull()) {
//                    int gold = rs.getInt("gold");
//                    int silver = rs.getInt("silver");
//                    int bronze = rs.getInt("bronze");
//                    int certificate = rs.getInt("certificate");
//                    Participation participation = new Participation(participationYear, gold, silver, bronze, certificate);
//                    olympicParticipations.add(participation);
//                }
//            } while (rs.next() && rs.getInt("idAthlete") == idAthlete);

            return new Athlete(idAthlete, password, name, country, gender, height, weight, dateOfBirth, null);
        }
        return null;
    }
}