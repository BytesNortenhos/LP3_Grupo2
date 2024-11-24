package Dao;

import Models.*;
import Utils.ConnectionsUtlis;
import java.sql.ResultSet;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SportDao {
    public List<Sport> getSports() throws SQLException {
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
    public List<List> getSportsToShow() throws SQLException {
        List<List> sports = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT s.*, " +
                "g.description AS genderDescription " + // Added space before FROM
                "FROM tblSport s " +
                "JOIN tblGender g ON s.idGender = g.idGender;");
        if (rs != null) {
            while (rs.next()) {
                List<String> sport = new ArrayList<>();
                sport.add(rs.getString("idSport"));
                sport.add(rs.getString("type"));
                sport.add(rs.getString("genderDescription"));
                sport.add(rs.getString("name"));
                sport.add(rs.getString("description"));
                sport.add(rs.getString("minParticipants"));
                sport.add(rs.getString("scoringMeasure"));
                sport.add(rs.getString("oneGame"));
                sports.add(sport);
            }
        } else {
            System.out.println("ResultSet is null. No results for Sport found.");
        }
        return sports;
    }
    public List<List> getSportsToStart(int year) throws SQLException {
        List<List> sports = new ArrayList<>();
        String query = "SELECT DISTINCT s.idSport, s.type, s.idGender, s.name, s.description," +
                " s.minParticipants, s.scoringMeasure, s.oneGame, r.idStatus, " +
                "g.description AS genderDescription " +
                "FROM tblSport s " +
                "JOIN tblGender g ON s.idGender = g.idGender " +
                "JOIN tblRegistration r ON s.idSport = r.idSport " +
                "WHERE r.idStatus >= 3 AND r.year = ?;";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, year);
        if (rs != null) {
            while (rs.next()) {
                List<String> sport = new ArrayList<>();
                sport.add(rs.getString("idSport"));
                sport.add(rs.getString("type"));
                sport.add(rs.getString("genderDescription"));
                sport.add(rs.getString("name"));
                sport.add(rs.getString("description"));
                sport.add(rs.getString("minParticipants"));
                sport.add(rs.getString("scoringMeasure"));
                sport.add(rs.getString("oneGame"));
                sport.add(rs.getString("idStatus"));
                sports.add(sport);
            }
        } else {
            System.out.println("ResultSet is null. No results for Sport found.");
        }
        return sports;
    }
    public int getNumberParticipantsSport(int idSport, int year) throws SQLException {
        String query = "SELECT COUNT(*) AS quantidade " +
                "FROM tblRegistration " +
                "WHERE idSport = ? " +
                "AND year = ? " +
                "AND (idStatus = 3 OR idStatus = 4) ;";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport, year);
        int quantidade = 0;
        if (rs != null && rs.next()) {
            quantidade = rs.getInt("quantidade");
        }
        return quantidade;
    }
    public boolean BootedSport(int idSport) throws SQLException {
        String query = "SELECT COUNT(*) AS quantidade " +
                "FROM tblResult " +
                "WHERE idSport = ?;";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        if (rs != null && rs.next()) {
            if (rs.getInt("quantidade") == 0) {
                return true;
            }
        }
        return false;
    }
    public static int addSport(Sport sport) throws SQLException {
        String query = "INSERT INTO tblSport (type, idGender, name, description, minParticipants, scoringMeasure, oneGame) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, sport.getType());
            stmt.setInt(2, sport.getGenre().getIdGender());
            stmt.setString(3, sport.getName());
            stmt.setString(4, sport.getDesc());
            stmt.setInt(5, sport.getMinParticipants());
            stmt.setString(6, sport.getScoringMeasure());
            stmt.setString(7, sport.getOneGame());

            // Executa a inserção
            stmt.executeUpdate();

            // Obter o ID gerado automaticamente
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1); // Retorna o ID gerado
                System.out.println("Generated Sport ID: " + generatedId); // Adicionando para depuração
                return generatedId;
            } else {
                throw new SQLException("Failed to retrieve the generated ID for the sport.");
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
        String query = "UPDATE tblSport SET type = ?, idGender = ?, name = ?, description = ?, minParticipants = ?, scoringMeasure = ?, oneGame = ?, resultMin = ?, resultMax = ? WHERE idSport = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            // Define os valores para a atualização
            stmt.setString(1, sport.getType());
            stmt.setInt(2, sport.getGenre().getIdGender());
            stmt.setString(3, sport.getName());
            stmt.setString(4, sport.getDesc());
            stmt.setInt(5, sport.getMinParticipants());
            stmt.setString(6, sport.getScoringMeasure());
            stmt.setString(7, sport.getOneGame());
            stmt.setInt(8, sport.getResultMin()); // Define o valor de resultMin
            stmt.setInt(9, sport.getResultMax()); // Define o valor de resultMax
            stmt.setInt(10, sport.getIdSport()); // Define o identificador do esporte

            // Executa a atualização
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

    public Sport getSportById(int idSport) throws SQLException {
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
            OlympicRecordDao olympicRecordDao = new OlympicRecordDao();
            OlympicRecord olympicRecord = olympicRecordDao.getOlympicRecordById(idSportResult, rs.getInt("olympicYear"));

            // Carregar os vencedores olímpicos associados ao esporte
            WinnerOlympicDao winnerOlympicDao = new WinnerOlympicDao();
            List<WinnerOlympic> winnerOlympics = winnerOlympicDao.getWinnerOlympicsBySport(idSportResult);

            // Carregar as regras associadas ao esporte
            List<Rule> rules = RuleDao.getRulesBySport(idSportResult);

            // Criar e retornar o objeto Sport com todos os dados carregados
            return new Sport(idSportResult, type, gender, name, description, minParticipants, scoringMeasure, oneGame, olympicRecord, winnerOlympics, rules);
        }

        // Caso não encontre o esporte, retorna null
        return null;
    }
    public Sport getSportByIdV2(int idSport) throws SQLException {
        // Consulta otimizada para obter os dados do esporte com seus registros olímpicos e gênero
        String query = """
        SELECT s.idSport, s.type, s.idGender, s.name, s.description, 
               s.minParticipants, s.scoringMeasure, s.oneGame, 
               g.description AS genderDescription, 
               r.year AS olympicYear, r.timeMS, r.medals 
        FROM tblSport s 
        JOIN tblGender g ON s.idGender = g.idGender 
        LEFT JOIN tblOlympicRecord r ON s.idSport = r.idSport 
        WHERE s.idSport = ?
    """;

        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        if (rs != null && rs.next()) {
            // Carrega os dados principais do esporte
            int idSportResult = rs.getInt("idSport");
            String type = rs.getString("type");
            int idGender = rs.getInt("idGender");
            String name = rs.getString("name");
            String description = rs.getString("description");
            int minParticipants = rs.getInt("minParticipants");
            String scoringMeasure = rs.getString("scoringMeasure");
            String oneGame = rs.getString("oneGame");

            // Carrega o registro olímpico diretamente
            OlympicRecord olympicRecord = OlympicRecordDao.getOlympicRecordByIdV2(idSportResult, rs.getInt("olympicYear"));

            // Busca lista de vencedores olímpicos e regras associadas ao esporte
            List<WinnerOlympic> winnerOlympics = WinnerOlympicDao.getWinnerOlympicsBySportV2(idSportResult);
            List<Rule> rules = RuleDao.getRulesBySportV2(idSportResult);

            // Retorna o objeto Sport com todos os dados carregados
            return new Sport(idSportResult, type, idGender, name, description,
                    minParticipants, scoringMeasure, oneGame, olympicRecord,
                    winnerOlympics, rules);
        }

        // Caso não encontre o esporte, retorna null
        return null;
    }

    public List<Sport> getSportsByName(String sportName) throws SQLException {
        List<Sport> sports = new ArrayList<>();
        // Consulta otimizada para pegar somente o nome e id do esporte
        String query = "SELECT idSport, name, type FROM tblSport WHERE name = ?";

        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, sportName);
        if (rs != null) {
            while (rs.next()) {
                int idSport = rs.getInt("idSport");
                String name = rs.getString("name");
                String type = rs.getString("type");
                Sport sport = new Sport(idSport, name, type); // Criando o objeto Sport apenas com id e nome
                sports.add(sport);
            }
        } else {
            System.out.println("No sports found with the specified name.");
        }
        return sports;
    }
    public static List<Sport> getAllSportsV2() throws SQLException {
        List<Sport> sports = new ArrayList<>();

        // Atualize a consulta SQL para incluir os novos campos resultMin e resultMax
        String query = "SELECT s.idSport, s.name, s.type, s.idGender, g.description AS genderDescription, " +
                "s.minParticipants, s.description AS sportDescription, s.scoringMeasure, s.oneGame, " +
                "s.resultMin, s.resultMax " +  // Incluindo os campos resultMin e resultMax
                "FROM tblSport s " +
                "INNER JOIN tblGender g ON s.idGender = g.idGender " +
                "LEFT JOIN tblTeam t ON s.idSport = t.idSport " +
                "LEFT JOIN tblRegistration r ON s.idSport = r.idSport " +
                "WHERE t.idSport IS NULL AND r.idSport IS NULL";  // Filtra para esportes sem registros nas tabelas tblTeam e tblRegistration

        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query);

        if (rs != null) {
            while (rs.next()) {
                int idSport = rs.getInt("idSport");
                String name = rs.getString("name");
                String type = rs.getString("type");
                int idGender = rs.getInt("idGender");
                String genderDescription = rs.getString("genderDescription");
                int minParticipants = rs.getInt("minParticipants");
                String sportDescription = rs.getString("sportDescription");
                String scoringMeasure = rs.getString("scoringMeasure");
                String oneGame = rs.getString("oneGame");
                int resultMin = rs.getInt("resultMin");  // Buscando o valor de resultMin
                int resultMax = rs.getInt("resultMax");  // Buscando o valor de resultMax

                // Criando o objeto Gender
                Gender gender = new Gender(idGender, genderDescription);

                // Criando o objeto Sport com todos os dados
                Sport sport = new Sport(idSport, name, type, gender, sportDescription, scoringMeasure);
                sport.setMinParticipants(minParticipants);
                sport.setOneGame(oneGame);  // Atribuindo o valor de oneGame
                sport.setResultMin(resultMin);  // Atribuindo o valor de resultMin
                sport.setResultMax(resultMax);  // Atribuindo o valor de resultMax

                sports.add(sport);
            }
        } else {
            System.out.println("ResultSet is null. No results for Sport found.");
        }

        return sports;
    }

    public static void updateSportV2(Sport sport) throws SQLException {
        String query = "UPDATE tblSport SET name = ?, description = ?, minParticipants = ? WHERE idSport = ?";

        try {
            // Obter conexão utilizando ConnectionsUtlis.dbConnect()
            Connection conn = ConnectionsUtlis.dbConnect();

            // Preparar a instrução SQL
            PreparedStatement stmt = conn.prepareStatement(query);

            // Configurar os parâmetros do PreparedStatement
            stmt.setString(1, sport.getName());
            stmt.setString(2, sport.getDesc());
            stmt.setInt(3, sport.getMinParticipants());
            stmt.setInt(4, sport.getIdSport());

            // Executar a atualização
            int rowsAffected = stmt.executeUpdate();

            // Informar o resultado
            if (rowsAffected > 0) {
                System.out.println("Sport updated successfully!");
            } else {
                System.out.println("No sport found with the provided ID.");
            }

            // Fechar o PreparedStatement
            stmt.close();

        } catch (SQLException e) {
            // Tratar exceção SQL
            System.err.println("Error updating sport: " + e.getMessage());
            throw e;
        }
    }}

