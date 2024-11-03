package Dao;

import Utils.ConnectionsUtlis;
import models.Result;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
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
                String date = rs.getString("date");
                String resultValue = rs.getString("result");
                int idLocal = rs.getInt("idLocal");

                Result result = new Result(idResult, idSport, idAthlete, idTeam, date, resultValue, idLocal);
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

            stmt.setInt(1, result.getIdSport());
            stmt.setInt(2, result.getIdAthlete());
            stmt.setInt(3, result.getIdTeam());
            stmt.setString(4, result.getDate());
            stmt.setString(5, result.getResultValue());
            stmt.setInt(6, result.getIdLocal());
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

            stmt.setInt(1, result.getIdSport());
            stmt.setInt(2, result.getIdAthlete());
            stmt.setInt(3, result.getIdTeam());
            stmt.setString(4, result.getDate());
            stmt.setString(5, result.getResultValue());
            stmt.setInt(6, result.getIdLocal());
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

    public static Result getResultById(int idResult) throws SQLException {
        String query = "SELECT * FROM tblResult WHERE idResult = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idResult);
        if (rs != null && rs.next()) {
            int idSport = rs.getInt("idSport");
            int idAthlete = rs.getInt("idAthlete");
            int idTeam = rs.getInt("idTeam");
            String date = rs.getString("date");
            String resultValue = rs.getString("result");
            int idLocal = rs.getInt("idLocal");
            return new Result(idResult, idSport, idAthlete, idTeam, date, resultValue, idLocal);
        }
        return null;
    }
}
