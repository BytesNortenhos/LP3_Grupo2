package Dao;

import Models.Athlete;
import Models.MedalType;
import Models.Team;
import Utils.ConnectionsUtlis;
import Models.Medal;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedalDao {
    /**
     * Get all medals
     * @return {List<Medal>} List of medals
     * @throws SQLException
     */
    public List<Medal> getMedals() throws SQLException {
        List<Medal> medals = new ArrayList<>();
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(
                "SELECT m.idMedal, m.idAthlete, m.idTeam, m.year," +
                        "mt.idMedalType, mt.descMedalType AS medalTypeDescription " +
                        "FROM tblMedal m " +
                        "INNER JOIN tblMedalType mt ON m.idMedalType = mt.idMedalType;");
        if (rs != null) {
            while (rs.next()) {
                int idMedal = rs.getInt("idMedal");
                int idAthlete = rs.getInt("idAthlete");
                int idTeam = rs.getInt("idTeam");
                int year = rs.getInt("year");
                MedalType medalType = new MedalType(rs.getInt("idMedalType"), rs.getString("medalTypeDescription"));
                AthleteDao athleteDao = new AthleteDao();
                Athlete athlete = athleteDao.getAthleteById(idAthlete);
                TeamDao teamDao = new TeamDao();
                Team team = teamDao.getTeamByIdV2(idTeam);
                Medal medal = new Medal(idMedal, athlete, team, year, medalType);
                medals.add(medal);
            }
        } else {
            System.out.println("ResultSet is null. No results for Medal found.");
        }
        return medals;
    }

    /**
     * Get gold medals by athlete id
     * @param idAthlete {int} Athlete id
     * @return {int} Gold medals
     * @throws SQLException
     */
    public int countGoldMedals(int idAthlete) throws SQLException {
    int quantidade = 0;
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(
                "SELECT COUNT(*) AS quantidade FROM tblMedal WHERE idAthlete = ? AND idMedalType = 1;", idAthlete);
        if (rs != null && rs.next()) {
            quantidade = rs.getInt("quantidade");
        }
        return quantidade;
    }

    /**
     * Get silver medals by athlete id
     * @param idAthlete {int} Athlete id
     * @return {int} Silver medals
     * @throws SQLException
     */
    public int countSilverMedals(int idAthlete) throws SQLException {
        int quantidade = 0;
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(
                "SELECT COUNT(*) AS quantidade FROM tblMedal WHERE idAthlete = ? AND idMedalType = 2;", idAthlete);
        if (rs != null && rs.next()) {
            quantidade = rs.getInt("quantidade");
        }
        return quantidade;
    }

    /**
     * Get bronze medals by athlete id
     * @param idAthlete {int} Athlete id
     * @return {int} Bronze medals
     * @throws SQLException
     */
    public int countBronzeMedals(int idAthlete) throws SQLException {
        int quantidade = 0;
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(
                "SELECT COUNT(*) AS quantidade FROM tblMedal WHERE idAthlete = ? AND idMedalType = 3;", idAthlete);
        if (rs != null && rs.next()) {
            quantidade = rs.getInt("quantidade");
        }
        return quantidade;
    }

    /**
     * Get certificate medals by athlete id
     * @param idAthlete {int} Athlete id
     * @return {int} Certificate medals
     * @throws SQLException
     */
    public int countCertificate(int idAthlete) throws SQLException {
        int quantidade = 0;
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(
                "SELECT COUNT(*) AS quantidade FROM tblMedal WHERE idAthlete = ? AND idMedalType = 4;", idAthlete);
        if (rs != null && rs.next()) {
            quantidade = rs.getInt("quantidade");
        }
        return quantidade;
    }

    /**
     * Add medal to database
     * @param medal {Medal} Medal
     * @throws SQLException
     */
    public void addMedal(Medal medal) throws SQLException {
        String query = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, medal.getAthlete().getIdAthlete());
            stmt.setInt(2, medal.getTeam().getIdTeam());
            stmt.setInt(3, medal.getYear());
            stmt.setInt(4, medal.getMedalType().getId());
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
     * Remove medal from database
     * @param idMedal {int} Medal id
     * @throws SQLException
     */
    public static void removeMedal(int idMedal) throws SQLException {
        String query = "DELETE FROM tblMedal WHERE idMedal = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idMedal);
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
     * Update medal
     * @param medal {Medal} Medal
     * @throws SQLException
     */
    public void updateMedal(Medal medal) throws SQLException {
        String query = "UPDATE tblMedal SET idAthlete = ?, idTeam = ?, year = ?, idMedalType = ? WHERE idMedal = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, medal.getAthlete().getIdAthlete());
            stmt.setInt(2, medal.getTeam().getIdTeam());
            stmt.setInt(3, medal.getYear());
            stmt.setInt(4, medal.getMedalType().getId());
            stmt.setInt(5, medal.getIdMedal());
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
     * Get medal by id
     * @param idMedal {int} Medal id
     * @return {Medal} Medal
     * @throws SQLException
     */
    public static Medal getMedalById(int idMedal) throws SQLException {
        String query = "SELECT m.idMedal, m.idAthlete, m.idTeam, m.year, " +
                "mt.idMedalType, mt.description AS medalTypeDescription " +
                "FROM tblMedal m " +
                "INNER JOIN tblMedalType mt ON m.idMedalType = mt.idMedalType " +
                "WHERE m.idMedal = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idMedal);
        if (rs != null && rs.next()) {
            int idAthlete = rs.getInt("idAthlete");
            int idTeam = rs.getInt("idTeam");
            int year = rs.getInt("year");
            AthleteDao athleteDao = new AthleteDao();
            Athlete athlete = athleteDao.getAthleteById(idAthlete);
            Team team = TeamDao.getTeamById(idTeam);
            MedalType medalType = new MedalType(rs.getInt("idMedalType"), rs.getString("medalTypeDescription"));
            return new Medal(idMedal, athlete, team, year, medalType);
        }
        return null;
    }

    /**
     * Get medals by athlete id
     * @param idAthlete {int} Athlete id
     * @return {List<Medal>} List of medals
     * @throws SQLException
     */
    public static List<Medal> getMedalsByAthleteId(int idAthlete) throws SQLException {
        List<Medal> medals = new ArrayList<>();
        String query = "SELECT m.idMedal, m.idAthlete, m.idTeam, m.year, " +
                "mt.idMedalType, mt.descMedalType AS medalTypeDescription " +
                "FROM tblMedal m " +
                "INNER JOIN tblMedalType mt ON m.idMedalType = mt.idMedalType " +
                "WHERE m.idAthlete = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idAthlete);

        if (rs != null) {
            while (rs.next()) {
                int idMedal = rs.getInt("idMedal");
                int idTeam = rs.getInt("idTeam");
                int year = rs.getInt("year");
                MedalType medalType = new MedalType(rs.getInt("idMedalType"), rs.getString("medalTypeDescription"));
                Team team = TeamDao.getTeamById(idTeam);
                AthleteDao athleteDao = new AthleteDao();
                Athlete athlete = athleteDao.getAthleteById(idAthlete);
                Medal medal = new Medal(idMedal, athlete, team, year, medalType);
                medals.add(medal);
            }
        }
        return medals;
    }

    /**
     * Add top medal athlete
     * @param idAthelete {int} Athlete id
     * @param year {int} Year
     * @param idMedalType {int} Medal type id
     * @throws SQLException
     */
    public void addTopMedalAthlete(int idAthelete, int year, int idMedalType) throws SQLException {
        String query = "INSERT INTO tblMedal (idAthlete, year, idMedalType) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, idAthelete);
            stmt.setInt(2, year);
            stmt.setInt(3, idMedalType);
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
     * Add top medal team
     * @param idTeam {int} Team id
     * @param year {int} Year
     * @param idMedalType {int} Medal type id
     * @throws SQLException
     */
    public void addTopMedalTeam(int idTeam, int year, int idMedalType) throws SQLException {
        String query = "INSERT INTO tblMedal (idTeam, year, idMedalType) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, idTeam);
            stmt.setInt(2, year);
            stmt.setInt(3, idMedalType);
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
     * Add top medal athlete team
     * @param idAthlete {int} Athlete id
     * @param idTeam {int} Team id
     * @param year {int} Year
     * @param idMedalType {int} Medal type id
     * @throws SQLException
     */
    public void addTopMedalAthleteTeam(int idAthlete, int idTeam, int year, int idMedalType) throws SQLException {
        String query = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, idAthlete);
            stmt.setInt(2, idTeam);
            stmt.setInt(3, year);
            stmt.setInt(4, idMedalType);
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
    public static int getMedalsByTeamId(int idTeam) throws SQLException {
        String query = "SELECT COUNT(DISTINCT m.idMedalType) AS totalMedals " +
                "FROM tblMedal m " +
                "INNER JOIN tblMedalType mt ON m.idMedalType = mt.idMedalType " +
                "WHERE m.idTeam = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idTeam);

        int totalMedals = 0;

        if (rs != null && rs.next()) {
            totalMedals = rs.getInt("totalMedals");
        }

        return totalMedals;
    }


}
