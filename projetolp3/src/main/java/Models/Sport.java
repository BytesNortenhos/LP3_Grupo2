package Models;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "sport")
@XmlAccessorType(XmlAccessType.FIELD)
public class Sport {
    private int idSport;
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
    private List<OlympicRecordXML> xmlOlympicRecord; // Needed for JAXB
    @XmlElement(name = "winnerOlympic")
    private List<WinnerOlympicXML> xmlWinnerOlympic; // Needed for JAXB
    @XmlElementWrapper(name = "rules")
    @XmlElement(name = "rule")
    private List<String> xmlRules; // Needed for JAXB

    private Gender genre;
    private OlympicRecord olympicRecord;
    private List<WinnerOlympic> winnerOlympic;
    private List<Rule> rules;

    /**
     * Constructor of Sport
     * @param idSport {int} Sport ID
     * @param type {String} Type
     * @param genre {Gender} Genre
     * @param name {String} Name
     * @param desc {String} Description
     * @param minParticipants {int} Number minimum of participants
     * @param scoringMeasure {String} Measure of scoring
     * @param oneGame {String} "One Game"
     * @param olympicRecord {OlympicRecord} Olympic record
     * @param winnerOlympic {List<WinnerOlympic>} List of olympic winners
     * @param rules {List<Rule>} List of rules
     */
    public Sport(int idSport, String type, Gender genre, String name, String desc, int minParticipants,
                 String scoringMeasure, String oneGame, OlympicRecord olympicRecord, List<WinnerOlympic> winnerOlympic,
                 List<Rule> rules) {
        this.idSport = idSport;
        this.type = type;
        this.genre = genre;
        this.name = name;
        this.desc = desc;
        this.minParticipants = minParticipants;
        this.scoringMeasure = scoringMeasure;
        this.oneGame = oneGame;
        this.olympicRecord = olympicRecord;
        this.winnerOlympic = winnerOlympic;
        this.rules = rules;
    }
    public Sport() {}

    /**
     * Get Sport ID
     * @return int
     */
    public int getIdSport() {
        return idSport;
    }

    /**
     * Set Sport ID
     * @param idSport {int} Sport ID
     */
    public void setIdSport(int idSport) {
        this.idSport = idSport;
    }

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
     * Get genre
     * @return Gender
     */
    public Gender getGenre() {
        return genre;
    }

    /**
     * Set genre
     * @param genre {Gender} Gênero
     */
    public void setGenre(Gender genre) {
        this.genre = genre;
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
     * Get olympic record
     * @return OlympicRecord
     */
    public OlympicRecord getOlympicRecord() {
        return olympicRecord;
    }

    /**
     * Set olympic record
     * @param olympicRecord {OlympicRecord} Olympic record
     */
    public void setOlympicRecord(OlympicRecord olympicRecord) {
        this.olympicRecord = olympicRecord;
    }

    /**
     * Get olympic winners
     * @return List<WinnerOlympic>
     */
    public List<WinnerOlympic> getWinnerOlympic() {
        return winnerOlympic;
    }

    /**
     * Set olympic winners
     * @param winnerOlympic {List<WinnerOlympic>} List of olympic winners
     */
    public void setWinnerOlympic(List <WinnerOlympic> winnerOlympic) {
        this.winnerOlympic = winnerOlympic;
    }

    /**
     * Get rules
     * @return List<Rule>
     */
    public List<Rule> getRules() {
        return rules;
    }

    /**
     * Set rules
     * @param rules {List<Rule>} List of rules
     */
    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    /**
     * Get XML genre
     * @return
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
    public List<OlympicRecordXML> getXmlOlympicRecord() {
        return xmlOlympicRecord;
    }

    /**
     * Set XML olympic record
     * @param xmlOlympicRecord {List<RecordOrWinnerOlympicXML>} Olympic record
     */
    public void setXmlOlympicRecord(List<OlympicRecordXML> xmlOlympicRecord) {
        this.xmlOlympicRecord = xmlOlympicRecord;
    }

    /**
     * Get XML winner olympic
     * @return List<WinnerOlympicXML>
     */
    public List<WinnerOlympicXML> getXmlWinnerOlympic() {
        return xmlWinnerOlympic;
    }

    /**
     * Set XML winner olympic
     * @param xmlWinnerOlympic {List<WinnerOlympicXML>} Winner olympic
     */
    public void setXmlWinnerOlympic(List<WinnerOlympicXML> xmlWinnerOlympic) {
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
}