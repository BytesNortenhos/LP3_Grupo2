package Dao;

import Models.*;
import Utils.ConnectionsUtlis;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SportDao {
    public static List<Sport> getSports() throws SQLException {
        List<Sport> sports = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT s.*," +
                "g.description AS genderDescription," +
                "r.year AS olympicYear," +
                "r.timeMS," +
                "r.medals " +
                "FROM tblSport s " +
                "JOIN tblGender g ON s.idGender = g.idGender " +
                "LEFT JOIN tblOlympicRecord r ON s.idSport = r.idSport;");
        if (rs != null) {
            while (rs.next()) {
                int idSport = rs.getInt("idSport");
                String type = rs.getString("type");
                int idGender = rs.getInt("idGender");
                String name = rs.getString("name");
                String description = rs.getString("description");
                int minParticipants = rs.getInt("minParticipants");
                String scoringMeasure = rs.getString("scoringMeasure");
                String oneGame = rs.getString("oneGame");

                String genderDescription = rs.getString("genderDescription");
                Gender gender = new Gender(idGender, genderDescription);

                OlympicRecord olympicRecord = OlympicRecordDao.getOlympicRecordByIdV2(idSport, rs.getInt("olympicYear"));

                List<WinnerOlympic> winnerOlympics = WinnerOlympicDao.getWinnerOlympicsBySportV2(idSport);
                List<Rule> rules = RuleDao.getRulesBySportV2(idSport);

                Sport sport = new Sport(idSport, type, gender, name, description, minParticipants, scoringMeasure, oneGame, olympicRecord, winnerOlympics, rules);
                sports.add(sport);
            }
        } else {
            System.out.println("ResultSet is null. No results for Sport found.");
        }
        return sports;
    }

    public static void addSport(Sport sport) throws SQLException {
        String query = "INSERT INTO tblSport (type, idGender, name, description, minParticipants, scoringMeasure, oneGame) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, sport.getType());
            stmt.setInt(2, sport.getGenre().getIdGender());
            stmt.setString(3, sport.getName());
            stmt.setString(4, sport.getDesc());
            stmt.setInt(5, sport.getMinParticipants());
            stmt.setString(6, sport.getScoringMeasure());
            stmt.setString(7, sport.getOneGame());
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

    public static void removeSport(int idSport) throws SQLException {
        String query = "DELETE FROM tblSport WHERE idSport = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idSport);
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

    public static void updateSport(Sport sport) throws SQLException {
        String query = "UPDATE tblSport SET type = ?, idGender = ?, name = ?, description = ?, minParticipants = ?, scoringMeasure = ?, oneGame = ? WHERE idSport = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, sport.getType());
            stmt.setInt(2, sport.getGenre().getIdGender());
            stmt.setString(3, sport.getName());
            stmt.setString(4, sport.getDesc());
            stmt.setInt(5, sport.getMinParticipants());
            stmt.setString(6, sport.getScoringMeasure());
            stmt.setString(7, sport.getOneGame());
            stmt.setInt(8, sport.getIdSport());
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

    public static Sport getSportById(int idSport) throws SQLException {
        // Consulta SQL para pegar os dados principais do esporte
        String query = "SELECT s.*, " +
                "g.description AS genderDescription, " +
                "r.year AS olympicYear, " +
                "r.timeMS, " +
                "r.medals " +
                "FROM tblSport s " +
                "JOIN tblGender g ON s.idGender = g.idGender " +
                "LEFT JOIN tblOlympicRecord r ON s.idSport = r.idSport " +
                "WHERE s.idSport = ?";

        // Executa a consulta SQL com o idSport fornecido
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        if (rs != null && rs.next()) {
            int idSportResult = rs.getInt("idSport");
            String type = rs.getString("type");
            int idGender = rs.getInt("idGender");
            String name = rs.getString("name");
            String description = rs.getString("description");
            int minParticipants = rs.getInt("minParticipants");
            String scoringMeasure = rs.getString("scoringMeasure");
            String oneGame = rs.getString("oneGame");
            // Criar o objeto Gender usando os dados da consulta
            String genderDescription = rs.getString("genderDescription");
            Gender gender = new Gender(idGender, genderDescription);

            // Carregar o recorde olímpico associado ao esporte
            OlympicRecord olympicRecord = OlympicRecordDao.getOlympicRecordById(idSportResult, rs.getInt("olympicYear"));

            // Carregar os vencedores olímpicos associados ao esporte
            List<WinnerOlympic> winnerOlympics = WinnerOlympicDao.getWinnerOlympicsBySport(idSportResult);

            // Carregar as regras associadas ao esporte
            List<Rule> rules = RuleDao.getRulesBySport(idSportResult);

            // Criar e retornar o objeto Sport com todos os dados carregados
            return new Sport(idSportResult, type, gender, name, description, minParticipants, scoringMeasure, oneGame, olympicRecord, winnerOlympics, rules);
        }

        // Caso não encontre o esporte, retorna null
        return null;
    }
    public static Sport getSportByIdV2(int idSport) throws SQLException {
        // Consulta SQL para pegar os dados principais do esporte
        String query = "SELECT s.*, " +
                "g.description AS genderDescription, " +
                "r.year AS olympicYear, " +
                "r.timeMS, " +
                "r.medals " +
                "FROM tblSport s " +
                "JOIN tblGender g ON s.idGender = g.idGender " +
                "LEFT JOIN tblOlympicRecord r ON s.idSport = r.idSport " +
                "WHERE s.idSport = ?";

        // Executa a consulta SQL com o idSport fornecido
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        if (rs != null && rs.next()) {
            int idSportResult = rs.getInt("idSport");
            String type = rs.getString("type");
            int idGender = rs.getInt("idGender");
            String name = rs.getString("name");
            String description = rs.getString("description");
            int minParticipants = rs.getInt("minParticipants");
            String scoringMeasure = rs.getString("scoringMeasure");
            String oneGame = rs.getString("oneGame");
            // Criar o objeto Gender usando os dados da consulta
            String genderDescription = rs.getString("genderDescription");


            // Carregar o recorde olímpico associado ao esporte
            OlympicRecord olympicRecord = OlympicRecordDao.getOlympicRecordById(idSportResult, rs.getInt("olympicYear"));

            // Carregar os vencedores olímpicos associados ao esporte
            List<WinnerOlympic> winnerOlympics = WinnerOlympicDao.getWinnerOlympicsBySport(idSportResult);

            // Carregar as regras associadas ao esporte
            List<Rule> rules = RuleDao.getRulesBySport(idSportResult);

            // Criar e retornar o objeto Sport com todos os dados carregados
            return new Sport(idSportResult, type, idGender, name, description, minParticipants, scoringMeasure, oneGame, olympicRecord, winnerOlympics, rules);
        }

        // Caso não encontre o esporte, retorna null
        return null;
    }
}