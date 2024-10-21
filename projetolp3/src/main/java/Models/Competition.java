package Models;

import java.util.List;

public class Competition {
    private String type;
    private String genre;
    private String name;
    private String description;
    private int minParticipants;
    private String scoringMeasure;
    private String oneGame;
    private OlympicRecord olympicRecord;
    private List<WinnerOlympic> winnerOlympic;
    private List<String> rules;

    /**
     * Constructor of competition
     * @param type {String} Type
     * @param genre {String} Genre
     * @param name {String} Name
     * @param description {String} Description
     * @param minParticipants {int} Number minimum of participants
     * @param scoringMeasure {String} Measure of scoring
     * @param oneGame {String} "One Game"
     * @param olympicRecord {OlympicRecord} Olympic record
     * @param winnerOlympic {List<WinnerOlympic>} List of olympic winners
     * @param rules {List<String>} List of rules
     */
    public Competition(String type, String genre, String name, String description, int minParticipants,
                       String scoringMeasure, String oneGame, OlympicRecord olympicRecord, List<WinnerOlympic> winnerOlympic,
                       List<String> rules) {
        this.type = type;
        this.genre = genre;
        this.name = name;
        this.description = description;
        this.minParticipants = minParticipants;
        this.scoringMeasure = scoringMeasure;
        this.oneGame = oneGame;
        this.olympicRecord = olympicRecord;
        this.winnerOlympic = winnerOlympic;
        this.rules = rules;
    }
    public Competition() {

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
     * @return String
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Set genre
     * @param genre {String} Gênero
     */
    public void setGenre(String genre) {
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
    public String getDescription() {
        return description;
    }

    /**
     * Set description
     * @param description {String} Descrição
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @return List<String>
     */
    public List<String> getRules() {
        return rules;
    }

    /**
     * Set rules
     * @param rules {List<String>} List of rules
     */
    public void setRules(List<String> rules) {
        this.rules = rules;
    }
}