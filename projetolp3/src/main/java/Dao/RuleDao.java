package Dao;

import Models.Sport;
import Utils.ConnectionsUtlis;
import Models.Rule;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RuleDao {
    /**
     * Get all rules
     * @return {List<Rule>} List of rules
     * @throws SQLException
     */
    public static List<Rule> getRules() throws SQLException {
        List<Rule> rules = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblRule;");
        if (rs != null) {
            while (rs.next()) {
                int idRule = rs.getInt("idRule");
                int idSport = rs.getInt("idSport");
                String description = rs.getString("description");

                SportDao sportDao = new SportDao();
                Sport sport = sportDao.getSportById(idSport);

                Rule rule = new Rule(idRule, sport, description);
                rules.add(rule);
            }
        } else {
            System.out.println("ResultSet is null. No results for Rule found.");
        }
        return rules;
    }

    /**
     * Add rule
     * @param rule {Rule} Rule
     * @throws SQLException
     */
    public static void addRule(Rule rule) throws SQLException {
        String query = "INSERT INTO tblRule (idSport, description) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            // Agora estamos acessando diretamente o idSport da Rule
            stmt.setInt(1, rule.getIdSport());
            stmt.setString(2, rule.getDesc());
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
     * Remove rule
     * @param idRule {int} Id rule
     * @throws SQLException
     */
    public static void removeRule(int idRule) throws SQLException {
        String query = "DELETE FROM tblRule WHERE idRule = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idRule);
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
     * Update rule
     * @param rule {Rule} Rule
     * @throws SQLException
     */
    public static void updateRule(Rule rule) throws SQLException {
        String query = "UPDATE tblRule SET idSport = ?, description = ? WHERE idRule = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, rule.getSport().getIdSport());
            stmt.setString(2, rule.getDesc());
            stmt.setInt(3, rule.getIdRule());
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
     * Get rule by id
     * @param idRule {int} Id rule
     * @return {Rule} Rule
     * @throws SQLException
     */
    public static Rule getRuleById(int idRule) throws SQLException {
        String query = "SELECT * FROM tblRule WHERE idRule = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idRule);
        if (rs != null && rs.next()) {
            int idSport = rs.getInt("idSport");
            String description = rs.getString("description");
            SportDao sportDao = new SportDao();
            Sport sport = sportDao.getSportById(idSport);
            return new Rule(idRule, sport, description);
        }
        return null;
    }

    /**
     * Get rules by sport
     * @param idSport {int} Id sport
     * @return {List<Rule>} List of rules
     * @throws SQLException
     */
    public static List<Rule> getRulesBySport(int idSport) throws SQLException {
        List<Rule> rules = new ArrayList<>();
        String query = "SELECT r.idRule, r.description FROM tblRule r WHERE r.idSport = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        while (rs.next()) {
            int idRule = rs.getInt("idRule");
            String description = rs.getString("description");
            SportDao sportDao = new SportDao();
            Sport sport = sportDao.getSportByIdV2(idSport);
            Rule rule = new Rule(idRule, sport, description);
            rules.add(rule);
        }
        return null;
    }

    /**
     * Get rules by sport V2
     * @param idSport {int} Id sport
     * @return {List<Rule>} List of rules
     * @throws SQLException
     */
    public static List<Rule> getRulesBySportV2(int idSport) throws SQLException {
        List<Rule> rules = new ArrayList<>();
        String query = "SELECT r.idRule, r.description FROM tblRule r WHERE r.idSport = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idSport);
        while (rs.next()) {
            int idRule = rs.getInt("idRule");
            String description = rs.getString("description");

            // InstÃ¢ncia de Rule com idSport
            Rule rule = new Rule(idRule, idSport, description);
            rules.add(rule);
        }
        return rules;  // Corrigido para retornar a lista de regras
    }

}