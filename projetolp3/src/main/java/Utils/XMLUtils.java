package Utils;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

import AuxilierXML.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class XMLUtils {
    private final String absolutePath = "src/main/java/DataXML";

    /**
     * Verifica se o nome do ficheiro em parâmetro existe
     * @param xmlName {String} Nome do Ficheiro XML (sem extensão)
     * @return Boolean[]
     */
    public Boolean[] checkFilesExist (String xmlName) {
        Boolean xmlFile = false;
        Boolean xsdFile = false;

        File directory = new File(absolutePath);
        File[] filesArray = directory.listFiles();
        List<String> filesnameArray = new ArrayList<>();
        for (File file : filesArray) {
            String fileName = file.getName().replace(".xml", "");

            if(fileName.equals(xmlName)) xmlFile = true;
            if(fileName.equals(xmlName + "_xsd")) xsdFile = true;
        }

        return new Boolean[]{xmlFile, xsdFile};
    }

    /**
     * Faz a validação do ficheiro XML com XSD
     * @param xmlName {String} Nome do Ficheiro XML (sem extensão)
     * @return boolean
     */
    public boolean validateXML(String xmlName) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(absolutePath + "/" + xmlName + "_xsd.xml"));

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(absolutePath + "/" + xmlName + ".xml")));
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Guarda os dados do XML de Sports na Base de Dados
     * @return void
     */
    public Sports getSportsDataXML() {
        String absolutePath = "src/main/java/DataXML";
        String xmlName = "sports";

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Sports.class, Sport.class, WinnerOlympic.class, OlympicRecord.class, Rules.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            Sports sports = (Sports) unmarshaller.unmarshal(new File(absolutePath + "/" + xmlName + ".xml"));

            for (Sport sport : sports.getSportList()) {
                System.out.println("Type: " + sport.getType());
                System.out.println("Gender: " + sport.getXmlGenre());
                System.out.println("Name: " + sport.getName());
                System.out.println("Description: " + sport.getDesc());
                System.out.println("Minimum Participants: " + sport.getMinParticipants());
                System.out.println("Scoring Measure: " + sport.getScoringMeasure());
                System.out.println("One Game: " + sport.getOneGame());
                System.out.println("");

                if (sport.getXmlOlympicRecord() != null && !sport.getXmlOlympicRecord().isEmpty()) {
                    for (OlympicRecord record : sport.getXmlOlympicRecord()) {
                        System.out.println("Year: " + record.getYear());
                        System.out.println("Holder: " + record.getHolder());
                        System.out.println("Time: " + record.getTime());
                        System.out.println("Medals: " + record.getMedals());
                        System.out.println("---------------------------");
                    }
                } else {
                    System.out.println("Nenhum registro olímpico encontrado.");
                }

                if (sport.getXmlWinnerOlympic() != null && !sport.getXmlWinnerOlympic().isEmpty()) {
                    for (WinnerOlympic winner : sport.getXmlWinnerOlympic()) {
                        System.out.println("Year: " + winner.getYear());
                        System.out.println("Winner: " + winner.getHolder());
                        System.out.println("Country: " + winner.getTime());
                        System.out.println("Medal: " + winner.getMedal());
                        System.out.println("---------------------------");
                    }
                } else {
                    System.out.println("Nenhum vencedor olímpico encontrado.");
                }

                if (sport.getXmlRules() != null && !sport.getXmlRules().isEmpty()) {
                    for (String rule : sport.getXmlRules()) {
                        System.out.println("Rule: " + rule);
                        System.out.println("---------------------------");
                    }
                } else {
                    System.out.println("Nenhuma regra encontrada.");
                }
            }

            return sports;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Guarda os dados do XML de Equipas na Base de Dados
     * @return void
     */
    public Teams getTeamsDataXML() {
        String absolutePath = "src/main/java/DataXML";
        String xmlName = "teams";

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Teams.class, Team.class, ParticipationTeam.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            Teams teams = (Teams) unmarshaller.unmarshal(new File(absolutePath + "/" + xmlName + ".xml"));

            for (Team team : teams.getTeamList()) {
                System.out.println("Name: " + team.getName());
                System.out.println("Country: " + team.getXmlCountry());
                System.out.println("Genre: " + team.getXmlGenre());
                System.out.println("Sport: " + team.getXmlSport());
                System.out.println("Foundation Year: " + team.getYearFounded());

                if (team.getOlympicParticipations() != null && !team.getOlympicParticipations().isEmpty()) {
                    for (ParticipationTeam participation : team.getOlympicParticipations()) {
                        System.out.println("Year: " + participation.getYear());
                        System.out.println("Result: " + participation.getResult());
                        System.out.println("---------------------------");
                    }
                } else {
                    System.out.println("Nenhuma participação registrada.");
                }
                System.out.println();
            }

            return teams;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Guarda os dados do XML de Atletas na Base de Dados (falta obter o id do atleta para colocar na tabela de medalhas)
     * @return void
     */
    public Athletes getAthletesDataXML() {
        String absolutePath = "src/main/java/DataXML";
        String xmlName = "athletes";

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Athletes.class, Athlete.class, ParticipationAthlete.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            Athletes athletes = (Athletes) unmarshaller.unmarshal(new File(absolutePath + "/" + xmlName + ".xml"));

            for (Athlete athlete : athletes.getAthleteList()) {
                System.out.println("Name: " + athlete.getName());
                System.out.println("Country: " + athlete.getXmlCountry());
                System.out.println("Gender: " + athlete.getXmlGenre());
                System.out.println("Height: " + athlete.getHeight());
                System.out.println("Weight: " + athlete.getWeight());
                System.out.println("Date of Birth: " + athlete.getDateOfBirth());

                if (athlete.getOlympicParticipations() != null && !athlete.getOlympicParticipations().isEmpty()) {
                    for (ParticipationAthlete participation : athlete.getOlympicParticipations()) {
                        System.out.println("Year: " + participation.getYear());
                        System.out.println("Gold: " + participation.getGold());
                        System.out.println("Silver: " + participation.getSilver());
                        System.out.println("Bronze: " + participation.getBronze());
                        //System.out.println("Certificate: " + participation.getCertificate());
                        System.out.println("---------------------------");
                    }
                } else {
                    System.out.println("Nenhuma participação registrada.");
                }
                System.out.println();
            }

            return athletes;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convert String HH:MM:SS.MS/HH:MM:SS to MS
     * @param time {String} String of time (HH:MM:SS.MS/HH:MM:SS)
     * @return int
     */
    public static int convertToMS(String time) {
        DateTimeFormatter formatter;
        LocalTime parsedTime;

        if (time.contains(".")) {
            formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SS");
            parsedTime = LocalTime.parse(time, formatter);
        } else {
            formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            parsedTime = LocalTime.parse(time, formatter);
        }

        return (int) parsedTime.until(LocalTime.MIDNIGHT, ChronoUnit.MILLIS);
    }
}