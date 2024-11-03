package Utils;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;

import Models.*;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
        Source xmlFile = new StreamSource(new File(absolutePath + "/" + xmlName + ".xml"));
        Source xsdFile = new StreamSource(new File(absolutePath + "/" + xmlName + "_xsd.xml"));
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        try {
            Schema schema = schemaFactory.newSchema(xsdFile);
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
            return true;
        } catch(SAXException | IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Guarda os dados do XML de Atletas na Base de Dados (falta obter o id do atleta para colocar na tabela de medalhas)
     * @return void
     */
    public void getAthletesDataXML() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(absolutePath + "/athletes.xml"));

            Element root = document.getDocumentElement();
            NodeList sportsNode = root.getChildNodes();

            for (int i = 0; i < sportsNode.getLength(); i++) {
                ArrayList<String> tempArray = new ArrayList<>();
                Node sportNode = sportsNode.item(i);
                if (sportNode.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList sportPropertyList = sportNode.getChildNodes();
                    for (int j = 0; j < sportPropertyList.getLength(); j++) {
                        Node sportPropertyNode = sportPropertyList.item(j);
                        if (sportPropertyNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element sportPropertyElement = (Element) sportPropertyNode;
                            tempArray.add(sportPropertyElement.getTextContent().trim().replaceAll("\\s+", "/"));
                        }
                    }

                    Athlete tempAthlete = new Athlete(
                            0,
                            "é preciso encriptar",
                            tempArray.get(0),
                            new Country(0, tempArray.get(1), ""),
                            new Gender(((tempArray.get(2) == "Men") ? 1 : 2), ((tempArray.get(2) == "Men") ? "Male" : "Female")),
                            Integer.parseInt(tempArray.get(3)),
                            Float.parseFloat(tempArray.get(4)),
                            formatter.parse(tempArray.get(5))
                    );

                    // OBTER O ID DO ATLETA INSERIDO!
                    int tempAthleteId = 0;

                    String[] tempOlympicParticipations = tempArray.get(6).split("/");
                    for (int c = 0; c < tempOlympicParticipations.length; c = c + 4) {
                        for (int g = 0; g < Integer.parseInt(tempOlympicParticipations[c + 1]); g++) {
                            Medal tempMedal = new Medal(
                                    0,
                                    tempAthleteId,
                                    0,
                                    Integer.parseInt(tempOlympicParticipations[c]),
                                    new MedalType(1, "Gold")
                            );

                            System.out.println(tempMedal.getMedalType().getDescMedalType());
                        }

                        for (int g = 0; g < Integer.parseInt(tempOlympicParticipations[c + 2]); g++) {
                            Medal tempMedal = new Medal(
                                    0,
                                    tempAthleteId,
                                    0,
                                    Integer.parseInt(tempOlympicParticipations[c]),
                                    new MedalType(2, "Silver")
                            );
                        }

                        for (int g = 0; g < Integer.parseInt(tempOlympicParticipations[c + 3]); g++) {
                            Medal tempMedal = new Medal(
                                    0,
                                    tempAthleteId,
                                    0,
                                    Integer.parseInt(tempOlympicParticipations[c]),
                                    new MedalType(3, "Bronze")
                            );
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Guarda os dados do XML de Sports na Base de Dados
     * @return void
     */
    public void getSportsDataXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(absolutePath + "/sports.xml"));

            Element root = document.getDocumentElement();
            NodeList sportsNode = root.getChildNodes();

            for (int i = 0; i < sportsNode.getLength(); i++) {
                ArrayList<String> tempArray = new ArrayList<>();
                Node sportNode = sportsNode.item(i);
                if (sportNode.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList sportPropertyList = sportNode.getChildNodes();
                    for (int j = 0; j < sportPropertyList.getLength(); j++) {
                        Node sportPropertyNode = sportPropertyList.item(j);
                        if (sportPropertyNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element sportPropertyElement = (Element) sportPropertyNode;
                            String tagName = sportPropertyElement.getTagName().trim();
                            String textContent = sportPropertyElement.getTextContent().trim();

                            if (
                                    tagName.equals("type")
                                            || tagName.equals("genre")
                                            || tagName.equals("name")
                                            || tagName.equals("description")
                                            || tagName.equals("minParticipants")
                                            || tagName.equals("scoringMeasure")
                                            || tagName.equals("oneGame")
                            ) {
                                tempArray.add(textContent);
                            }

                            if (tagName.equals("olympicRecord")) {
                                NodeList recordProperties = sportPropertyElement.getChildNodes();
                                StringBuilder tempOlympicRecord = new StringBuilder();
                                for (int k = 0; k < recordProperties.getLength(); k++) {
                                    Node recordNode = recordProperties.item(k);
                                    if (recordNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element recordElement = (Element) recordNode;
                                        String recordTag = recordElement.getTagName().trim();
                                        String recordText = recordElement.getTextContent().trim();
                                        tempOlympicRecord.append(recordText + "/");
                                    }
                                }

                                tempArray.add(tempOlympicRecord.toString());
                            }

                            if (tagName.equals("winnerOlympic")) {
                                NodeList recordProperties = sportPropertyElement.getChildNodes();
                                StringBuilder tempWinnerOlympic = new StringBuilder();
                                for (int k = 0; k < recordProperties.getLength(); k++) {
                                    Node recordNode = recordProperties.item(k);
                                    if (recordNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element recordElement = (Element) recordNode;
                                        String recordTag = recordElement.getTagName().trim();
                                        String recordText = recordElement.getTextContent().trim();
                                        tempWinnerOlympic.append(recordText + "/");
                                    }
                                }

                                tempArray.add(tempWinnerOlympic.toString());
                            }

                            if (tagName.equals("rules")) {
                                NodeList rulesList = sportPropertyElement.getElementsByTagName("rule");
                                StringBuilder tempRules = new StringBuilder();
                                for (int k = 0; k < rulesList.getLength(); k++) {
                                    Element ruleElement = (Element) rulesList.item(k);
                                    String ruleText = ruleElement.getTextContent().trim();
                                    tempRules.append(ruleText).append("/");
                                }

                                tempArray.add(tempRules.toString());
                            }
                        }
                    }

                    for (int c = 0; c < tempArray.size(); c++) {
                        System.out.println(tempArray.get(c));
                    }

                    Sport tempSport = new Sport(
                            0,
                            tempArray.get(0),
                            new Gender(((tempArray.get(1) == "Men") ? 1 : 2), ((tempArray.get(1) == "Men") ? "Male" : "Female")),
                            tempArray.get(2),
                            tempArray.get(3),
                            Integer.parseInt(tempArray.get(4)),
                            tempArray.get(5),
                            tempArray.get(6),
                            null,
                            null,
                            null
                    );

                    // OBTER O ID DO Sport INSERIDO!
                    int tempSportId = 0;

                    String[] tempOlympicRecordContent = tempArray.get(7).substring(0, tempArray.get(7).length() - 1).split("/");
                    int tempORHolderId = 0; //Select BD (Name) > tempOlympicRecordContent[2]
                    OlympicRecord tempOlympicRecord = new OlympicRecord(
                            tempSportId,
                            (tempArray.get(5).equals("Time") ? Integer.parseInt(tempOlympicRecordContent[1]) : Integer.parseInt(tempOlympicRecordContent[0])),
                            (tempArray.get(0).equals("Individual") ? tempORHolderId : 0),
                            (tempArray.get(0).equals("Collective") ? tempORHolderId : 0),
                            (tempArray.get(5).equals("Time") ? convertToMS(tempOlympicRecordContent[0]) : 0),
                            (tempArray.get(5).equals("Points") ? Integer.parseInt(tempOlympicRecordContent[2]) : 0)
                    );
                    // Adicionar à BD

                    String[] tempWinnerOlympicContent = tempArray.get(8).substring(0, tempArray.get(7).length() - 1).split("/");
                    int tempWOHolderId = 0; //Select BD (Name) > tempWinnerOlympicContent[2]
                    String descMedalType = tempWinnerOlympicContent[2];
                    MedalType tempMedalType = null;
                    if (!descMedalType.equals("")) {
                        //executar query BD para obter MedalType
                        tempMedalType = (tempArray.get(5).equals("Points") ? new MedalType(1, "Teste") : new MedalType(0, null));
                    }
                    WinnerOlympic tempWinnerOlympic = new WinnerOlympic(
                            tempSportId,
                            (tempArray.get(5).equals("Time") ? Integer.parseInt(tempOlympicRecordContent[1]) : Integer.parseInt(tempOlympicRecordContent[0])),
                            (tempArray.get(0).equals("Individual") ? tempWOHolderId : 0),
                            (tempArray.get(0).equals("Collective") ? tempWOHolderId : 0),
                            (tempArray.get(5).equals("Time") ? convertToMS(tempWinnerOlympicContent[0]) : 0),
                            (tempArray.get(5).equals("Points")
                                    ? // TRUE:
                                    new Medal(
                                            0,
                                            (tempArray.get(0).equals("Individual") ? tempORHolderId : 0),
                                            (tempArray.get(0).equals("Collective") ? tempORHolderId : 0),
                                            (tempArray.get(5).equals("Time") ? Integer.parseInt(tempOlympicRecordContent[1]) : Integer.parseInt(tempOlympicRecordContent[0])),
                                            tempMedalType
                                    )
                                    : // FALSE:
                                    null
                            )
                    );
                    // Adicionar à BD

                    String[] tempRules = tempArray.get(9).split("/");
                    for (int r = 0; r < tempRules.length; r++) {
                        Rule tempRule = new Rule(
                                0,
                                tempSportId,
                                tempRules[r]
                        );
                        //Adicionar à BD
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Guarda os dados do XML de Equipas na Base de Dados
     * @return void
     */
    public void getTeamsDataXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(absolutePath + "/teams.xml"));

            Element root = document.getDocumentElement();
            NodeList sportsNode = root.getChildNodes();

            for (int i = 0; i < sportsNode.getLength(); i++) {
                Node sportNode = sportsNode.item(i);
                if (sportNode.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList sportPropertyList = sportNode.getChildNodes();
                    for (int j = 0; j < sportPropertyList.getLength(); j++) {
                        Node sportPropertyNode = sportPropertyList.item(j);
                        if (sportPropertyNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element sportPropertyElement = (Element) sportPropertyNode;
                            System.out.println(sportPropertyElement.getTagName().trim() + ": " + sportPropertyElement.getTextContent().trim());
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Convert String HH:MM:SS.MS/HH:MM:SS to MS
     * @param time {String} String of time (HH:MM:SS.MS/HH:MM:SS)
     * @return int
     */
    public static int convertToMS(String time) {
        // Choose the appropriate pattern based on the input format
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