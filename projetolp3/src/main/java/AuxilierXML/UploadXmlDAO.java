package AuxilierXML;
import Utils.ConnectionsUtlis;
import Utils.PasswordUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.sql.rowset.CachedRowSet;
import java.lang.reflect.Type;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class UploadXmlDAO {
    /**
     * Add sports from XML to database
     * @param sports {Sports} Sports object
     * @return boolean
     * @throws SQLException
     */
    public boolean addSports(Sports sports) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        conn = ConnectionsUtlis.dbConnect();

        try {
            for (Sport sport : sports.getSportList()) {
                String queryGetSport = "SELECT name FROM tblSport WHERE name LIKE ?";
                stmt = conn.prepareStatement(queryGetSport);
                stmt.setString(1, sport.getName());
                ResultSet rsSport = stmt.executeQuery();

                if(rsSport.next()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Aviso!");
                    alert.setHeaderText("A modalidade " + sport.getName() + " já existe na base de dados.");
                    Optional<ButtonType> result = alert.showAndWait();
                    continue;
                }

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
                if(rs.next()) {
                    sport.setTempDatabaseId(rs.getInt(1));
                }

                if (sport.getXmlOlympicRecord() != null && !sport.getXmlOlympicRecord().isEmpty()) {
                    for (OlympicRecord record : sport.getXmlOlympicRecord()) {
                        int tempHolderId = 0;
                        String queryGetHolder = sport.getType().equals("Individual") ?
                                "SELECT idAthlete FROM tblAthlete WHERE name = ?" :
                                "SELECT T.idTeam FROM tblTeam T INNER JOIN tblCountry C ON T.idCountry LIKE C.idCountry WHERE C.name = ?";

                        stmt = conn.prepareStatement(queryGetHolder);
                        stmt.setString(1, record.getHolder());
                        ResultSet rs2 = stmt.executeQuery();
                        if (rs2 != null && rs2.next()) {
                            tempHolderId = rs2.getInt(1);
                        }

                        String query2 = "INSERT INTO tblOlympicRecord (idSport, year, idAthlete, idTeam, timeMS, medals) VALUES (?, ?, ?, ?, ?, ?)";
                        stmt2 = conn.prepareStatement(query2);
                        stmt2.setInt(1, sport.getTempDatabaseId());
                        stmt2.setInt(2, record.getYear());

                        if (sport.getType().equals("Individual")) {
                            if(tempHolderId == 0) {
                                stmt2.setNull(3, Types.INTEGER);
                            } else {
                                stmt2.setInt(3, tempHolderId);
                            }
                            stmt2.setNull(4, Types.INTEGER);
                        } else {
                            stmt2.setNull(3, Types.INTEGER);
                            if(tempHolderId == 0) {
                                stmt2.setNull(4, Types.INTEGER);
                            } else {
                                stmt2.setInt(4, tempHolderId);
                            }
                        }

                        if (sport.getScoringMeasure().equals("Time")) {
                            stmt2.setInt(5, convertToMS(record.getTime()));
                            stmt2.setNull(6, Types.INTEGER);
                        } else {
                            stmt2.setNull(5, Types.INTEGER);
                            stmt2.setInt(6, record.getMedals());
                        }

                        stmt2.executeUpdate();
                    }
                }

                if (sport.getXmlWinnerOlympic() != null && !sport.getXmlWinnerOlympic().isEmpty()) {
                    for (WinnerOlympic winner : sport.getXmlWinnerOlympic()) {
                        int tempHolderId = 0;
                        String queryGetHolder = sport.getType().equals("Individual") ?
                                "SELECT idAthlete FROM tblAthlete WHERE name = ?" :
                                "SELECT T.idTeam FROM tblTeam T INNER JOIN tblCountry C ON T.idCountry LIKE C.idCountry WHERE C.name = ?";

                        stmt = conn.prepareStatement(queryGetHolder);
                        stmt.setString(1, winner.getHolder());
                        ResultSet rs2 = stmt.executeQuery();
                        if (rs2 != null && rs2.next()) {
                            tempHolderId = rs2.getInt(1);
                        }

                        int tempMedalTypeId = 0;
                        int tempMedalId = 0;
                        if(sport.getType().equals("Collective") && sport.getScoringMeasure().equals("Points")) {
                            tempMedalTypeId = switch (winner.getMedal()) {
                                case "Gold" -> 1;
                                case "Silver" -> 2;
                                case "Bronze" -> 3;
                                default -> 0;
                            };

                            String query2 = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
                            stmt = conn.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS);

                            stmt.setNull(1, Types.INTEGER);
                            if(tempHolderId == 0) {
                                stmt.setNull(2, Types.INTEGER);
                            } else {
                                stmt.setInt(2, tempHolderId);
                            }
                            stmt.setInt(3, winner.getYear());
                            stmt.setInt(4, tempMedalTypeId);
                            stmt.executeUpdate();

                            ResultSet rs3 = stmt.getGeneratedKeys();
                            if(rs3.next()) {
                                tempMedalId = (rs3.getInt(1));
                            }
                        }

                        String query3 = "INSERT INTO tblWinnerOlympic (idSport, year, idAthlete, idTeam, timeMS, idMedal) VALUES (?, ?, ?, ?, ?, ?)";
                        stmt = conn.prepareStatement(query3);
                        stmt.setInt(1, sport.getTempDatabaseId());
                        stmt.setInt(2, winner.getYear());
                        if (sport.getType().equals("Individual")) {
                            if(tempHolderId == 0) {
                                stmt.setNull(3, Types.INTEGER);
                            } else {
                                stmt.setInt(3, tempHolderId);
                            }
                            stmt.setNull(4, Types.INTEGER);
                        } else {
                            stmt.setNull(3, Types.INTEGER);
                            if(tempHolderId == 0) {
                                stmt.setNull(4, Types.INTEGER);
                            } else {
                                stmt.setInt(4, tempHolderId);
                            }
                        }

                        if (sport.getScoringMeasure().equals("Time")) {
                            stmt.setInt(5, convertToMS(winner.getTime()));
                            stmt.setNull(6, Types.INTEGER);
                        } else {
                            stmt.setNull(5, Types.INTEGER);
                            stmt.setInt(6, tempMedalId);
                        }

                        stmt.executeUpdate();
                    }
                }

                if (sport.getXmlRules() != null && !sport.getXmlRules().isEmpty()) {
                    for (String rule : sport.getXmlRules()) {
                        String query3 = "INSERT INTO tblRule (idSport, description) VALUES (?, ?)";
                        stmt = conn.prepareStatement(query3);
                        stmt.setInt(1, sport.getTempDatabaseId());
                        stmt.setString(2, rule);
                        stmt.executeUpdate();
                    }
                }
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
    public boolean addTeams(Teams teams) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;

        try {
            conn = ConnectionsUtlis.dbConnect();

            for (Team team : teams.getTeamList()) {
                String queryGetTeam = "SELECT name FROM tblTeam WHERE name LIKE ?";
                stmt = conn.prepareStatement(queryGetTeam);
                stmt.setString(1, team.getName());
                ResultSet rsTeam = stmt.executeQuery();

                if(rsTeam.next()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Aviso!");
                    alert.setHeaderText("A equipa " + team.getName() + " já existe na base de dados.");
                    Optional<ButtonType> result = alert.showAndWait();
                    continue;
                }

                int tempSportId = 0;
                int tempGenderId = (team.getXmlGenre().equals("Men") ? 1 : 2);
                String queryGetSport = "SELECT idSport FROM tblSport WHERE name LIKE ? AND idGender = ?";
                stmt = conn.prepareStatement(queryGetSport);
                stmt.setString(1, team.getXmlSport());
                stmt.setInt(2, tempGenderId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    tempSportId = rs.getInt(1);
                }

                String query = "INSERT INTO tblTeam (name, idCountry, idGender, idSport, yearFounded) VALUES (?, ?, ?, ?, ?)";
                stmt2 = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                stmt2.setString(1, team.getName());
                stmt2.setString(2, team.getXmlCountry());
                stmt2.setInt(3, tempGenderId);
                if(tempSportId == 0) {
                    stmt2.setNull(4, Types.INTEGER);
                } else {
                    stmt2.setInt(4, tempSportId);
                }
                stmt2.setInt(5, team.getYearFounded());
                stmt2.executeUpdate();

                ResultSet rs2 = stmt2.getGeneratedKeys();
                int tempTeamId = 0;
                if(rs2.next()) {
                    tempTeamId = rs2.getInt(1);
                }

                if (team.getOlympicParticipations() != null && !team.getOlympicParticipations().isEmpty()) {
                    for (ParticipationTeam participation : team.getOlympicParticipations()) {
                        int tempMedalTypeId = switch (participation.getResult()) {
                            case "Gold" -> 1;
                            case "Silver" -> 2;
                            case "Bronze" -> 3;
                            case "Diploma" -> 4;
                            default -> 0;
                        };

                        String query3 = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
                        stmt2 = conn.prepareStatement(query3);
                        stmt2.setNull(1, Types.INTEGER);
                        stmt2.setInt(2, tempTeamId);
                        stmt2.setInt(3, participation.getYear());
                        stmt2.setInt(4, tempMedalTypeId);
                        stmt2.executeUpdate();
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (stmt != null) stmt.close();
            if (stmt2 != null) stmt2.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * Add athletes from XML to database
     * @param athletes {Athletes} Athletes object
     * @return boolean
     * @throws SQLException
     */
    public boolean addAthletes(Athletes athletes) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        conn = ConnectionsUtlis.dbConnect();

        try {
            for (Athlete athlete : athletes.getAthleteList()) {
                String queryGetAthlete = "SELECT name FROM tblAthlete WHERE name LIKE ?";
                stmt = conn.prepareStatement(queryGetAthlete);
                stmt.setString(1, athlete.getName());
                ResultSet rsAthlete = stmt.executeQuery();

                if(rsAthlete.next()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Aviso!");
                    alert.setHeaderText("O atleta " + athlete.getName() + " já existe na base de dados.");
                    Optional<ButtonType> result = alert.showAndWait();
                    continue;
                }

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

                PasswordUtils passwordUtils = new PasswordUtils();
                String password = passwordUtils.encriptarPassword(String.valueOf(tempId));

                //-> Update password
                String query2 = "UPDATE tblAthlete SET password = ? WHERE idAthlete = ?";
                stmt2 = conn.prepareStatement(query2);
                stmt2.setString(1, password);
                stmt2.setString(2, String.valueOf(tempId));
                stmt2.executeUpdate();

                if (athlete.getOlympicParticipations() != null && !athlete.getOlympicParticipations().isEmpty()) {
                    for (ParticipationAthlete participation : athlete.getOlympicParticipations()) {

                        //-> Insert Gold medal
                        for (int i = 0; i < participation.getGold(); i++) {
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

    /**
     * Convert String HH:MM:SS.MS/HH:MM:SS to MS
     * @param time {String} String of time (HH:MM:SS.MS/HH:MM:SS)
     * @return int
     */
    public int convertToMS(String time) {
        DateTimeFormatter formatter;
        LocalTime parsedTime;

        if (time.contains(".")) {
            formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SS");
            parsedTime = LocalTime.parse(time, formatter);
        } else {
            formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            parsedTime = LocalTime.parse(time, formatter);
        }

        return (int) ChronoUnit.MILLIS.between(LocalTime.MIDNIGHT, parsedTime);
    }
}
