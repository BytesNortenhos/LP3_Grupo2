package Dao;

import Utils.ConnectionsUtlis;
import models.Event;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDao {
    public static List<Event> getEvents() throws SQLException {
        List<Event> events = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblEvent;");
        if (rs != null) {
            while (rs.next()) {
                int year = rs.getInt("year");
                int idCountry = rs.getInt("idCountry");
                String logo = rs.getString("Logo");

                Event event = new Event(year, idCountry, logo);
                events.add(event);
            }
        } else {
            System.out.println("ResultSet is null. No results for Event found.");
        }
        return events;
    }

    public static void addEvent(Event event) throws SQLException {
        String query = "INSERT INTO tblEvent (year, idCountry, Logo) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, event.getYear());
            stmt.setInt(2, event.getIdCountry());
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

    public static void updateEvent(Event event) throws SQLException {
        String query = "UPDATE tblEvent SET idCountry = ?, Logo = ? WHERE year = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, event.getIdCountry());
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

    public static Event getEventByYear(int year) throws SQLException {
        String query = "SELECT * FROM tblEvent WHERE year = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, year);
        if (rs != null && rs.next()) {
            int idCountry = rs.getInt("idCountry");
            String logo = rs.getString("Logo");
            return new Event(year, idCountry, logo);
        }
        return null;
    }
}
