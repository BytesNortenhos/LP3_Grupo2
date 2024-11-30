package Dao;

import Models.Country;
import Utils.ConnectionsUtlis;
import Models.Event;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDao {
    /**
     * Get all events
     * @return List<Event>
     * @throws SQLException
     */
    public static List<Event> getEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT e.year, e.logo, " +
                "c.idCountry, c.name AS countryName, c.continent " +
                "FROM tblEvent e " +
                "INNER JOIN tblCountry c ON e.idCountry = c.idCountry;");
        if (rs != null) {
            while (rs.next()) {
                int year = rs.getInt("year");
                Country country = new Country(rs.getString("idCountry"), rs.getString("countryName"), rs.getString("continent"));
                String logo = rs.getString("Logo");

                Event event = new Event(year, country, logo);
                events.add(event);
            }
        } else {
            System.out.println("ResultSet is null. No results for Event found.");
        }
        return events;
    }

    /**
     * Add event
     * @param event {Event} Event
     * @throws SQLException
     */
    public static void addEvent(Event event) throws SQLException {
        String query = "INSERT INTO tblEvent (year, idCountry, Logo) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, event.getYear());
            stmt.setString(2, event.getCountry().getIdCountry());
            stmt.setString(3, event.getLogo());
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
     * Remove event
     * @param year {int} Year
     * @throws SQLException
     */
    public static void removeEvent(int year) throws SQLException {
        String query = "DELETE FROM tblEvent WHERE year = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, year);
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
     * Update event
     * @param event {Event} Event
     * @throws SQLException
     */
    public static void updateEvent(Event event) throws SQLException {
        String query = "UPDATE tblEvent SET idCountry = ?, Logo = ? WHERE year = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, event.getCountry().getIdCountry());
            stmt.setString(2, event.getLogo());
            stmt.setInt(3, event.getYear());
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
     * Get event by year
     * @param year {int} Year
     * @return Event
     * @throws SQLException
     */
    public static Event getEventByYear(int year) throws SQLException {
        String query = "SELECT e.year, e.logo, " +
                "c.idCountry, c.name AS countryName, c.continent " +
                "FROM tblEvent e " +
                "INNER JOIN tblCountry c ON e.idCountry = c.idCountry " +
                "WHERE e.year = ?;";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, year);
        if (rs != null && rs.next()) {
            Country country = new Country(rs.getString("idCountry"), rs.getString("countryName"), rs.getString("continent"));
            String logo = rs.getString("Logo");
            return new Event(year, country, logo);
        }
        return null;
    }

    /**
     * Update event image
     * @param year {int} Year
     * @param path {String} Path
     * @param extensao {String} Extension
     * @throws SQLException
     */
    public void updateEventImage(int year, String path, String extensao) throws SQLException {
        String query = "UPDATE tblEvent SET Logo = ? WHERE year = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, path + year + extensao);
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
}