package Utils;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
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

    //-> É preciso modificar para retornar objetos instanciados!
    public void getAthletesDataXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(absolutePath + "/athletes.xml"));

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

    //-> É preciso modificar para retornar objetos instanciados!
    public void getSportsDataXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(absolutePath + "/sports.xml"));

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

    //-> É preciso modificar para retornar objetos instanciados!
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
}