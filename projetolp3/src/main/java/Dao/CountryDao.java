package Dao;

import Utils.ConnectionsUtlis;
import Models.Country;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CountryDao {

    /**
     * Get all countries
     * @return List<Country>
     * @throws SQLException
     */
    public static List<Country> getCountries() throws SQLException {
        List<Country> countries = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM tblCountry;");
        if (rs != null) {
            while (rs.next()) {
                String idCountry = rs.getString("idCountry");
                String name = rs.getString("name");
                String continent = rs.getString("continent");

                Country country = new Country(idCountry, name, continent);
                countries.add(country);
            }
        } else {
            System.out.println("ResultSet is null. No results for Country found.");
        }
        return countries;
    }

    /**
     * Add country
     * @param country {Country} Country
     * @throws SQLException
     */
    public static void addCountry(Country country) throws SQLException {
        String query = "INSERT INTO tblCountry (name, continent) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, country.getName());
            stmt.setString(2, country.getContinent());
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
     * Remove country
     * @param name {String} Name
     * @throws SQLException
     */
    public static void removeCountry(String name) throws SQLException {
        String query = "DELETE FROM tblCountry WHERE name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, name);
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
     * Update country
     * @param country {Country} Country
     * @throws SQLException
     */
    public static void updateCountry(Country country) throws SQLException {
        String query = "UPDATE tblCountry SET name = ?, continent = ? WHERE idCountry = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, country.getName());
            stmt.setString(2, country.getContinent());
            stmt.setString(3, country.getIdCountry());
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
     * Get country by ID
     * @param idCountry {String} ID
     * @return Country
     * @throws SQLException
     */
    public Country getCountryById(String idCountry) throws SQLException {
        String query = "SELECT * FROM tblCountry WHERE idCountry = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idCountry);
        if (rs != null && rs.next()) {
            String name = rs.getString("name");
            String continent = rs.getString("continent");
            return new Country(idCountry, name, continent);
        }
        return null;
    }
}
