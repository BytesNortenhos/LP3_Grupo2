package Dao;

import Models.*;
import Utils.ConnectionsUtlis;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamListDao {


    /**
     * Get all team lists
     * @return {List<TeamList>} List of team lists
     * @throws SQLException
     */
    public List<TeamList> getAllTeamLists() throws SQLException {
        List<TeamList> teamLists = new ArrayList<>();
        String query = "SELECT tl.idTeamList, tl.idTeam, tl.idAthlete, tl.idStatus, tl.year, tl.isActive, " +
                "a.name AS athleteName, t.name AS teamName, ts.description AS statusDescription " +
                "FROM tblTeamList tl " +
                "INNER JOIN tblAthlete a ON tl.idAthlete = a.idAthlete " +
                "INNER JOIN tblTeam t ON tl.idTeam = t.idTeam " +
                "INNER JOIN tblTeamListStatus ts ON tl.idStatus = ts.idStatus";

        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query);

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

    /**
     * Add a new TeamList
     * @param teamList {TeamList} TeamList
     * @throws SQLException
     */
    public void addTeamList(TeamList teamList) throws SQLException {
        String query = "INSERT INTO tblTeamList (idTeam, idAthlete, idStatus, year, isActive) " +
                "VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
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

    /**
     * Remove a TeamList
     * @param idTeamList {int} Id TeamList
     * @throws SQLException
     */
    public void removeTeamList(int idTeamList) throws SQLException {
        String query = "DELETE FROM tblTeamList WHERE idTeamList = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idTeamList);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * Update a TeamList
     * @param teamList {TeamList} TeamList
     * @throws SQLException
     */
    public void updateTeamList(TeamList teamList) throws SQLException {
        String query = "UPDATE tblTeamList SET idTeam = ?, idAthlete = ?, idStatus = ?, year = ?, isActive = ? " +
                "WHERE idTeamList = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
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

    /**
     * Get TeamList by id
     * @param idTeamList {int} Id TeamList
     * @return {TeamList} TeamList
     * @throws SQLException
     */
    public TeamList getTeamListById(int idTeamList) throws SQLException {
        String query = "SELECT tl.idTeamList, tl.idTeam, tl.idAthlete, tl.idStatus, tl.year, tl.isActive, " +
                "a.name AS athleteName, t.name AS teamName, ts.description AS statusDescription " +
                "FROM tblTeamList tl " +
                "INNER JOIN tblAthlete a ON tl.idAthlete = a.idAthlete " +
                "INNER JOIN tblTeam t ON tl.idTeam = t.idTeam " +
                "INNER JOIN tblTeamListStatus ts ON tl.idStatus = ts.idStatus " +
                "WHERE tl.idTeamList = ?";

        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idTeamList);

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

    /**
     * Insert a new record into tblTeamList
     * @param athleteId {int} Id of the athlete
     * @param teamId {int} Id of the team
     * @param statusId {int} Id of the status
     * @param year {int} Year of the event
     * @throws SQLException
     */
    public void insertIntoTeamList(int athleteId, int teamId, int statusId, int year) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM tblTeamList WHERE idAthlete = ? AND idTeam = ? AND idStatus = ? AND year = ?";

        String insertQuery = "INSERT INTO tblTeamList (idTeam, idAthlete, idStatus, year, isActive) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();

            checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, athleteId);
            checkStmt.setInt(2, teamId);
            checkStmt.setInt(3, statusId);
            checkStmt.setInt(4, year);

            rs = checkStmt.executeQuery();

            if (rs != null && rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    System.out.println("A inscrição já existe na lista de equipes!");
                    return;
                }
            }

            insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setInt(1, teamId);
            insertStmt.setInt(2, athleteId);
            insertStmt.setInt(3, statusId);
            insertStmt.setInt(4, year);
            insertStmt.setBoolean(5, true);

            insertStmt.executeUpdate();
            System.out.println("Inscrição na lista de equipes realizada com sucesso!");

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (checkStmt != null) {
                checkStmt.close();
            }
            if (insertStmt != null) {
                insertStmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}