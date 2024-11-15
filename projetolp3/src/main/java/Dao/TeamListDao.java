package Dao;

import Models.*;
import Utils.ConnectionsUtlis;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamListDao {


    public static List<TeamList> getAllTeamLists() throws SQLException {
        List<TeamList> teamLists = new ArrayList<>();
        String query = "SELECT tl.idTeamList, tl.idTeam, tl.idAthlete, tl.idStatus, tl.year, tl.isActive, " +
                "a.name AS athleteName, t.name AS teamName, ts.description AS statusDescription " +
                "FROM tblTeamList tl " +
                "INNER JOIN tblAthlete a ON tl.idAthlete = a.idAthlete " +
                "INNER JOIN tblTeam t ON tl.idTeam = t.idTeam " +
                "INNER JOIN tblTeamListStatus ts ON tl.idStatus = ts.idStatus";

        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query);

        if (rs != null) {
            while (rs.next()) {
                int idTeamList = rs.getInt("idTeamList");
                int idTeam = rs.getInt("idTeam");
                int idAthlete = rs.getInt("idAthlete");
                Athlete athlete = AthleteDao.getAthleteByIdMinimum(idAthlete);
                int idStatus = rs.getInt("idStatus");
                TeamListStatus status = TeamListStatusDao.getTeamListStatusById(idStatus);
                int year = rs.getInt("year");
                boolean isActive = rs.getBoolean("isActive");

                TeamList teamList = new TeamList(idTeamList, idTeam, athlete, status, year, isActive);
                teamLists.add(teamList);
            }
        }

        return teamLists;
    }

    // Adicionar um TeamList
    public static void addTeamList(TeamList teamList) throws SQLException {
        String query = "INSERT INTO tblTeamList (idTeam, idAthlete, idStatus, year, isActive) " +
                "VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, teamList.getIdTeam());
            stmt.setInt(2, teamList.getAthlete().getIdAthlete());
            stmt.setInt(3, teamList.getStatus().getIdStatus());
            stmt.setInt(4, teamList.getYear());
            stmt.setBoolean(5, teamList.isActive());

            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Remover um TeamList
    public static void removeTeamList(int idTeamList) throws SQLException {
        String query = "DELETE FROM tblTeamList WHERE idTeamList = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idTeamList);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Atualizar um TeamList
    public static void updateTeamList(TeamList teamList) throws SQLException {
        String query = "UPDATE tblTeamList SET idTeam = ?, idAthlete = ?, idStatus = ?, year = ?, isActive = ? " +
                "WHERE idTeamList = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, teamList.getIdTeam());
            stmt.setInt(2, teamList.getAthlete().getIdAthlete());
            stmt.setInt(3, teamList.getStatus().getIdStatus());
            stmt.setInt(4, teamList.getYear());
            stmt.setBoolean(5, teamList.isActive());
            stmt.setInt(6, teamList.getIdTeamList());

            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    // Recuperar TeamList pelo ID
    public static TeamList getTeamListById(int idTeamList) throws SQLException {
        String query = "SELECT tl.idTeamList, tl.idTeam, tl.idAthlete, tl.idStatus, tl.year, tl.isActive, " +
                "a.name AS athleteName, t.name AS teamName, ts.description AS statusDescription " +
                "FROM tblTeamList tl " +
                "INNER JOIN tblAthlete a ON tl.idAthlete = a.idAthlete " +
                "INNER JOIN tblTeam t ON tl.idTeam = t.idTeam " +
                "INNER JOIN tblTeamListStatus ts ON tl.idStatus = ts.idStatus " +
                "WHERE tl.idTeamList = ?";

        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idTeamList);

        if (rs != null && rs.next()) {
            int idTeam = rs.getInt("idTeam");
            int idAthlete = rs.getInt("idAthlete");
            Athlete athlete = AthleteDao.getAthleteByIdMinimum(idAthlete);
            int idStatus = rs.getInt("idStatus");
            TeamListStatus status = TeamListStatusDao.getTeamListStatusById(idStatus);
            int year = rs.getInt("year");
            boolean isActive = rs.getBoolean("isActive");

            return new TeamList(idTeamList, idTeam, athlete, status, year, isActive);
        }

        return null;
    }
}
