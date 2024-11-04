package Dao;

import Models.Country;
import Models.Gender;
import Utils.ConnectionsUtlis;
import Models.Team;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamDao {
    public static List<Team> getTeams() throws SQLException {
        List<Team> teams = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT t.idTeam, t.name AS teamName, t.idCountry, c.name AS countryName, " +
                "c.continent, t.idGender, g.desc AS genderDesc, " +
                "t.idSport, t.yearFounded " +
                "FROM tblTeam t " +
                "INNER JOIN tblCountry c ON t.idCountry = c.idCountry " +
                "INNER JOIN tblGender g ON t.idGender = g.idGender;");
        if (rs != null) {
            while (rs.next()) { int idTeam = rs.getInt("idTeam");
                String teamName = rs.getString("teamName");
                int idCountry = rs.getInt("idCountry");
                String countryName = rs.getString("countryName");
                String continent = rs.getString("continent");
                int idGender = rs.getInt("idGender");
                String genderDesc = rs.getString("genderDesc");
                int idSport = rs.getInt("idSport");
                int yearFounded = rs.getInt("yearFounded");
                Country country = new Country(idCountry, countryName, continent);
                Gender gender = new Gender(idGender, genderDesc);

                Team team = new Team(idTeam, teamName, country, gender, idSport, yearFounded);
                teams.add(team);
            }
        } else {
            System.out.println("ResultSet is null. No results for Team found.");
        }
        return teams;
    }

    public static void addTeam(Team team) throws SQLException {
        String query = "INSERT INTO tblTeam (name, idCountry, idGender, idSport, yearFounded) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, team.getName());
            stmt.setInt(2, team.getCountry().getIdCountry());
            stmt.setInt(3, team.getGenre().getIdGender());
            stmt.setInt(4, team.getIdSport());
            stmt.setInt(5, team.getYearFounded());
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

    public static void removeTeam(int idTeam) throws SQLException {
        String query = "DELETE FROM tblTeam WHERE idTeam = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idTeam);
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

    public static void updateTeam(Team team) throws SQLException {
        String query = "UPDATE tblTeam SET name = ?, idCountry = ?, idGender = ?, idSport = ?, yearFounded = ? WHERE idTeam = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, team.getName());
            stmt.setInt(2, team.getCountry().getIdCountry());
            stmt.setInt(3, team.getGenre().getIdGender());
            stmt.setInt(4, team.getIdSport());
            stmt.setInt(5, team.getYearFounded());
            stmt.setInt(6, team.getIdTeam());
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

    public static Team getTeamById(int idTeam) throws SQLException {
        String query = "SELECT t.idTeam, t.name AS teamName, c.idCountry, c.name AS countryName, c.continent, " +
                "g.idGender AS genderId, g.desc AS genderDesc, " +
                "t.idSport, t.yearFounded " +
                "FROM tblTeam t " +
                "INNER JOIN tblCountry c ON t.idCountry = c.idCountry " +
                "INNER JOIN tblGender g ON t.idGender = g.idGender " +
                "WHERE t.idTeam = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idTeam);
        if (rs != null && rs.next()) {
            String teamName = rs.getString("teamName");
            int idCountry = rs.getInt("idCountry");
            String countryName = rs.getString("countryName");
            String continent = rs.getString("continent");
            int genderId = rs.getInt("genderId");
            String genderDesc = rs.getString("genderDesc");
            int idSport = rs.getInt("idSport");
            int yearFounded = rs.getInt("yearFounded");
            Country country = new Country(idCountry, countryName, continent);
            Gender gender = new Gender(genderId, genderDesc);
            return new Team(idTeam, teamName, country, gender, idSport, yearFounded);
        }
        return null;
    }
}