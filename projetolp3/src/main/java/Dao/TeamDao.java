package Dao;

import Models.Country;
import Models.Gender;
import Models.Sport;
import Utils.ConnectionsUtlis;
import Models.Team;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeamDao {
    /**
     * Get all teams
     * @return {List<Team>} List of teams
     * @throws SQLException
     */
    public static List<Team> getTeams() throws SQLException {
        List<Team> teams = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT t.idTeam, t.name AS teamName, t.idCountry, c.name AS countryName, " +
                "c.continent, t.idGender, g.description AS genderDesc, " +
                "t.idSport, t.yearFounded, t.minParticipants, t.maxParticipants " +
                "FROM tblTeam t " +
                "INNER JOIN tblCountry c ON t.idCountry = c.idCountry " +
                "INNER JOIN tblGender g ON t.idGender = g.idGender;");
        if (rs != null) {
            while (rs.next()) { int idTeam = rs.getInt("idTeam");
                String teamName = rs.getString("teamName");
                String idCountry = rs.getString("idCountry");
                String countryName = rs.getString("countryName");
                String continent = rs.getString("continent");
                int idGender = rs.getInt("idGender");
                String genderDesc = rs.getString("genderDesc");
                int idSport = rs.getInt("idSport");
                int yearFounded = rs.getInt("yearFounded");
                int minParticipants = rs.getInt("minParticipants");
                int maxParticipants = rs.getInt("maxParticipants");
                Country country = new Country(idCountry, countryName, continent);
                Gender gender = new Gender(idGender, genderDesc);
                SportDao sportDao = new SportDao();
                Sport sport = sportDao.getSportByIdV2(idSport);

                Team team = new Team(idTeam, teamName, country, gender, sport, yearFounded, minParticipants, maxParticipants);
                teams.add(team);
            }
        } else {
            System.out.println("ResultSet is null. No results for Team found.");
        }
        return teams;
    }

    /**
     * Get all teams names and ids
     * @return {List<Team>} List of teams
     * @throws SQLException
     */
    public List<List> getTeamsNamesAndId() throws SQLException {
        List<List> teams = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT idTeam, name FROM tblTeam;");
        if (rs != null) {
            while (rs.next()) {
                List<String> team = new ArrayList<>();
                team.add(rs.getString("idTeam"));
                team.add(rs.getString("name"));
                teams.add(team);
            }
        } else {
            System.out.println("ResultSet is null. No results for Team found.");
        }
        return teams;
    }

    /**
     * Add team
     * @param team {Team} Team
     * @return {int} Generated id
     * @throws SQLException
     */
    public int addTeam(Team team) throws SQLException {
        String query = "INSERT INTO tblTeam (name, idCountry, idGender, idSport, yearFounded, minParticipants, maxParticipants) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int generatedId = -1;

        boolean isAdded = false;

        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, team.getName());
            stmt.setString(2, team.getCountry().getIdCountry());
            stmt.setInt(3, team.getGenre().getIdGender());
            stmt.setInt(4, team.getSport().getIdSport());
            stmt.setInt(5, team.getYearFounded());
            stmt.setInt(6, team.getMinParticipants());
            stmt.setInt(7, team.getMaxParticipants());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                    System.out.println("Generated Team ID: " + generatedId); // Debugging
                } else {
                    System.out.println("No generated keys.");
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return generatedId;
    }

    /**
     * Remove team
     * @param idTeam {int} Id team
     * @throws SQLException
     */
    public static void removeTeam(int idTeam) throws SQLException {
        String query = "DELETE FROM tblTeam WHERE idTeam = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, idTeam);
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
     * Update team
     * @param team {Team} Team
     * @throws SQLException
     */
    public static void updateTeam(Team team) throws SQLException {
        String query = "UPDATE tblTeam SET name = ?, idCountry = ?, idGender = ?, idSport = ?, yearFounded = ?, minParticipants = ?, maxParticipants = ? WHERE idTeam = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1, team.getName());
            stmt.setString(2, team.getCountry().getIdCountry());
            stmt.setInt(3, team.getGenre().getIdGender());
            stmt.setInt(4, team.getSport().getIdSport());
            stmt.setInt(5, team.getYearFounded());
            stmt.setInt(6, team.getMinParticipants());
            stmt.setInt(7, team.getMaxParticipants());
            stmt.setInt(8, team.getIdTeam());
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
     * Update teama
     * @param idTeam {String} Id team
     * @param name {String} Name
     * @param playersCount {int} Players count
     * @param playersMaxCount {int} Players max count
     * @throws SQLException
     */
    public static void updateTeams(String idTeam, String name, int playersCount, int playersMaxCount) throws SQLException {
        String query = "UPDATE tblTeam SET name = ?, minParticipants = ?, maxParticipants = ? WHERE idTeam = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionsUtlis.dbConnect();
            stmt = conn.prepareStatement(query);

            stmt.setString(1,name);
            stmt.setInt(2, playersCount);
            stmt.setInt(3, playersMaxCount);
            stmt.setString(4, idTeam);
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
     * Get team by id
     * @param idTeam {int} Id team
     * @return {Team} Team
     * @throws SQLException
     */
    public static Team getTeamById(int idTeam) throws SQLException {
        String query = "SELECT t.idTeam, t.name AS teamName, c.idCountry, c.name AS countryName, c.continent, " +
                "g.idGender AS genderId, g.description AS genderDesc, " +
                "t.idSport, t.yearFounded, t.minParticipants, t.maxParticipants " +
                "FROM tblTeam t " +
                "INNER JOIN tblCountry c ON t.idCountry = c.idCountry " +
                "INNER JOIN tblGender g ON t.idGender = g.idGender " +
                "WHERE t.idTeam = ?";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idTeam);
        if (rs != null && rs.next()) {
            String teamName = rs.getString("teamName");
            String idCountry = rs.getString("idCountry");
            String countryName = rs.getString("countryName");
            String continent = rs.getString("continent");
            int genderId = rs.getInt("genderId");
            String genderDesc = rs.getString("genderDesc");
            int idSport = rs.getInt("idSport");
            int yearFounded = rs.getInt("yearFounded");
            Country country = new Country(idCountry, countryName, continent);
            Gender gender = new Gender(genderId, genderDesc);
            SportDao sportDao = new SportDao();
            Sport sport = sportDao.getSportById(idSport);
            return new Team(idTeam, teamName, country, gender, sport, yearFounded);
        }
        return null;
    }

    /**
     * Get team to show by id athlete
     * @param idAthlete {int} Id athlete
     * @return {List<List>} List of teams
     * @throws SQLException
     */
    public List<List> getTeamToShow(int idAthlete) throws SQLException {
        List<List> teams = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT t.idTeam, t.name AS teamName, " +
                "g.idGender AS genderId, g.description AS genderDesc, " +
                "c.idCountry, c.name AS countryName " +
                "FROM tblTeam t " +
                "INNER JOIN tblGender g ON t.idGender = g.idGender " +
                "INNER JOIN tblCountry c ON t.idCountry = c.idCountry; ");
        if (rs != null) {
            while (rs.next()) {
                List<String> team = new ArrayList<>();
                team.add(rs.getString("idTeam"));
                team.add(rs.getString("teamName"));
                team.add(rs.getString("genderId"));
                team.add(rs.getString("genderDesc"));
                team.add(rs.getString("idCountry"));
                team.add(rs.getString("countryName"));
                teams.add(team);
            }
        }else {
            System.out.println("ResultSet is null. No results for Sport found.");
        }
        return teams;
    }

    /**
     * Get team by id v2
     * @param idTeam {int} Id team
     * @return {Team} Team
     * @throws SQLException
     */
    public Team getTeamByIdV2(int idTeam) throws SQLException {
        String query = "SELECT t.idTeam, t.name AS teamName, c.idCountry, c.name AS countryName, c.continent, " +
                "g.idGender AS genderId, g.description AS genderDesc, " +
                "t.idSport, t.yearFounded, t.minParticipants, t.maxParticipants " +
                "FROM tblTeam t " +
                "INNER JOIN tblCountry c ON t.idCountry = c.idCountry " +
                "INNER JOIN tblGender g ON t.idGender = g.idGender " +
                "WHERE t.idTeam = ?";

        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idTeam);
        if (rs != null && rs.next()) {
            String teamName = rs.getString("teamName");
            String idCountry = rs.getString("idCountry");
            String countryName = rs.getString("countryName");
            String continent = rs.getString("continent");
            int genderId = rs.getInt("genderId");
            String genderDesc = rs.getString("genderDesc");
            int idSport = rs.getInt("idSport"); // Apenas o ID do esporte
            int yearFounded = rs.getInt("yearFounded");

            Country country = new Country(idCountry, countryName, continent);
            Gender gender = new Gender(genderId, genderDesc);

            // Aqui o esporte ainda não é carregado; será carregado quando necessário
            return new Team(idTeam, teamName, country, gender, idSport, yearFounded);
        }
        return null;
    }

    /**
     * Get team by id minimum
     * @param idTeam {int} Id team
     * @return {Team} Team
     * @throws SQLException
     */
    public static Team getTeamByIdMinimum(int idTeam) throws SQLException {
        String query = "SELECT t.idTeam, t.name AS teamName, c.idCountry, c.name AS countryName, " +
                "g.idGender AS genderId, g.description AS genderDesc, " +
                "t.idSport, t.yearFounded " +
                "FROM tblTeam t " +
                "INNER JOIN tblCountry c ON t.idCountry = c.idCountry " +
                "INNER JOIN tblGender g ON t.idGender = g.idGender " +
                "WHERE t.idTeam = ?";

        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idTeam);
        if (rs != null && rs.next()) {
            String teamName = rs.getString("teamName");
            String idCountry = rs.getString("idCountry");
            String countryName = rs.getString("countryName");
            int genderId = rs.getInt("genderId");
            String genderDesc = rs.getString("genderDesc");
            int idSport = rs.getInt("idSport"); // Apenas o ID do esporte
            int yearFounded = rs.getInt("yearFounded");

            Country country = new Country(idCountry, countryName);
            Gender gender = new Gender(genderId, genderDesc);

            // Aqui o esporte ainda não é carregado; será carregado quando necessário
            return new Team(idTeam, teamName, country, gender, idSport, yearFounded);
        }
        return null;
    }

    public List<List> getTeamsToShow() throws SQLException {
        List<List> teams = new ArrayList<>();
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery("SELECT t.idTeam as idTeam, t.name as teamName, t.yearFounded, t.minParticipants, t.maxParticipants, " +
                        "c.name as countryName, g.description as gender, s.name as sport " +
                "from tblTeam as t " +
                "join tblCountry c on t.idCountry = c.idCountry " +
                "join tblGender g on t.idGender = g.idGender " +
                "join tblSport s on t.idSport = s.idSport; ");
        if (rs != null) {
            while (rs.next()) {
                List<String> team = new ArrayList<>();
                team.add(rs.getString("teamName"));
                team.add(rs.getString("yearFounded"));
                team.add(rs.getString("minParticipants"));
                team.add(rs.getString("maxParticipants"));
                team.add(rs.getString("countryName"));
                team.add(rs.getString("gender"));
                team.add(rs.getString("sport"));
                team.add(rs.getString("idTeam"));
                teams.add(team);
            }
        }
        return teams;
    }
    public int getNAthletesOnTeam(int idTeam) throws SQLException {
        String query = "SELECT COUNT(*) as nAthletes FROM tblRegistration WHERE idTeam = ? " +
                "AND (idStatus = 3 OR idStatus = 4);";
        CachedRowSet rs = ConnectionsUtlis.dbExecuteQuery(query, idTeam);
        if (rs != null && rs.next()) {
            return rs.getInt("nAthletes");
        }
        return 0;
    }

}