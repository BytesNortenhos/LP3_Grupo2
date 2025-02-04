package Models;

import jakarta.xml.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private int idStatus;
    private String metrica;

    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

    private OlympicRecord olympicRecord;
    private List<WinnerOlympic> winnerOlympic;
    private List<Rule> rules;

    private int idLocal;
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
                 String scoringMeasure, String oneGame, String metric, LocalDateTime startDate, LocalDateTime endDate, OlympicRecord olympicRecord, List<WinnerOlympic> winnerOlympic,
                 List<Rule> rules, int idLocal, int resultMin, int resultMax) {
        this.idSport = idSport;
        this.type = type;
        this.genre = genre;
        this.name = name;
        this.desc = desc;
        this.minParticipants = minParticipants;
        this.scoringMeasure = scoringMeasure;
        this.oneGame = oneGame;
        this.metrica = metric;
        this.dataInicio = startDate;
        this.dataFim = endDate;
        this.olympicRecord = olympicRecord;
        this.winnerOlympic = winnerOlympic;
        this.rules = rules;
        this.idLocal = idLocal;
        this.resultMin = resultMin;
        this.resultMax = resultMax;
    }

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
     * @param resultMin {int} Minimum result
     * @param resultMax {int} Maximum result
     */
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

    /**
     * Constructor of Sport
     * @param idSport {int} Sport ID
     * @param type {String} Type
     * @param idGender {int} Gender ID
     * @param name {String} Name
     * @param desc {String} Description
     * @param minParticipants {int} Number minimum of participants
     * @param scoringMeasure {String} Measure of scoring
     * @param oneGame {String} "One Game"
     * @param olympicRecord {OlympicRecord} Olympic record
     * @param winnerOlympic {List<WinnerOlympic>} List of olympic winners
     * @param rules {List<Rule>} List of rules
     */
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

    /**
     * Constructor of Sport
     * @param idSport {int} Sport ID
     * @param type {String} Type
     * @param genre {Gender} Genre
     * @param name {String} Name
     * @param desc {String} Description
     */
    public Sport(int idSport, String name, String type, Gender genre, String desc) {
        this.idSport = idSport;
        this.name = name;
        this.type = type;
        this.genre = genre;
        this.desc = desc;
    }

    /**
     * Constructor of Sport
     * @param type {String} Type
     * @param genre {Gender} Genre
     * @param name {String} Name
     * @param desc {String} Description
     * @param minParticipants {int} Number minimum of participants
     * @param scoringMeasure {String} Measure of scoring
     * @param oneGame {String} "One Game"
     */
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
     * Constructor of Sport with essential details.
     * @param idSport {int} Sport ID
     * @param name {String} Name of the Sport
     * @param type {String} Type of the Sport
     */
    public Sport(int idSport, String name, String type) {
        this.idSport = idSport;
        this.name = name;
        this.type = type;
    }

    /**
     * Constructor of Sport
     * @param idSport {int} Sport ID
     * @param name {String} Name
     * @param type {String} Type
     * @param genre {Gender} Genre
     * @param desc {String} Description
     * @param scoringMeasure {String} Measure of scoring
     */
    public Sport(int idSport, String name, String type, Gender genre, String desc, String scoringMeasure) {
        this.idSport = idSport;
        this.name = name;
        this.type = type;
        this.genre = genre;
        this.desc = desc;
        this.scoringMeasure = scoringMeasure;
    }

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
     * @param resultMin {int} Minimum result
     * @param resultMax {int} Maximum result
     * @param dataInicio {LocalDateTime} Data de início
     * @param dataFim {LocalDateTime} Data de fim
     */
    public Sport(int idSport, String type, Gender genre, String name, String desc, int minParticipants,
                 String scoringMeasure, String oneGame, OlympicRecord olympicRecord, List<WinnerOlympic> winnerOlympic,
                 List<Rule> rules, int resultMin, int resultMax, LocalDateTime dataInicio, LocalDateTime dataFim) {
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
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

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
     * @param resultMin {int} Minimum result
     * @param resultMax {int} Maximum result
     * @param idStatus {int} Status ID
     * @param metrica {String} Metric
     * @param dataInicio {LocalDateTime} Data de início
     * @param dataFim {LocalDateTime} Data de fim
     */
    public Sport(int idSport, String type, Gender genre, String name, String desc, int minParticipants,
                 String scoringMeasure, String oneGame, OlympicRecord olympicRecord, List<WinnerOlympic> winnerOlympic,
                 List<Rule> rules, int resultMin, int resultMax, int idStatus, String metrica, LocalDateTime dataInicio, LocalDateTime dataFim) {
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
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

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
     * @param dataInicio {LocalDateTime} Data de início
     * @param dataFim {LocalDateTime} Data de fim
     */
    public Sport(int idSport, String type, Gender genre, String name, String desc, int minParticipants,
                 String scoringMeasure, String oneGame, OlympicRecord olympicRecord, List<WinnerOlympic> winnerOlympic,
                 List<Rule> rules, LocalDateTime dataInicio, LocalDateTime dataFim) {
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
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    /**
     * Constructor of Sport
     * @param idSport {int} Sport ID
     * @param type {String} Type
     * @param gender {Gender} Gender
     * @param name {String} Name
     * @param description {String} Description
     * @param minParticipants {int} Minimum number of participants
     * @param scoringMeasure {String} Measure of scoring
     * @param oneGame {String} "One Game"
     * @param resultMin {int} Minimum result
     * @param resultMax {int} Maximum result
     */
    public Sport(int idSport, String type, Gender gender, String name, String description, int minParticipants, String scoringMeasure, String oneGame, int resultMin, int resultMax) {
        this.idSport = idSport;
        this.type = type;
        this.genre = gender;
        this.name = name;
        this.desc = description;
        this.minParticipants = minParticipants;
        this.scoringMeasure = scoringMeasure;
        this.oneGame = oneGame;
        this.resultMin = resultMin;
        this.resultMax = resultMax;
    }

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
     * @param resultMin {int} Minimum result
     * @param resultMax {int} Maximum result
     * @param idStatus {int} Status ID
     * @param metrica {String} Metric
     * @param dataInicio {LocalDateTime} Start date
     * @param dataFim {LocalDateTime} End date
     * @param olympicRecord {OlympicRecord} Olympic record
     * @param winnerOlympic {List<WinnerOlympic>} List of olympic winners
     * @param rules {List<Rule>} List of rules
     */
    public Sport(int idSport, String type, Gender genre, String name, String desc, int minParticipants, String scoringMeasure, String oneGame, int resultMin, int resultMax, int idStatus, String metrica, LocalDateTime dataInicio, LocalDateTime dataFim, OlympicRecord olympicRecord, List<WinnerOlympic> winnerOlympic, List<Rule> rules) {
        this.idSport = idSport;
        this.type = type;
        this.genre = genre;
        this.name = name;
        this.desc = desc;
        this.minParticipants = minParticipants;
        this.scoringMeasure = scoringMeasure;
        this.oneGame = oneGame;
        this.resultMin = resultMin;
        this.resultMax = resultMax;
        this.idStatus = idStatus;
        this.metrica = metrica;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.olympicRecord = olympicRecord;
        this.winnerOlympic = winnerOlympic;
        this.rules = rules;
    }

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
     * @param resultMin {int} Minimum result
     * @param resultMax {int} Maximum result
     * @param idStatus {int} Status ID
     * @param metrica {String} Metric
     * @param dataInicio {LocalDateTime} Start date
     * @param dataFim {LocalDateTime} End date
     */
    public Sport(int idSport, String type, Gender genre, String name, String desc, int minParticipants, String scoringMeasure, String oneGame, int resultMin, int resultMax, int idStatus, String metrica, LocalDateTime dataInicio, LocalDateTime dataFim) {
        this.idSport = idSport;
        this.type = type;
        this.genre = genre;
        this.name = name;
        this.desc = desc;
        this.minParticipants = minParticipants;
        this.scoringMeasure = scoringMeasure;
        this.oneGame = oneGame;
        this.resultMin = resultMin;
        this.resultMax = resultMax;
        this.idStatus = idStatus;
        this.metrica = metrica;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

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
     * @param resultMin {int} Minimum result
     * @param resultMax {int} Maximum result
     * @param idStatus {int} Status ID
     * @param metrica {String} Metric
     * @param dataInicio {LocalDateTime} Start date
     * @param dataFim {LocalDateTime} End date
     * @param olympicRecord {OlympicRecord} Olympic record
     * @param winnerOlympic {List<WinnerOlympic>} List of olympic winners
     * @param rules {List<Rule>} List of rules
     * @param idLocal {int} Local ID
     */
    public Sport(int idSport, String type, Gender genre, String name, String desc, int minParticipants, String scoringMeasure, String oneGame, int resultMin, int resultMax, int idStatus, String metrica, LocalDateTime dataInicio, LocalDateTime dataFim, OlympicRecord olympicRecord, List<WinnerOlympic> winnerOlympic, List<Rule> rules, int idLocal) {
        this.idSport = idSport;
        this.type = type;
        this.genre = genre;
        this.name = name;
        this.desc = desc;
        this.minParticipants = minParticipants;
        this.scoringMeasure = scoringMeasure;
        this.oneGame = oneGame;
        this.resultMin = resultMin;
        this.resultMax = resultMax;
        this.idStatus = idStatus;
        this.metrica = metrica;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.olympicRecord = olympicRecord;
        this.winnerOlympic = winnerOlympic;
        this.rules = rules;
        this.idLocal = idLocal;
    }

    /**
     * Constructs a `Sport` object with the sport's ID and name.
     *
     * @param idSport The unique identifier for the sport.
     * @param name The name of the sport.
     */
    public Sport(int idSport, String name) {
        this.idSport = idSport;
        this.name = name;
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

    /**
     * Get Result min
     * @return int
     */
    public int getResultMin() {
        return resultMin;
    }

    /**
     * Set Result min
     * @param resultMin {int} Minimum result
     */
    public void setResultMin(int resultMin) {
        this.resultMin = resultMin;
    }

    /**
     * Get Result max
     * @return int
     */
    public int getResultMax() {
        return resultMax;
    }

    /**
     * Set Result max
     * @param resultMax {int} Maximum result
     */
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

    /**
     * Get Status ID
     * @return int
     */
    public int getIdStatus() {
        return idStatus;
    }

    /**
     * Set Status ID
     * @param idStatus {int} Status ID
     */
    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    /**
     * Get Metric
     * @return String
     */
    public String getMetrica() {
        return metrica;
    }

    /**
     * Set Metric
     * @param metrica {String} Metric
     */
    public void setMetrica(String metrica) {
        this.metrica = metrica;
    }

    /**
     * Get Data de início
     * @return LocalDateTime
     */
    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    /**
     * Set Data de início
     * @param dataInicio {LocalDateTime} Data de início
     */
    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * Get Data de fim
     * @return LocalDateTime
     */
    public LocalDateTime getDataFim() {
        return dataFim;
    }

    /**
     * Set Data de fim
     * @param dataFim {LocalDateTime} Data de fim
     */
    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    /**
     * Override toString method
     * @return String
     */
    @Override
    public String toString() {
        return String.format("Sport: {ID: %d, Type: %s, Gender: %s, Name: %s}",
                idSport, type, (genre != null ? genre.getDesc() : "Unknown"), name);
    }

    /**
     * Get Gender ID
     * @return int
     */
    public int getIdGender() {
        return idGender;
    }

    /**
     * Set Gender ID
     * @param idGender {int} Gender ID
     */
    public void setIdGender(int idGender) {
        this.idGender = idGender;
    }

    /**
     * Get Local ID
     * @return int
     */
    public int getIdLocal() {
        return idLocal;
    }

    /**
     * Set Local ID
     * @param idLocal {int} Local ID
     */
    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

}