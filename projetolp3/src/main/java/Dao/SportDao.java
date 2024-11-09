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

                OlympicRecord olympicRecord = OlympicRecordDao.getOlympicRecordById(idSport, rs.getInt("olympicYear"));

                List<WinnerOlympic> winnerOlympics = WinnerOlympicDao.getWinnerOlympicsBySport(idSport);
                List<Rule> rules = RuleDao.getRulesBySport(idSport);

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
        String query = "SELECT s.type, s.idGender, s.name, s.description, s.minParticipants, s.scoringMeasure, s.oneGame," +
                "g.description AS genderDescription," +
                "orc.year AS olympicYear, orc.idAthlete AS olympicAthlete," +
                "orc.idTeam AS olympicTeam, orc.timeMS AS olympicTime, orc.medals AS olympicMedals," +
                "w.year AS winnerYear, w.idAthlete AS winnerAthlete," +
                "w.idTeam AS winnerTeam, w.timeMS AS winnerTime, w.idMedal AS winnerMedal," +
                "mt.descMedalType AS medalTypeDescription," +
                "r.idRule, r.description AS ruleDescription" +
                "FROM tblSport s" +
                "LEFT JOIN tblGender g ON s.idGender = g.idGender" +
                "LEFT JOIN tblOlympicRecord orc ON s.idSport = orc.idSport" +
                "LEFT JOIN tblWinnerOlympic w ON s.idSport = w.idSport" +
                "LEFT JOIN tblMedal m ON w.idMedal = m.idMedal" +
                "LEFT JOIN tblMedalType mt ON m.idMedalType = mt.idMedalType" +
                "LEFT JOIN tblRule r ON s.idSport = r.idSport" +
                "WHERE s.idSport = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);/*
        if (rs != null && rs.next()) {
            String type = rs.getString("type");
            int idGender = rs.getInt("idGender");
            String name = rs.getString("name");
            String description = rs.getString("description");
            int minParticipants = rs.getInt("minParticipants");
            String scoringMeasure = rs.getString("scoringMeasure");
            String oneGame = rs.getString("oneGame");
            String genderDescription = rs.getString("genderDescription");
            Gender gender = new Gender(idGender, genderDescription);
            OlympicRecord olympicRecord = new OlympicRecord(idSport, rs.getInt("olympicYear"), rs.getInt("olympicAthlete"),
                    rs.getInt("olympicTeam"), rs.getInt("olympicTime"), rs.getInt("olympicMedals"));

            List<WinnerOlympic> winnerOlympic = new ArrayList<>();
            do {
                int winnerMedalId = rs.getInt("winnerMedal");
                String medalTypeDescription = rs.getString("medalTypeDescription");
                MedalType medalType = new MedalType(winnerMedalId, medalTypeDescription);
                winnerOlympic.add(new WinnerOlympic(idSport, rs.getInt("winnerYear"), rs.getInt("winnerAthlete"),
                        rs.getInt("winnerTeam"), rs.getInt("winnerTime"), new Medal(winnerMedalId, rs.getInt("winnerAthlete"),
                        rs.getInt("winnerTeam"), rs.getInt("winnerYear"), medalType)));
            } while (rs.next() && rs.getInt("idSport") == idSport);

            List<Rule> rules = new ArrayList<>();
            do {
                rules.add(new Rule(rs.getInt("idRule"), idSport, rs.getString("ruleDescription")));
            } while (rs.next() && rs.getInt("idSport") == idSport);

            return new Sport(idSport, type, gender, name, description, minParticipants, scoringMeasure, oneGame, olympicRecord, winnerOlympic, rules);
        }*/
        return null;
    }
}