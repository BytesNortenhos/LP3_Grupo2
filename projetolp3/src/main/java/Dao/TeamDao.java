package Dao;

import Utils.ConnectionsUtlis;
import models.Team;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamDao {
    public static List<Team> getTeams() throws SQLException {
        List<Team> teams = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblTeam;");
        if (rs != null) {
            while (rs.next()) {
                int idTeam = rs.getInt("idTeam");
                String name = rs.getString("name");
                int idCountry = rs.getInt("idCountry");
                int idGender = rs.getInt("idGender");
                int idSport = rs.getInt("idSport");
                int yearFounded = rs.getInt("yearFounded");

                Team team = new Team(idTeam, name, idCountry, idGender, idSport, yearFounded);
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
            stmt.setInt(2, team.getIdCountry());
            stmt.setInt(3, team.getIdGender());
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
            stmt.setInt(2, team.getIdCountry());
            stmt.setInt(3, team.getIdGender());
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
        String query = "SELECT * FROM tblTeam WHERE idTeam = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idTeam);
        if (rs != null && rs.next()) {
            String name = rs.getString("name");
            int idCountry = rs.getInt("idCountry");
            int idGender = rs.getInt("idGender");
            int idSport = rs.getInt("idSport");
            int yearFounded = rs.getInt("yearFounded");
            return new Team(idTeam, name, idCountry, idGender, idSport, yearFounded);
        }
        return null;
    }
}
