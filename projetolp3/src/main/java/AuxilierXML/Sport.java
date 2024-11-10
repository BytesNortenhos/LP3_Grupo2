package AuxilierXML;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "sport")
@XmlAccessorType(XmlAccessType.FIELD)
public class Sport {
    @XmlElement
    private String type;
    @XmlElement(name = "genre")
    private String xmlGenre;
    @XmlElement
    private String name;
    @XmlElement(name = "description")
    private String desc;
    @XmlElement
    private int minParticipants;
    @XmlElement
    private String scoringMeasure;
    @XmlElement
    private String oneGame;
    @XmlElement(name = "olympicRecord")
    private List<OlympicRecord> xmlOlympicRecord; // Needed for JAXB
    @XmlElement(name = "winnerOlympic")
    private List<WinnerOlympic> xmlWinnerOlympic; // Needed for JAXB
    @XmlElementWrapper(name = "rules")
    @XmlElement(name = "rule")
    private List<String> xmlRules; // Needed for JAXB
    private int tempDatabaseId;

    /**
     * Constructor of Sport (without parameters)
     */
    public Sport() {}

    /**
     * Get type
     * @return String
     */
    public String getType() {
        return type;
    }

    /**
     * Set type
     * @param type {String} Tipo
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Set name
     * @param name {String} Nome
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get description
     * @return String
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Set description
     * @param desc {String} Descrição
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Get minimum number of participants
     * @return int
     */
    public int getMinParticipants() {
        return minParticipants;
    }

    /**
     * Set minimum number of participants
     * @param minParticipants {int} Minimum number of participants
     */
    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    /**
     * Get measure of scoring
     * @return String
     */
    public String getScoringMeasure() {
        return scoringMeasure;
    }

    /**
     * Set measure of scoring
     * @param scoringMeasure {String} Measure of scoring
     */
    public void setScoringMeasure(String scoringMeasure) {
        this.scoringMeasure = scoringMeasure;
    }

    /**
     * Get "one game"
     * @return String
     */
    public String getOneGame() {
        return oneGame;
    }

    /**
     * Set "one game"
     * @param oneGame {String} "Um jogo"
     */
    public void setOneGame(String oneGame) {
        this.oneGame = oneGame;
    }

    /**
     * Get XML genre
     * @return String
     */
    public String getXmlGenre() {
        return xmlGenre;
    }

    /**
     * Set XML genre
     * @param xmlGenre {String} Genre
     */
    public void setXmlGenre(String xmlGenre) {
        this.xmlGenre = xmlGenre;
    }

    /**
     * Get XML olympic record
     * @return List<RecordOrWinnerOlympicXML>
     */
    public List<OlympicRecord> getXmlOlympicRecord() {
        return xmlOlympicRecord;
    }

    /**
     * Set XML olympic record
     * @param xmlOlympicRecord {List<RecordOrWinnerOlympicXML>} Olympic record
     */
    public void setXmlOlympicRecord(List<OlympicRecord> xmlOlympicRecord) {
        this.xmlOlympicRecord = xmlOlympicRecord;
    }

    /**
     * Get XML winner olympic
     * @return List<WinnerOlympicXML>
     */
    public List<WinnerOlympic> getXmlWinnerOlympic() {
        return xmlWinnerOlympic;
    }

    /**
     * Set XML winner olympic
     * @param xmlWinnerOlympic {List<WinnerOlympicXML>} Winner olympic
     */
    public void setXmlWinnerOlympic(List<WinnerOlympic> xmlWinnerOlympic) {
        this.xmlWinnerOlympic = xmlWinnerOlympic;
    }

    /**
     * Get XML rules
     * @return List<String>
     */
    public List<String> getXmlRules() {
        return xmlRules;
    }

    /**
     * Set XML rules
     * @param xmlRules {List<String>} Rules
     */
    public void setXmlRules(List<String> xmlRules) {
        this.xmlRules = xmlRules;
    }

    /**
     * Get temporary database ID
     * @return int
     */
    public int getTempDatabaseId() {
        return tempDatabaseId;
    }

    /**
     * Set temporary database ID
     * @param tempDatabaseId {int} Temporary database ID
     */
    public void setTempDatabaseId(int tempDatabaseId) {
        this.tempDatabaseId = tempDatabaseId;
    }
}