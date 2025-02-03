package Dao;
import Models.SportEvent;
import Utils.ConnectionsUtlis;
import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SportEventDao {
    /**
     * Get all SportEvents
     *
     * @return List<SportEvent>
     * @throws SQLException
     */
    public List<SportEvent> getSportEvents() throws SQLException {
        List<SportEvent> sportEvents = new ArrayList<>();
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery("SELECT * FROM tblSportEvent;");
        if (rs != null) {
            while (rs.next()) {
                int idSport = rs.getInt("idSport");
                int year = rs.getInt("year");
                String guidApi = rs.getString("guidApi");

                SportEvent sportEvent = new SportEvent(idSport, year, guidApi);
                sportEvents.add(sportEvent);
            }
        } else {
            System.out.println("ResultSet is null. No results for SportEvents found.");
        }
        return sportEvents;
    }

    /**
     * Add SportEvent
     *
     * @param sportEvent {SportEvent} Sport Event
     * @throws SQLException
     */
    public int addSportEvent(SportEvent sportEvent) throws SQLException {
        String insertQuery = "INSERT INTO tblSportEvent (idSport, year) VALUES (?, ?)";
        String maxIdQuery = "SELECT MAX(id) AS max_id FROM tblSportEvent";
        Connection conn = null;
        PreparedStatement insertStmt = null;
        PreparedStatement maxIdStmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            conn.setAutoCommit(false);

            insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setInt(1, sportEvent.getIdSport());
            insertStmt.setInt(2, sportEvent.getYear());
            insertStmt.executeUpdate();

            maxIdStmt = conn.prepareStatement(maxIdQuery);
            ResultSet rs = maxIdStmt.executeQuery();
            if (rs.next()) {
                int maxId = rs.getInt("max_id");
                conn.commit();
                return maxId;
            } else {
                conn.rollback();
                throw new SQLException("Erro ao obter ID adicionado.");
            }
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (insertStmt != null) {
                insertStmt.close();
            }
            if (maxIdStmt != null) {
                maxIdStmt.close();
            }
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Remove SportEvent
     *
     * @param idSport {int} Sport ID
     * @param year {int} Year
     * @throws SQLException
     */
    public void removeSportEvent(int idSport, int year) throws SQLException {
        String query = "DELETE FROM tblSportEvent WHERE idSport = ? AND year = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idSport);
            stmt.setInt(2, year);
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
     * Update SportEvent
     *
     * @param sportEvent {SportEvent} SportEvent
     * @throws SQLException
     */
    public void updateSportEvent(SportEvent sportEvent) throws SQLException {
        String query = "UPDATE tblSportEvent SET idSport = ? WHERE year = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, sportEvent.getIdSport());
            stmt.setInt(2, sportEvent.getYear());
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
     * Get SportEvent by Sport ID
     *
     * @param idSport {int} Sport ID
     * @return SportEvent
     * @throws SQLException
     */
    public List<SportEvent> getSportEventsByIdSport(int idSport) throws SQLException {
        List<SportEvent> sportEvents = new ArrayList<>();
        String query = "SELECT * FROM tblSportEvent WHERE idSport = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idSport);
        if (rs != null && rs.next()) {
            sportEvents.add(new SportEvent(rs.getInt("idSport"), rs.getInt("year"), rs.getString("guidApi")));
        }
        return sportEvents;
    }

    /**
     * Get SportEvent by Year
     *
     * @param year {int} Year
     * @return SportEvent
     * @throws SQLException
     */
    public List<SportEvent> getSportEventsByYear(int year) throws SQLException {
        List<SportEvent> sportEvents = new ArrayList<>();
        String query = "SELECT * FROM tblSportEvent WHERE year = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, year);
        if (rs != null) {
            while (rs.next()) {
                int idSport = rs.getInt("idSport");
                int yearFromDb = rs.getInt("year");
                String guidApi = rs.getString("guidApi");
                sportEvents.add(new SportEvent(idSport, yearFromDb, guidApi));
            }
        }
        return sportEvents;
    }

    public boolean updateGuidApi(String guidApi, int eventId) throws SQLException {
        String query = "UPDATE tblSportEvent SET guidApi = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, guidApi);
            stmt.setInt(2, eventId);
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
