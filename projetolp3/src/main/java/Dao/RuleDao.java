package Dao;

import Utils.ConnectionsUtlis;
import models.Rule;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RuleDao {
    public static List<Rule> getRules() throws SQLException {
        List<Rule> rules = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblRule;");
        if (rs != null) {
            while (rs.next()) {
                int idRule = rs.getInt("idRule");
                int idSport = rs.getInt("idSport");
                String description = rs.getString("description");

                Rule rule = new Rule(idRule, idSport, description);
                rules.add(rule);
            }
        } else {
            System.out.println("ResultSet is null. No results for Rule found.");
        }
        return rules;
    }

    public static void addRule(Rule rule) throws SQLException {
        String query = "INSERT INTO tblRule (idSport, description) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, rule.getIdSport());
            stmt.setString(2, rule.getDescription());
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

    public static void updateRule(Rule rule) throws SQLException {
        String query = "UPDATE tblRule SET idSport = ?, description = ? WHERE idRule = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, rule.getIdSport());
            stmt.setString(2, rule.getDescription());
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

    public static Rule getRuleById(int idRule) throws SQLException {
        String query = "SELECT * FROM tblRule WHERE idRule = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idRule);
        if (rs != null && rs.next()) {
            int idSport = rs.getInt("idSport");
            String description = rs.getString("description");
            return new Rule(idRule, idSport, description);
        }
        return null;
    }
}
