package AuxilierXML;
import Utils.ConnectionsUtlis;

import java.sql.*;

public class UploadXmlDAO {
    /**
     * Add sports from XML to database
     * @param sports {Sports} Sports object
     * @return boolean
     * @throws SQLException
     */
    public static boolean addSports(Sports sports) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        conn = ConnectionsUtlis.dbConnect();

        try {
            for (Sport sport : sports.getSportList()) {
                //-> Insert athlete
                String query = "INSERT INTO tblSport (type, idGender, name, description, minParticipants, scoringMeasure, oneGame) VALUES (?, ?, ?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                stmt.setString(1, sport.getType());
                stmt.setInt(2, (sport.getXmlGenre().equals("Men") ? 1 : 2));
                stmt.setString(3, sport.getName());
                stmt.setString(4, sport.getDesc());
                stmt.setInt(5, sport.getMinParticipants());
                stmt.setString(6, sport.getScoringMeasure());
                stmt.setString(7, sport.getOneGame());
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                int tempId = 0;
                if(rs.next()) {
                    tempId = rs.getInt(1);
                }

                if (sport.getXmlOlympicRecord() != null && !sport.getXmlOlympicRecord().isEmpty()) {
                    for (OlympicRecord record : sport.getXmlOlympicRecord()) {
                        System.out.println("Year: " + record.getYear());
                        System.out.println("Holder: " + record.getHolder());
                        System.out.println("Time: " + record.getTime());
                        System.out.println("Medals: " + record.getMedals());
                        System.out.println("---------------------------");
                    }
                }

                if (sport.getXmlWinnerOlympic() != null && !sport.getXmlWinnerOlympic().isEmpty()) {
                    for (WinnerOlympic winner : sport.getXmlWinnerOlympic()) {
                        System.out.println("Year: " + winner.getYear());
                        System.out.println("Winner: " + winner.getHolder());
                        System.out.println("Country: " + winner.getTime());
                        System.out.println("Medal: " + winner.getMedal());
                        System.out.println("---------------------------");
                    }
                }

                if (sport.getXmlRules() != null && !sport.getXmlRules().isEmpty()) {
                    for (String rule : sport.getXmlRules()) {
                        System.out.println("Rule: " + rule);
                        System.out.println("---------------------------");
                    }
                }

                //////////////////

                /*if (athlete.getOlympicParticipations() != null && !athlete.getOlympicParticipations().isEmpty()) {
                    for (ParticipationAthlete participation : athlete.getOlympicParticipations()) {

                        //-> Insert Gold medal
                        for (int i = 0; i < participation.getGold(); i++) {
                            System.out.println(i);
                            String query3 = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
                            stmt2 = conn.prepareStatement(query3);
                            stmt2.setInt(1, tempId);
                            stmt2.setNull(2, Types.INTEGER);
                            stmt2.setInt(3, participation.getYear());
                            stmt2.setInt(4, 1);
                            stmt2.executeUpdate();
                        }

                        //-> Insert Silver medal
                        for (int i = 0; i < participation.getSilver(); i++) {
                            String query3 = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
                            stmt2 = conn.prepareStatement(query3);
                            stmt2.setInt(1, tempId);
                            stmt2.setNull(2, Types.INTEGER);
                            stmt2.setInt(3, participation.getYear());
                            stmt2.setInt(4, 2);
                            stmt2.executeUpdate();
                        }

                        //-> Insert Bronze medal
                        for (int i = 0; i < participation.getBronze(); i++) {
                            String query3 = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
                            stmt2 = conn.prepareStatement(query3);
                            stmt2.setInt(1, tempId);
                            stmt2.setNull(2, Types.INTEGER);
                            stmt2.setInt(3, participation.getYear());
                            stmt2.setInt(4, 3);
                            stmt2.executeUpdate();
                        }
                    }
                }*/

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Add teams from XML to database
     * @param teams {Teams} Teams object
     * @return boolean
     * @throws SQLException
     */
    /*public static boolean addTeams(Teams teams) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        conn = ConnectionsUtlis.dbConnect();

        try {
            for (Team team : teams.getAthleteList()) {
                //-> Insert athlete
                String query = "INSERT INTO tblTeam (name, idCountry, idGender, idSport, yearFounded) VALUES (?, ?, ?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                stmt.setString(1, "");
                stmt.setString(2, athlete.getName());
                stmt.setString(3, athlete.getXmlCountry());
                stmt.setInt(4, (athlete.getXmlGenre().equals("Men") ? 1 : 2));
                stmt.setInt(5, athlete.getHeight());
                stmt.setFloat(6, athlete.getWeight());
                stmt.setDate(7, new java.sql.Date(athlete.getDateOfBirth().getTime()));
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                int tempId = 0;
                if(rs.next()) {
                    tempId = rs.getInt(1);
                }

                //-> Update password
                String query2 = "UPDATE tblAthlete SET password = HashBytes('MD5', ?) WHERE idAthlete = ?";
                stmt2 = conn.prepareStatement(query2);
                stmt2.setString(1, String.valueOf(tempId));
                stmt2.setString(2, String.valueOf(tempId));
                stmt2.executeUpdate();

                if (athlete.getOlympicParticipations() != null && !athlete.getOlympicParticipations().isEmpty()) {
                    for (ParticipationAthlete participation : athlete.getOlympicParticipations()) {

                        //-> Insert Gold medal
                        for (int i = 0; i < participation.getGold(); i++) {
                            System.out.println(i);
                            String query3 = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
                            stmt2 = conn.prepareStatement(query3);
                            stmt2.setInt(1, tempId);
                            stmt2.setNull(2, Types.INTEGER);
                            stmt2.setInt(3, participation.getYear());
                            stmt2.setInt(4, 1);
                            stmt2.executeUpdate();
                        }

                        //-> Insert Silver medal
                        for (int i = 0; i < participation.getSilver(); i++) {
                            String query3 = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
                            stmt2 = conn.prepareStatement(query3);
                            stmt2.setInt(1, tempId);
                            stmt2.setNull(2, Types.INTEGER);
                            stmt2.setInt(3, participation.getYear());
                            stmt2.setInt(4, 2);
                            stmt2.executeUpdate();
                        }

                        //-> Insert Bronze medal
                        for (int i = 0; i < participation.getBronze(); i++) {
                            String query3 = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
                            stmt2 = conn.prepareStatement(query3);
                            stmt2.setInt(1, tempId);
                            stmt2.setNull(2, Types.INTEGER);
                            stmt2.setInt(3, participation.getYear());
                            stmt2.setInt(4, 3);
                            stmt2.executeUpdate();
                        }
                    }
                }

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }*/

    /**
     * Add athletes from XML to database
     * @param athletes {Athletes} Athletes object
     * @return boolean
     * @throws SQLException
     */
    public static boolean addAthletes(Athletes athletes) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        conn = ConnectionsUtlis.dbConnect();

        try {
            for (Athlete athlete : athletes.getAthleteList()) {
                //-> Insert athlete
                String query = "INSERT INTO tblAthlete (password, name, idCountry, idGender, height, weight, dateOfBirth) VALUES (?, ?, ?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                stmt.setString(1, "");
                stmt.setString(2, athlete.getName());
                stmt.setString(3, athlete.getXmlCountry());
                stmt.setInt(4, (athlete.getXmlGenre().equals("Men") ? 1 : 2));
                stmt.setInt(5, athlete.getHeight());
                stmt.setFloat(6, athlete.getWeight());
                stmt.setDate(7, new java.sql.Date(athlete.getDateOfBirth().getTime()));
                stmt.executeUpdate();

                ResultSet rs = stmt.getGeneratedKeys();
                int tempId = 0;
                if(rs.next()) {
                    tempId = rs.getInt(1);
                }

                //-> Update password
                String query2 = "UPDATE tblAthlete SET password = HashBytes('MD5', ?) WHERE idAthlete = ?";
                stmt2 = conn.prepareStatement(query2);
                stmt2.setString(1, String.valueOf(tempId));
                stmt2.setString(2, String.valueOf(tempId));
                stmt2.executeUpdate();

                if (athlete.getOlympicParticipations() != null && !athlete.getOlympicParticipations().isEmpty()) {
                    for (ParticipationAthlete participation : athlete.getOlympicParticipations()) {

                        //-> Insert Gold medal
                        for (int i = 0; i < participation.getGold(); i++) {
                            System.out.println(i);
                            String query3 = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
                            stmt2 = conn.prepareStatement(query3);
                            stmt2.setInt(1, tempId);
                            stmt2.setNull(2, Types.INTEGER);
                            stmt2.setInt(3, participation.getYear());
                            stmt2.setInt(4, 1);
                            stmt2.executeUpdate();
                        }

                        //-> Insert Silver medal
                        for (int i = 0; i < participation.getSilver(); i++) {
                            String query3 = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
                            stmt2 = conn.prepareStatement(query3);
                            stmt2.setInt(1, tempId);
                            stmt2.setNull(2, Types.INTEGER);
                            stmt2.setInt(3, participation.getYear());
                            stmt2.setInt(4, 2);
                            stmt2.executeUpdate();
                        }

                        //-> Insert Bronze medal
                        for (int i = 0; i < participation.getBronze(); i++) {
                            String query3 = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
                            stmt2 = conn.prepareStatement(query3);
                            stmt2.setInt(1, tempId);
                            stmt2.setNull(2, Types.INTEGER);
                            stmt2.setInt(3, participation.getYear());
                            stmt2.setInt(4, 3);
                            stmt2.executeUpdate();
                        }
                    }
                }

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
