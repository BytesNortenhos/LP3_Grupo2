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
    /**
     * Get all results
     *
     * @return {List<Result>} List of results
     * @throws SQLException
     */
    public static List<Result> getResults() throws SQLException {
        List<Result> results = new ArrayList<>();
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery("SELECT * FROM tblResult;");
        if (rs != null) {
            while (rs.next()) {
                int idResult = rs.getInt("idResult");
                int idSport = rs.getInt("idSport");
                int idAthlete = rs.getInt("idAthlete");
                int idTeam = rs.getInt("idTeam");
                java.sql.Date date = rs.getDate("date");
                String resultValue = rs.getString("result");
                int idLocal = rs.getInt("idLocal");
                int position = rs.getInt("position");

                SportDao sportDao = new SportDao();
                Sport sport = sportDao.getSportById(idSport);
                AthleteDao athleteDao = new AthleteDao();
                Athlete athlete = athleteDao.getAthleteById(idAthlete);
                Team team = TeamDao.getTeamById(idTeam);
                Local local = LocalDao.getLocalById(idLocal);

                Result result = new Result(idResult, sport, athlete, team, date, resultValue, local, position);
                results.add(result);
            }
        } else {
            System.out.println("ResultSet is null. No results found.");
        }
        return results;
    }

    /**
     * Add result
     *
     * @param result {Result} Result
     * @throws SQLException
     */
    public void addResult(Result result) throws SQLException {
        String query = "INSERT INTO tblResult (idSport, idAthlete, idTeam, date, result, idLocal) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
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

    /**
     * Add result athlete
     *
     * @param idSport   {int} Id sport
     * @param idAthlete {int} Id athlete
     * @param date      {Date} Date
     * @param result    {int} Result
     * @param idLocal   {int} Id local
     * @throws SQLException
     */
    public void addResultAthlete(int idSport, int idAthlete, Date date, String result, int idLocal, int position) throws SQLException {
        String query = "INSERT INTO tblResult (idSport, idAthlete, date, result, idLocal, position) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSport);
            stmt.setInt(2, idAthlete);
            stmt.setDate(3, date);
            stmt.setString(4, result);
            stmt.setInt(5, idLocal);
            stmt.setInt(6, position);
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
     * Add result team
     *
     * @param idSport {int} Id sport
     * @param idTeam  {int} Id team
     * @param date    {Date} Date
     * @param result  {int} Result
     * @param idLocal {int} Id local
     * @throws SQLException
     */
    public void addResultTeam(int idSport, int idTeam, Date date, int result, int idLocal) throws SQLException {
        String query = "INSERT INTO tblResult (idSport, idTeam, date, result, idLocal) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
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

    /**
     * Add result athlete team
     *
     * @param idSport   {int} Id sport
     * @param idAthlete {int} Id athlete
     * @param idTeam    {int} Id team
     * @param date      {Date} Date
     * @param result    {int} Result
     * @param idLocal   {int} Id local
     * @throws SQLException
     */
    public void addResultAthleteTeam(int idSport, int idAthlete, int idTeam, Date date, String result, int idLocal, int position) throws SQLException {
        String query = "INSERT INTO tblResult (idSport, idAthlete, idTeam, date, result, idLocal, position) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, idSport);
            stmt.setInt(2, idAthlete);
            stmt.setInt(3, idTeam);
            stmt.setDate(4, date);
            stmt.setString(5, result);
            stmt.setInt(6, idLocal);
            stmt.setInt(7, position);
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
     * Remove result
     *
     * @param idResult {int} Id result
     * @throws SQLException
     */
    public static void removeResult(int idResult) throws SQLException {
        String query = "DELETE FROM tblResult WHERE idResult = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
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

    /**
     * Update result
     *
     * @param result {Result} Result
     * @throws SQLException
     */
    public void updateResult(Result result) throws SQLException {
        String query = "UPDATE tblResult SET idSport = ?, idAthlete = ?, idTeam = ?, date = ?, result = ?, idLocal = ? WHERE idResult = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
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

    /**
     * Get result by id
     *
     * @param idResult {int} Id result
     * @return {Result} Result
     * @throws SQLException
     */
    public Result getResultById(int idResult) throws SQLException {
        String query = "SELECT * FROM tblResult WHERE idResult = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idResult);
        if (rs != null && rs.next()) {
            int idSport = rs.getInt("idSport");
            int idAthlete = rs.getInt("idAthlete");
            int idTeam = rs.getInt("idTeam");
            java.sql.Date date = rs.getDate("date");
            String resultValue = rs.getString("result");
            int idLocal = rs.getInt("idLocal");
            int position = rs.getInt("position");

            SportDao sportDao = new SportDao();
            Sport sport = sportDao.getSportById(idSport);
            AthleteDao athleteDao = new AthleteDao();
            Athlete athlete = athleteDao.getAthleteById(idAthlete);
            Team team = TeamDao.getTeamById(idTeam);
            Local local = LocalDao.getLocalById(idLocal);
            return new Result(idResult, sport, athlete, team, date, resultValue, local, position);
        }
        return null;
    }

    /**
     * Get result by athlete
     *
     * @param idAthlete {int} Id athlete
     * @return {List<Result>} List of results
     * @throws SQLException
     */
    public List<List> getResultByAthlete(int idAthlete) throws SQLException {
        String query = "SELECT r.*, s.name as sportName, s.scoringMeasure as scoringMeasure, s.metrica as metrica, s.idSport as idSport, s.type as sportType, s.oneGame as one, t.name as teamName, t.idTeam as teamId, l.name as localName FROM tblResult as r " +
                "LEFT JOIN tblSport as s ON r.idSport = s.idSport " +
                "LEFT JOIN tblTeam as t ON r.idTeam = t.idTeam " +
                "LEFT JOIN tblLocal as l on r.idLocal = l.idLocal " +
                "WHERE r.idAthlete = ?;";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idAthlete);
        List<List> results = new ArrayList<>();
        if (rs != null) {
            while (rs.next()) {

                List<Object> result = new ArrayList<>();
                result.add(rs.getString("result"));
                result.add(rs.getString("sportName"));
                result.add(rs.getString("sportType"));
                result.add(rs.getString("teamName"));
                result.add(rs.getDate("date"));
                result.add(rs.getString("localName"));
                result.add(rs.getInt("idSport"));
                result.add(rs.getString("one"));
                result.add(rs.getString("teamId"));
                result.add(rs.getString("scoringMeasure"));
                result.add(rs.getString("metrica"));
                results.add(result);
            }
        } else {
            System.out.println("ResultSet is null. No results found.");
        }
        return results;
    }

    /**
     * Get position by id
     *
     * @param idSport {int} Id sport
     * @param date    {String} Date
     * @return {List<List>} List of Results
     * @throws SQLException
     */
    public List<List> getPositionById(int idSport, String date) throws SQLException {
        List<List> positions = new ArrayList<>();
        String query = "SELECT *,\n" +
                "       DENSE_RANK() OVER (PARTITION BY idSport, [date] ORDER BY idTeam ASC) AS lugar\n" +
                "FROM tblResult\n" +
                "WHERE idSport = ? AND [date] = ?;";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idSport, "%" + date + "%");
        if (rs != null) {
            while (rs.next()) {
                List<Object> positon = new ArrayList<>();
                positon.add(rs.getInt("lugar"));
                positon.add(rs.getInt("idAthlete"));
                positions.add(positon);
            }
        }
        return positions;
    }

    public List<List> getPositionById1(int idSport, String date) throws SQLException {
        System.out.println(date);
        List<List> positions = new ArrayList<>();
        String query = "SELECT *,\n" +
                "       DENSE_RANK() OVER (PARTITION BY idSport, [date] ORDER BY idTeam ASC) AS lugar\n" +
                "FROM tblResult\n" +
                "WHERE idSport = ? AND [date] = ?;";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idSport, "%" + date + "%");
        if (rs != null) {
            while (rs.next()) {
                List<Object> positon = new ArrayList<>();
                positon.add(rs.getInt("lugar"));
                positon.add(rs.getInt("idAthlete"));
                positions.add(positon);
            }
        }
        return positions;
    }

    public List<List<Object>> getPositionById2(int idSport, String date) throws SQLException {
        System.out.println("Query Date: " + date); // Apenas para debugging

        List<List<Object>> positions = new ArrayList<>();

        // Ajuste na consulta SQL para evitar conflitos com palavras reservadas
        String query = """
                SELECT *,
                       DENSE_RANK() OVER (PARTITION BY idSport, [date] ORDER BY idTeam ASC) AS lugar
                FROM tblResult
                WHERE idSport = ? AND [date] = ?;
                """;

        // Usando CachedRowSet para executar a consulta
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idSport, date); // "%" removido para manter igualdade exata

        if (rs != null) {
            while (rs.next()) {
                List<Object> position = new ArrayList<>();
                position.add(rs.getInt("lugar"));       // Adiciona a posição
                position.add(rs.getInt("idAthlete"));  // Adiciona o ID do atleta
                positions.add(position);
            }
        }

        return positions;
    }

    public List<List> getPositionByIdCollective(int idSport, String date) throws SQLException {
        List<List> positions = new ArrayList<>();

        String query = """
                SELECT *,
                       DENSE_RANK() OVER (PARTITION BY idSport, [date] ORDER BY idTeam ASC) AS lugar
                FROM tblResult
                WHERE idSport = ? AND [date] = ?;
                """;

        // Executa a consulta com os parâmetros
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idSport, date);

        if (rs != null) {
            while (rs.next()) {
                List<Object> position = new ArrayList<>();
                System.out.println(rs.getInt("lugar"));
                position.add(rs.getInt("lugar"));   // 'lugar' gerado pelo DENSE_RANK
                position.add(rs.getInt("idTeam")); // Trocar para 'idTeam' (coletivo)
                positions.add(position);
            }
        }

        return positions;
    }

    public List<List> getPositionByIdIndividualOne(int idSport, String date) throws SQLException {
        List<List> positions = new ArrayList<>();

        String query = """
                SELECT *,
                       DENSE_RANK() OVER (PARTITION BY idSport, [date] ORDER BY idAthlete ASC) AS lugar
                FROM tblResult
                WHERE idSport = ? AND [date] = ?;
                """;
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idSport, date);

        if (rs != null) {
            while (rs.next()) {
                List<Object> position = new ArrayList<>();
                position.add(rs.getInt("lugar"));
                position.add(rs.getInt("idAthlete"));
                positions.add(position);
            }
        }

        return positions;
    }

    public List<List> getPositionByIdIndividualMultiple(int idSport, String date) throws SQLException {
        List<List> positions = new ArrayList<>();

        String query = """
                SELECT *,
                       DENSE_RANK() OVER (PARTITION BY idSport, [date] ORDER BY idAthlete ASC) AS lugar
                FROM tblResult
                WHERE idSport = ? AND [date] = ?;
                """;

        // Executa a consulta com os parâmetros
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idSport, date);

        if (rs != null) {
            while (rs.next()) {
                List<Object> position = new ArrayList<>();
                position.add(rs.getInt("lugar"));       // Nome correto da coluna calculada
                position.add(rs.getInt("idAthlete"));  // Nome correto da coluna existente
                positions.add(position);
            }
        }

        return positions;
    }

    public List<List> getResultBySport(int idSport, String gender, int year) throws SQLException {
        String query = "SELECT DISTINCT r.*, s.name as sportName, s.type as sportType, a.name as athleteName, a.image as profilePhoto, t.name as teamName, g.description as gender, re.year as year, l.name as localName FROM tblResult as r " +
                "LEFT JOIN tblSport as s ON r.idSport = s.idSport " +
                "LEFT JOIN tblAthlete as a ON r.idAthlete = a.idAthlete " +
                "LEFT JOIN tblTeam as t ON r.idTeam = t.idTeam " +
                "LEFT JOIN tblLocal as l on r.idLocal = l.idLocal " +
                "LEFT JOIN tblGender AS g ON s.idGender = g.idGender " +
                "LEFT JOIN tblRegistration AS re ON re.year = ? " +
                "WHERE r.idSport = ? AND g.description = ? AND re.idStatus =4;";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, year, idSport, gender);
        List<List> results = new ArrayList<>();
        if (rs != null) {
            while (rs.next()) {
                List<Object> result = new ArrayList<>();
                result.add(rs.getString("result"));
                result.add(rs.getString("idSport"));
                result.add(rs.getString("sportName"));
                result.add(rs.getString("sportType"));
                result.add(rs.getString("athleteName"));
                result.add(rs.getString("teamName"));
                result.add(rs.getDate("date"));
                result.add(rs.getString("localName"));
                result.add(rs.getString("profilePhoto"));
                result.add(rs.getString("position"));
                result.add(rs.getString("year"));
                results.add(result);
            }
        }
        return results;
    }

    public List<Result> getResultByAthleteJunit(int idAthlete) throws SQLException{
        String query = "Select * From tblResult Where idAthlete = ?;";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idAthlete);
        List<Result> results = new ArrayList<>();
        if (rs != null) {
            while (rs.next()) {
                int id = rs.getInt("idResult");
                Sport sport = new SportDao().getSportById(rs.getInt("idSport"));
                Athlete athlete = new AthleteDao().getAthleteById(rs.getInt("idAthlete"));
                Date date = rs.getDate("date");
                String Result = rs.getString("result");
                Local local = new LocalDao().getLocalById(rs.getInt("idLocal"));
                int position = rs.getInt("position");

                Result result = new Result(id, sport, athlete, null, date, Result, local, position);
                results.add(result);
            }
        } else {
            System.out.println("ResultSet is null. No results found.");
        }
        return results;
    }
}