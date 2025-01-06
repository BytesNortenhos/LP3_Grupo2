package Dao;

import Models.Country;
import Utils.ConnectionsUtlis;
import Models.Event;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDao {
    /**
     * Get all events
     * @return List<Event>
     * @throws SQLException
     */
    public List<Event> getEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery("SELECT e.year, e.logo, " +
                "c.idCountry, c.name AS countryName, c.continent " +
                "FROM tblEvent e " +
                "INNER JOIN tblCountry c ON e.idCountry = c.idCountry " +
                "WHERE e.status=1 " +
                "ORDER BY e.year DESC;");
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
     * Adds a new event to the database.
     *
     * @param event {Event} Event
     * @return boolean
     * @throws SQLException
     */
    public boolean addEvent(Event event) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM tblEvent WHERE year = ?";
        String insertQuery = "INSERT INTO tblEvent (year, idCountry, Logo, status) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(checkQuery);
            stmt.setInt(1, event.getYear());
            rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }


            stmt = conn.prepareStatement(insertQuery);
            stmt.setInt(1, event.getYear());
            stmt.setString(2, event.getCountry().getIdCountry());
            stmt.setString(3, event.getLogo());
            stmt.setInt(4, event.getStatus());
            stmt.executeUpdate();

            return true;

        } finally {
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



    /**
     * Remove event
     * @param year {int} Year
     * @throws SQLException
     */
    public void removeEvent(int year) throws SQLException {
        String query = "DELETE FROM tblEvent WHERE year = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
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
    public void updateEvent(Event event) throws SQLException {
        String query = "UPDATE tblEvent SET idCountry = ?, Logo = ?, status = ? WHERE year = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, event.getCountry().getIdCountry());
            stmt.setString(2, event.getLogo());
            stmt.setInt(3, event.getStatus());
            stmt.setInt(4, event.getYear());
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
    public Event getEventByYear(int year) throws SQLException {
        String query = "SELECT e.year, e.logo, e.status, " +
                "c.idCountry, c.name AS countryName, c.continent " +
                "FROM tblEvent e " +
                "INNER JOIN tblCountry c ON e.idCountry = c.idCountry " +
                "WHERE e.year = ?;";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, year);
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
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
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

    public List<List> getEventsToShow() throws SQLException {
        List<List> events = new ArrayList<>();
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery("SELECT e.year, e.logo, " +
                "c.idCountry, c.name AS countryName, c.continent " +
                "FROM tblEvent e " +
                "INNER JOIN tblCountry c ON e.idCountry = c.idCountry " +
                "ORDER BY e.year DESC;");
        if (rs != null) {
            while (rs.next()) {
                List<String> event = new ArrayList<>();
                event.add(rs.getString("year"));
                event.add(rs.getString("countryName"));
                event.add(rs.getString("continent"));
                event.add(rs.getString("logo"));
                events.add(event);
            }
        } else {
            System.out.println("ResultSet is null. No results for Event found.");
        }
        return events;
    }

    public boolean getIfLocals(int year) throws SQLException {
        String query = "SELECT COUNT(*) FROM tblLocal WHERE event = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, year);
        if (rs != null && rs.next() && rs.getInt(1) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getActualYear() throws SQLException {
        String query = "SELECT year FROM tblEvent WHERE status = 1";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query);
        if (rs != null && rs.next()) {
            return rs.getInt(1);
        } else {
            return 0;
        }
    }
    public List<Integer> getEventsToStart() throws SQLException {
        List<Integer> years = new ArrayList<>();
        String query = "SELECT year FROM tblEvent WHERE status = 0";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query);
        if (rs != null) {
            while (rs.next()) {
                years.add(rs.getInt("year"));
            }
        }
        return years;
    }

    public void startNewEvent(int currentEvent, int selectedEvent) throws SQLException {
        String query1 = "UPDATE tblEvent SET status = 2 WHERE year = ?";
        String query2 = "UPDATE tblEvent SET status = 1 WHERE year = ?";
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt1 = conn.prepareStatement(query1);
            stmt2 = conn.prepareStatement(query2);
            stmt1.setInt(1, currentEvent);
            stmt1.executeUpdate();
            stmt2.setInt(1, selectedEvent);
            stmt2.executeUpdate();
        } finally {
            if (stmt1 != null && stmt2 != null) {
                stmt1.close();
                stmt2.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

}