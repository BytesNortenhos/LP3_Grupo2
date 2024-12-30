package AuxilierXML;
import Utils.ConnectionsUtlis;
import Utils.ErrorHandler;
import Utils.PasswordUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.sql.rowset.CachedRowSet;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class UploadXmlDAO {
    /**
     * Add sports from XML to database
     * @param sports {Sports} Sports object
     * @return ErrorHandler
     */
    public ErrorHandler addSports(Sports sports) {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;

        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            PreparedStatement stmtGetSport = conn.prepareStatement("SELECT name, idSport FROM tblSport WHERE name IN (?) AND idGender = ?");
            for (Sport sport : sports.getSportList()) {
                stmtGetSport.setString(1, sport.getName());
                stmtGetSport.setInt(2, sport.getXmlGenre().equals("Men") ? 1 : 2);
                ResultSet rsSport = stmtGetSport.executeQuery();


                if(rsSport.next()) {
                    sport.setTempDatabaseId(rsSport.getInt(2));

                    String query = "UPDATE tblSport SET description = ?, minParticipants = ? WHERE idSport = ?";
                    stmt = conn.prepareStatement(query);

                    stmt.setString(1, sport.getDesc());
                    stmt.setInt(2, (sport.getMinParticipants() < 0 ? 4 : sport.getMinParticipants()));
                    stmt.setInt(3, sport.getTempDatabaseId());
                    stmt.executeUpdate();
                } else {
                    String query = "INSERT INTO tblSport (type, idGender, name, description, minParticipants, scoringMeasure, oneGame) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                    stmt.setString(1, sport.getType());
                    stmt.setInt(2, (sport.getXmlGenre().equals("Men") ? 1 : 2));
                    stmt.setString(3, sport.getName());
                    stmt.setString(4, sport.getDesc());
                    stmt.setInt(5, (sport.getMinParticipants() < 0 ? 4 : sport.getMinParticipants()));
                    stmt.setString(6, sport.getScoringMeasure());
                    stmt.setString(7, sport.getOneGame());
                    stmt.executeUpdate();

                    ResultSet rs = stmt.getGeneratedKeys();
                    if(rs.next()) {
                        sport.setTempDatabaseId(rs.getInt(1));
                    }
                }

                if (sport.getXmlOlympicRecord() != null && !sport.getXmlOlympicRecord().isEmpty()) {
                    for (OlympicRecord record : sport.getXmlOlympicRecord()) {
                        try {
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

                            String query2 = "INSERT INTO tblOlympicRecord (idSport, year, idAthlete, idTeam, result, medals) VALUES (?, ?, ?, ?, ?, ?)";
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
                        } catch (SQLException e) {
                            System.out.println("Erro ao adicionar Recorde Olímpico" + e.getMessage());
                        }
                    }
                }

                if (sport.getXmlWinnerOlympic() != null && !sport.getXmlWinnerOlympic().isEmpty()) {
                    for (WinnerOlympic winner : sport.getXmlWinnerOlympic()) {

                        try {
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

                            String query3 = "INSERT INTO tblWinnerOlympic (idSport, year, idAthlete, idTeam, result, idMedal) VALUES (?, ?, ?, ?, ?, ?)";
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
                        } catch (SQLException e) {
                            System.out.println("Erro ao adicionar Vencedor Olímpico" + e.getMessage());
                        }
                    }
                }

                if (sport.getXmlRules() != null && !sport.getXmlRules().isEmpty()) {
                    String queryDeleteRules = "DELETE FROM tblRule WHERE idSport = ?";
                    stmt = conn.prepareStatement(queryDeleteRules);
                    stmt.setInt(1, sport.getTempDatabaseId());
                    stmt.executeUpdate();

                    for (String rule : sport.getXmlRules()) {
                        String query3 = "INSERT INTO tblRule (idSport, description) VALUES (?, ?)";
                        stmt = conn.prepareStatement(query3);
                        stmt.setInt(1, sport.getTempDatabaseId());
                        stmt.setString(2, rule);
                        stmt.executeUpdate();
                    }
                }
            }

            return new ErrorHandler(true, "");
        } catch (Exception e) {
            return new ErrorHandler(false, e.getMessage());
        }
    }

    /**
     * Add teams from XML to database
     * @param teams {Teams} Teams object
     * @return ErrorHandler
     */
    public ErrorHandler addTeams(Teams teams) {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;

        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            PreparedStatement stmtGetTeam = conn.prepareStatement("SELECT name, idTeam FROM tblTeam WHERE name IN (?) AND idGender = ?");
            PreparedStatement stmtGetSport = conn.prepareStatement("SELECT idSport FROM tblSport WHERE name IN (?) AND idGender = ?");

            for (Team team : teams.getTeamList()) {
                stmtGetTeam.setString(1, team.getName());
                stmtGetTeam.setInt(2, team.getXmlGenre().equals("Men") ? 1 : 2);
                ResultSet rsTeam = stmtGetTeam.executeQuery();
                int tempSportId = 0;
                int tempTeamId = 0;

                if(rsTeam.next()) {
                    tempTeamId = rsTeam.getInt(2);

                    String query = "UPDATE tblTeam SET yearFounded = ? WHERE idTeam = ?";
                    stmt = conn.prepareStatement(query);

                    stmt.setInt(1, (team.getYearFounded() > Year.now().getValue() || team.getYearFounded() < 1  ? Year.now().getValue() : team.getYearFounded()));
                    stmt.setInt(2, tempTeamId);
                    stmt.executeUpdate();
                } else {
                    int tempGenderId = (team.getXmlGenre().equals("Men") ? 1 : 2);
                    stmtGetSport.setString(1, team.getXmlSport());
                    stmtGetSport.setInt(2, tempGenderId);
                    ResultSet rs = stmtGetSport.executeQuery();

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

                    stmt2.setInt(5, (team.getYearFounded() > Year.now().getValue() || team.getYearFounded() < 1  ? Year.now().getValue() : team.getYearFounded()));
                    stmt2.executeUpdate();

                    ResultSet rs2 = stmt2.getGeneratedKeys();
                    if(rs2.next()) {
                        tempTeamId = rs2.getInt(1);
                    }
                }

                if (team.getOlympicParticipations() != null && !team.getOlympicParticipations().isEmpty()) {
                    for (ParticipationTeam participation : team.getOlympicParticipations()) {
                        try {
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
                        } catch (SQLException e) {
                            System.out.println("Erro ao adicionar Participação Olímpica: " + e.getMessage());
                        }
                    }
                }
            }
            return new ErrorHandler(true, "");
        } catch (Exception e) {
            return new ErrorHandler(false, e.getMessage());
        }
    }

    /**
     * Add athletes from XML to database
     * @param athletes {Athletes} Athletes object
     * @return ErrorHandler
     */
    public ErrorHandler addAthletes(Athletes athletes) {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;

        try {
            ConnectionsUtlis connectionsUtlis = new ConnectionsUtlis();
            conn = connectionsUtlis.dbConnect();
            PreparedStatement stmtGetAthlete = conn.prepareStatement("SELECT name, idAthlete FROM tblAthlete WHERE name IN (?)");
            for (Athlete athlete : athletes.getAthleteList()) {
                stmtGetAthlete.setString(1, athlete.getName());
                ResultSet rsAthlete = stmtGetAthlete.executeQuery();
                int tempId = 0;

                LocalDate dateOfBirth = athlete.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate today = LocalDate.now();
                LocalDate minDate = today.minusYears(18);
                if (dateOfBirth.isAfter(today) || dateOfBirth.isAfter(minDate)) {
                    dateOfBirth = LocalDate.of(2000, 1, 1);
                }

                if(rsAthlete.next()) {
                    tempId = rsAthlete.getInt(2);

                    String query = "UPDATE tblAthlete SET height = ?, weight = ?, dateOfBirth = ? WHERE idAthlete = ?";
                    stmt = conn.prepareStatement(query);
                    stmt.setInt(1, (athlete.getHeight() < 110 ? 150 : athlete.getHeight()));
                    stmt.setFloat(2, (athlete.getWeight() < 30 ? 60 : athlete.getWeight()));
                    stmt.setDate(3, new java.sql.Date(Date.valueOf(dateOfBirth).getTime()));
                    stmt.setInt(4, tempId);
                    stmt.executeUpdate();
                } else {

                    //-> Insert athlete
                    String query = "INSERT INTO tblAthlete (password, name, idCountry, idGender, height, weight, dateOfBirth) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                    stmt.setString(1, "");
                    stmt.setString(2, athlete.getName());
                    stmt.setString(3, athlete.getXmlCountry());
                    stmt.setInt(4, (athlete.getXmlGenre().equals("Men") ? 1 : 2));
                    stmt.setInt(5, (athlete.getHeight() < 110 ? 150 : athlete.getHeight()));
                    stmt.setFloat(6, (athlete.getWeight() < 30 ? 60 : athlete.getWeight()));
                    stmt.setDate(7, new java.sql.Date(Date.valueOf(dateOfBirth).getTime()));
                    stmt.executeUpdate();

                    ResultSet rs = stmt.getGeneratedKeys();
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
                }

                if (athlete.getOlympicParticipations() != null && !athlete.getOlympicParticipations().isEmpty()) {
                    for (ParticipationAthlete participation : athlete.getOlympicParticipations()) {
                        //-> Insert Gold medal
                        for (int i = 0; i < participation.getGold(); i++) {
                            try {
                                String query3 = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
                                stmt2 = conn.prepareStatement(query3);
                                stmt2.setInt(1, tempId);
                                stmt2.setNull(2, Types.INTEGER);
                                stmt2.setInt(3, participation.getYear());
                                stmt2.setInt(4, 1);
                                stmt2.executeUpdate();
                            } catch (SQLException e) {
                                System.out.println("Erro ao adicionar Medalha de Ouro: " + e.getMessage());
                            }
                        }

                        //-> Insert Silver medal
                        for (int i = 0; i < participation.getSilver(); i++) {
                            try {
                                String query3 = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
                                stmt2 = conn.prepareStatement(query3);
                                stmt2.setInt(1, tempId);
                                stmt2.setNull(2, Types.INTEGER);
                                stmt2.setInt(3, participation.getYear());
                                stmt2.setInt(4, 2);
                                stmt2.executeUpdate();
                            } catch (SQLException e) {
                                System.out.println("Erro ao adicionar Medalha de Prata: " + e.getMessage());
                            }
                        }

                        //-> Insert Bronze medal
                        for (int i = 0; i < participation.getBronze(); i++) {
                            try {
                                String query3 = "INSERT INTO tblMedal (idAthlete, idTeam, year, idMedalType) VALUES (?, ?, ?, ?)";
                                stmt2 = conn.prepareStatement(query3);
                                stmt2.setInt(1, tempId);
                                stmt2.setNull(2, Types.INTEGER);
                                stmt2.setInt(3, participation.getYear());
                                stmt2.setInt(4, 3);
                                stmt2.executeUpdate();
                            } catch (SQLException e) {
                                System.out.println("Erro ao adicionar Medalha de Bronze: " + e.getMessage());
                            }
                        }
                    }
                }

            }
            return new ErrorHandler(true, "");
        } catch (Exception e) {
            return new ErrorHandler(false, e.getMessage());
        }
    }

    /**
     * Convert String HH:MM:SS.MS/HH:MM:SS to MS
     * @param time {String} String of time (HH:MM:SS/HH:MM:SS.SS)
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

    /**
     * Save XML and XSD files
     * @param pathXML {String} Path of XML file
     * @param pathXSD {String} Path of XSD file
     * @return boolean
     */
    public ErrorHandler saveXML(String pathXML, String pathXSD) {
        String pathSave = "src/main/java/DataXML_uploads/";

        File fileXML = new File(pathXML);
        File fileXSD = new File(pathXSD);

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
        String formattedDateTime = currentDateTime.format(formatter);

        try {
            Files.copy(fileXML.toPath(), Path.of(pathSave, formattedDateTime + "_" + fileXML.getName()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(fileXSD.toPath(), Path.of(pathSave, formattedDateTime + "_" + fileXSD.getName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return new ErrorHandler(false, e.getMessage());
        }

        return new ErrorHandler(true, "");
    }
}