package Models;

import jakarta.xml.bind.annotation.*;

import java.util.List;

public class Sport {
    private int idSport;
    private String type;
    private Gender genre;
    private String name;
    private String desc;
    private int minParticipants;
    private String scoringMeasure;
    private String oneGame;
    private int resultMin;

    private int resultMax;

    private OlympicRecord olympicRecord;
    private List<WinnerOlympic> winnerOlympic;
    private List<Rule> rules;

    private int idGender; // Atributo para armazenar apenas o id do gênero

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

    public Sport(int idSport, String type, Gender genre, String name, String desc, int minParticipants,
                 String scoringMeasure, String oneGame, OlympicRecord olympicRecord, List<WinnerOlympic> winnerOlympic,
                 List<Rule> rules, int resultMin, int resultMax) {
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
        this.resultMin = resultMin;
        this.resultMax = resultMax;
    }

    public Sport(int idSport, String type, int idGender, String name, String desc, int minParticipants,
                 String scoringMeasure, String oneGame, OlympicRecord olympicRecord, List<WinnerOlympic> winnerOlympic,
                 List<Rule> rules) {
        this.idSport = idSport;
        this.type = type;
        this.idGender = idGender;
        this.name = name;
        this.desc = desc;
        this.minParticipants = minParticipants;
        this.scoringMeasure = scoringMeasure;
        this.oneGame = oneGame;
        this.olympicRecord = olympicRecord;
        this.winnerOlympic = winnerOlympic;
        this.rules = rules;
    }
    // Constructor including the description
    public Sport(int idSport, String name, String type, Gender genre, String desc) {
        this.idSport = idSport;
        this.name = name;
        this.type = type;
        this.genre = genre;
        this.desc = desc;  // Initialize description
    }
    public Sport(String type, Gender genre, String name, String desc, int minParticipants,
                 String scoringMeasure, String oneGame) {
        this.type = type;
        this.genre = genre;
        this.name = name;
        this.desc = desc;
        this.minParticipants = minParticipants;
        this.scoringMeasure = scoringMeasure;
        this.oneGame = oneGame;
    }
    public Sport(int idSport, String type, Gender genre, String name, String desc, int minParticipants,
                 String scoringMeasure, String oneGame, int resultMin, int resultMax) {
        this.type = type;
        this.genre = genre;
        this.name = name;
        this.desc = desc;
        this.minParticipants = minParticipants;
        this.scoringMeasure = scoringMeasure;
        this.oneGame = oneGame;
        this.resultMin = resultMin;
        this.resultMax = resultMax;
    }
    /**
     * Constructor of Sport with essential details.
     * @param idSport {int} Sport ID
     * @param name {String} Name of the Sport
     * @param type {String} Type of the Sport
     * @param genre {Gender} Genre of the Sport
     */
    public Sport(int idSport, String name, String type, Gender genre) {
        this.idSport = idSport;
        this.name = name;
        this.type = type;
        this.genre = genre;
    }
    /**
     * Constructor for creating a Sport object with essential details.
     * This constructor initializes a Sport with the provided idSport and name.
     * All other attributes are left with default values (e.g., null or empty).
     *
     * @param idSport {int} The ID of the sport.
     * @param name {String} The name of the sport.
     */
    public Sport(int idSport, String name, String type) {
        this.idSport = idSport;
        this.name = name;
        this.type = type;
    }
    // Construtor atualizado para incluir scoringMeasure
    public Sport(int idSport, String name, String type, Gender genre, String desc, String scoringMeasure) {
        this.idSport = idSport;
        this.name = name;
        this.type = type;
        this.genre = genre;
        this.desc = desc;
        this.scoringMeasure = scoringMeasure;  // Atribuição do scoringMeasure
    }

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
    public int getResultMin() {
        return resultMin;
    }

    public void setResultMin(int resultMin) {
        this.resultMin = resultMin;
    }

    public int getResultMax() {
        return resultMax;
    }

    public void setResultMax(int resultMax) {
        this.resultMax = resultMax;
    }


    /**
     * Set rules
     * @param rules {List<Rule>} List of rules
     */

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }
    @Override
    public String toString() {
        return String.format("Sport: {ID: %d, Type: %s, Gender: %s, Name: %s}",
                idSport, type, (genre != null ? genre.getDesc() : "Unknown"), name);
    }  }
