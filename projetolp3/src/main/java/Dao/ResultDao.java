package Dao;

import Models.*;
import Utils.ConnectionsUtlis;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultDao {
    public static List<Result> getResults() throws SQLException {
        List<Result> results = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblResult;");
        if (rs != null) {
            while (rs.next()) {
                int idResult = rs.getInt("idResult");
                int idSport = rs.getInt("idSport");
                int idAthlete = rs.getInt("idAthlete");
                int idTeam = rs.getInt("idTeam");
                java.sql.Date date = rs.getDate("date");
                String resultValue = rs.getString("result");
                int idLocal = rs.getInt("idLocal");

                SportDao sportDao = new SportDao();
                Sport sport = sportDao.getSportById(idSport);
                AthleteDao athleteDao = new AthleteDao();
                Athlete athlete = athleteDao.getAthleteById(idAthlete);
                Team team = TeamDao.getTeamById(idTeam);
                Local local = LocalDao.getLocalById(idLocal);

                Result result = new Result(idResult, sport, athlete, team, date, resultValue, local);
                results.add(result);
            }
        } else {
            System.out.println("ResultSet is null. No results found.");
        }
        return results;
    }

    public static void addResult(Result result) throws SQLException {
        String query = "INSERT INTO tblResult (idSport, idAthlete, idTeam, date, result, idLocal) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, result.getSport().getIdSport());
            stmt.setInt(2, result.getAthlete().getIdAthlete());
            stmt.setInt(3, result.getTeam().getIdTeam());
            stmt.setDate(4, result.getDate());
            stmt.setString(5, result.getResult());
            stmt.setInt(6, result.getLocal().getIdLocal());
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

    public void addResultAthlete(int idSport, int idAthlete, Date date, int result, int idLocal) throws SQLException {
        String query = "INSERT INTO tblResult (idSport, idAthlete, date, result, idLocal) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSport);
            stmt.setInt(2, idAthlete);
            stmt.setDate(3, date);
            stmt.setInt(4, result);
            stmt.setInt(5, idLocal);
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

    public void addResultTeam(int idSport, int idTeam, Date date, int result, int idLocal) throws SQLException {
        String query = "INSERT INTO tblResult (idSport, idTeam, date, result, idLocal) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSport);
            stmt.setInt(2, idTeam);
            stmt.setDate(3, date);
            stmt.setInt(4, result);
            stmt.setInt(5, idLocal);
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

    public void addResultAthleteTeam(int idSport, int idAthlete, int idTeam, Date date, int result, int idLocal) throws SQLException {
        String query = "INSERT INTO tblResult (idSport, idAthlete, idTeam, date, result, idLocal) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSport);
            stmt.setInt(2, idAthlete);
            stmt.setInt(3, idTeam);
            stmt.setDate(4, date);
            stmt.setInt(5, result);
            stmt.setInt(6, idLocal);
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

    public static void removeResult(int idResult) throws SQLException {
        String query = "DELETE FROM tblResult WHERE idResult = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idResult);
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

    public static void updateResult(Result result) throws SQLException {
        String query = "UPDATE tblResult SET idSport = ?, idAthlete = ?, idTeam = ?, date = ?, result = ?, idLocal = ? WHERE idResult = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, result.getSport().getIdSport());
            stmt.setInt(2, result.getAthlete().getIdAthlete());
            stmt.setInt(3, result.getTeam().getIdTeam());
            stmt.setDate(4, result.getDate());
            stmt.setString(5, result.getResult());
            stmt.setInt(6, result.getLocal().getIdLocal());
            stmt.setInt(7, result.getIdResult());
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

    public Result getResultById(int idResult) throws SQLException {
        String query = "SELECT * FROM tblResult WHERE idResult = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idResult);
        if (rs != null && rs.next()) {
            int idSport = rs.getInt("idSport");
            int idAthlete = rs.getInt("idAthlete");
            int idTeam = rs.getInt("idTeam");
            java.sql.Date date = rs.getDate("date");
            String resultValue = rs.getString("result");
            int idLocal = rs.getInt("idLocal");

            SportDao sportDao = new SportDao();
            Sport sport = sportDao.getSportById(idSport);
            AthleteDao athleteDao = new AthleteDao();
            Athlete athlete = athleteDao.getAthleteById(idAthlete);
            Team team = TeamDao.getTeamById(idTeam);
            Local local = LocalDao.getLocalById(idLocal);
            return new Result(idResult, sport, athlete, team, date, resultValue, local);
        }
        return null;
    }

    public List<List> getResultByAthlete(int idAthlete) throws SQLException{
        String query = "SELECT r.*, s.name as sportName, s.type as sportType, t.name as teamName, l.name as localName FROM tblResult as r " +
                "LEFT JOIN tblSport as s ON r.idSport = s.idSport " +
                "LEFT JOIN tblTeam as t ON r.idTeam = t.idTeam " +
                "LEFT JOIN tblLocal as l on r.idLocal = l.idLocal " +
                "WHERE r.idAthlete = ?;";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idAthlete);
        List<List> results = new ArrayList<>();
        if (rs != null) {
            while (rs.next()) {

                List<Object> result = new ArrayList<>();
                result.add(rs.getInt("result"));
                result.add(rs.getString("sportName"));
                result.add(rs.getString("sportType"));
                result.add(rs.getString("teamName"));
                result.add(rs.getDate("date"));
                result.add(rs.getString("localName"));
                results.add(result);
            }
        } else {
            System.out.println("ResultSet is null. No results found.");
        }
        return results;
    }
}