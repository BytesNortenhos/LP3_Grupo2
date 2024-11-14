package Dao;

import Models.Athlete;
import Utils.ConnectionsUtlis;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TesteDao {
    public static void getAthletes() throws SQLException {

        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT * FROM teste;");

        if (rs != null) {
            while (rs.next()) {
                String teste = rs.getString("teste");
                System.out.println(teste);
            }
        } else {
            System.out.println("ResultSet is null. No results found.");
        }
    }
}