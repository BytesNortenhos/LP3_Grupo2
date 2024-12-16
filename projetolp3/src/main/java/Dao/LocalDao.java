package Dao;

import Models.Country;
import Models.Event;
import Utils.ConnectionsUtlis;
import Models.Local;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocalDao {
    /**
     * Get all locals
     * @return {List<Local>} List of locals
     * @throws SQLException
     */
    public List<Local> getLocals() throws SQLException {
        List<Local> locals = new ArrayList<>();
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery("""
                        
                SELECT l.idLocal, l.name, l.type, l.address, l.city, l.capacity, l.constructionYear,
                e.year AS eventYear, e.logo,
                c.idCountry, c.name AS countryName, c.continent
                FROM tblLocal l
                LEFT JOIN tblEvent e ON l.event = e.year
                LEFT JOIN tblCountry c ON e.idCountry = c.idCountry;
        """);
        if (rs != null) {
            while (rs.next()) {
                int idLocal = rs.getInt("idLocal");
                String name = rs.getString("name");
                String type = rs.getString("type");
                String address = rs.getString("address");
                String city = rs.getString("city");
                int capacity = rs.getInt("capacity");
                int constructionYear = rs.getInt("constructionYear");

                Event event = null;
                int eventYear = rs.getInt("eventYear");
                if (!rs.wasNull()) {
                    String idCountry = rs.getString("idCountry");
                    String countryName = rs.getString("countryName");
                    String continent = rs.getString("continent");
                    Country country = new Country(idCountry, countryName, continent);

                    String logo = rs.getString("logo");
                    event = new Event(eventYear, country, logo);
                }

                Local local = new Local(idLocal, name, type, address, city, capacity, constructionYear, event);
                locals.add(local);
            }
        } else {
            System.out.println("ResultSet is null. No results for Local found.");
        }

        return locals;
    }



    /**
     * Get locals by year
     * @param year {int} Year
     * @return {List<Local>} List of locals
     * @throws SQLException
     */
    public List<Local> getLocalsByYear(int year) throws SQLException{
        List<Local> locals = new ArrayList<>();
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery("SELECT * FROM tblLocal WHERE event = ?;", year);
        if (rs != null) {
            while (rs.next()) {
                int idLocal = rs.getInt("idLocal");
                String name = rs.getString("name");
                String type = rs.getString("type");
                String address = rs.getString("address");
                String city = rs.getString("city");
                int capacity = rs.getInt("capacity");
                int constructionYear = rs.getInt("constructionYear");

                Local local = new Local(idLocal, name, type, address, city, capacity, constructionYear);
                locals.add(local);
            }
        } else {
            System.out.println("ResultSet is null. No results for Local found.");
        }
        return locals;
    }

    /**
     * Add local
     * @param local {Local} Local
     * @throws SQLException
     */
    public boolean addLocal(Local local) throws SQLException {
        String query = "INSERT INTO tblLocal (name, type, address, city, capacity, constructionYear, event) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, local.getName());
            stmt.setString(2, local.getType());
            stmt.setString(3, local.getAddress());
            stmt.setString(4, local.getCity());
            stmt.setInt(5, local.getCapacity());
            stmt.setInt(6, local.getConstructionYear());
            stmt.setInt(7, local.getEventObject().getYear());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
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
     * Remove local
     * @param idLocal {int} Local ID
     * @throws SQLException
     */
    public void removeLocal(int idLocal) throws SQLException {
        String query = "DELETE FROM tblLocal WHERE idLocal = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idLocal);
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
     * Update local
     * @param local {Local} Local
     * @throws SQLException
     */
    public boolean updateLocal(Local local) throws SQLException {
        String query = "UPDATE tblLocal SET name = ?, type = ?, address = ?, city = ?, capacity = ?, constructionYear = ? " +
                "WHERE idLocal = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, local.getName());
            stmt.setString(2, local.getType());
            stmt.setString(3, local.getAddress());
            stmt.setString(4, local.getCity());
            stmt.setInt(5, local.getCapacity());
            stmt.setInt(6, local.getConstructionYear());
            stmt.setInt(7, local.getIdLocal());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
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
     * Get local by ID
     * @param idLocal {int} Local ID
     * @return {Local} Local
     * @throws SQLException
     */
    public static Local getLocalById(int idLocal) throws SQLException {
        String query = "SELECT * FROM tblLocal WHERE idLocal = ?";
        ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
        CachedRowSet rs = connectionsUtlis.dbExecuteQuery(query, idLocal);
        if (rs != null && rs.next()) {
            String name = rs.getString("name");
            String type = rs.getString("type");
            String address = rs.getString("address");
            String city = rs.getString("city");
            int capacity = rs.getInt("capacity");
            int constructionYear = rs.getInt("constructionYear");
            return new Local(idLocal, name, type, address, city, capacity, constructionYear);
        }
        return null;
    }
}
