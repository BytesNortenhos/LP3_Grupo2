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
    public void insertIntoTeamList(int athleteId, int teamId, int statusId, int year) throws SQLException {
        // Consulta para verificar se já existe um registro duplicado
        String checkQuery = "SELECT COUNT(*) FROM tblTeamList WHERE idAthlete = ? AND idTeam = ? AND idStatus = ? AND year = ?";

        // Query para inserir o novo registro
        String insertQuery = "INSERT INTO tblTeamList (idTeam, idAthlete, idStatus, year, isActive) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionsUtlis.dbConnect();  // Estabelece a conexão com o banco de dados

            // Verifica se já existe um registro duplicado
            checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, athleteId);  // Define o id do atleta
            checkStmt.setInt(2, teamId);     // Define o id da equipe
            checkStmt.setInt(3, statusId);   // Define o id do status
            checkStmt.setInt(4, year);       // Define o ano do evento

            // Executa a consulta para verificar a duplicidade
            rs = checkStmt.executeQuery();

            if (rs != null && rs.next()) {
                int count = rs.getInt(1);  // Obtém o número de registros encontrados
                if (count > 0) {
                    System.out.println("A inscrição já existe na lista de equipes!");  // Exibe mensagem de duplicidade
                    return;  // Sai do método sem fazer a inserção
                }
            }

            // Se não houver duplicidade, procede com a inserção
            insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setInt(1, teamId);      // Define o id da equipe
            insertStmt.setInt(2, athleteId);   // Define o id do atleta
            insertStmt.setInt(3, statusId);    // Define o id do status
            insertStmt.setInt(4, year);        // Define o ano do evento
            insertStmt.setBoolean(5, true);    // Define isActive como true (inscrição ativa)

            insertStmt.executeUpdate();  // Executa a inserção
            System.out.println("Inscrição na lista de equipes realizada com sucesso!");  // Mensagem de sucesso após a inserção

        } finally {
            // Fechando os recursos
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
    }   }

//public void insertIntoTeamList(int athleteId, int teamId, int statusId, int year) throws SQLException {
//        // Consulta para verificar duplicidade na tblTeamList
//        String checkQuery = "SELECT COUNT(*) FROM tblTeamList WHERE idAthlete = ? AND idTeam = ? AND idStatus = ? AND year = ?";
//
//        // Consulta para obter idSport e minParticipantes a partir do idTeam
//        String getSportAndMinQuery = "SELECT t.idSport, s.minParticipants FROM tblTeam t INNER JOIN tblSport s ON t.idSport = s.idSport WHERE t.idTeam = ?";
//
//        // Consulta para contar registros com idStatus = 3 para o teamId
//        String countStatusQuery = "SELECT COUNT(*) FROM tblTeamList WHERE idTeam = ? AND idStatus = 3";
//
//        // Consulta para inserir novo registro na tblTeamList
//        String insertQuery = "INSERT INTO tblTeamList (idTeam, idAthlete, idStatus, year, isActive) VALUES (?, ?, ?, ?, ?)";
//
//        // Consulta para inserir registro na tblRegistration
//        String insertRegistrationQuery = "INSERT INTO tblRegistration (idTeam, idSport, idStatus, year) VALUES (?, ?, ?, ?)";
//
//        Connection conn = null;
//        PreparedStatement checkStmt = null;
//        PreparedStatement getSportAndMinStmt = null;
//        PreparedStatement countStatusStmt = null;
//        PreparedStatement insertStmt = null;
//        PreparedStatement insertRegistrationStmt = null;
//        ResultSet rs = null;
//
//        try {
//            conn = ConnectionsUtlis.dbConnect();
//
//            // Obtém o idSport e minParticipantes para o teamId
//            getSportAndMinStmt = conn.prepareStatement(getSportAndMinQuery);
//            getSportAndMinStmt.setInt(1, teamId);
//            rs = getSportAndMinStmt.executeQuery();
//            int idSport = 0;
//            int minParticipantes = 0;
//            if (rs != null && rs.next()) {
//                idSport = rs.getInt("idSport");
//                minParticipantes = rs.getInt("minParticipants");
//            } else {
//                System.out.println("Esporte ou minParticipantes não encontrado para o time.");
//                return;
//            }
//
//            // Conta os registros com idStatus = 3 para o time
//            countStatusStmt = conn.prepareStatement(countStatusQuery);
//            countStatusStmt.setInt(1, teamId);
//            rs = countStatusStmt.executeQuery();
//            int countStatus = 0;
//            if (rs != null && rs.next()) {
//                countStatus = rs.getInt(1);
//            }
//
//            // Verifica se o número de participantes já atingiu o limite
//            if (countStatus >= minParticipantes) {
//                System.out.println("Equipa cheia! Nenhum registro foi inserido.");
//                return;
//            }
//
//            // Verifica duplicidade na tblTeamList
//            checkStmt = conn.prepareStatement(checkQuery);
//            checkStmt.setInt(1, athleteId);
//            checkStmt.setInt(2, teamId);
//            checkStmt.setInt(3, statusId);
//            checkStmt.setInt(4, year);
//            rs = checkStmt.executeQuery();
//            if (rs != null && rs.next()) {
//                int count = rs.getInt(1);
//                if (count > 0) {
//                    System.out.println("A inscrição já existe na lista de equipes!");
//                    return;
//                }
//            }
//
//            // Insere novo registro na tblTeamList
//            insertStmt = conn.prepareStatement(insertQuery);
//            insertStmt.setInt(1, teamId);
//            insertStmt.setInt(2, athleteId);
//            insertStmt.setInt(3, statusId);
//            insertStmt.setInt(4, year);
//            insertStmt.setBoolean(5, true);
//            insertStmt.executeUpdate();
//            System.out.println("Inscrição na lista de equipes realizada com sucesso!");
//
//            // Atualiza a contagem após inserção
//            countStatus++;
//
//            // Se a contagem atingir exatamente minParticipantes, insere na tblRegistration
//            if (countStatus == minParticipantes) {
//                insertRegistrationStmt = conn.prepareStatement(insertRegistrationQuery);
//                insertRegistrationStmt.setInt(1, teamId);
//                insertRegistrationStmt.setInt(2, idSport);
//                insertRegistrationStmt.setInt(3, 1); // idStatus = 2 (ou outro valor especificado)
//                insertRegistrationStmt.setInt(4, year);
//                insertRegistrationStmt.executeUpdate();
//                System.out.println("Registro inserido na tblRegistration com sucesso!");
//            }
//
//        } finally {
//            // Fecha recursos
//            if (rs != null) rs.close();
//            if (checkStmt != null) checkStmt.close();
//            if (getSportAndMinStmt != null) getSportAndMinStmt.close();
//            if (countStatusStmt != null) countStatusStmt.close();
//            if (insertStmt != null) insertStmt.close();
//            if (insertRegistrationStmt != null) insertRegistrationStmt.close();
//            if (conn != null) conn.close();
//        }
//    } }
