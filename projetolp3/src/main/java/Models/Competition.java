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

    // Getters e Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    public String getScoringMeasure() {
        return scoringMeasure;
    }

    public void setScoringMeasure(String scoringMeasure) {
        this.scoringMeasure = scoringMeasure;
    }

    public String getOneGame() {
        return oneGame;
    }

    public void setOneGame(String oneGame) {
        this.oneGame = oneGame;
    }

    public OlympicRecord getOlympicRecord() {
        return olympicRecord;
    }

    public void setOlympicRecord(OlympicRecord olympicRecord) {
        this.olympicRecord = olympicRecord;
    }

    public List<WinnerOlympic> getWinnerOlympic() {
        return winnerOlympic;
    }

    public void setWinnerOlympic(List <WinnerOlympic> winnerOlympic) {
        this.winnerOlympic = winnerOlympic;
    }

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }
}