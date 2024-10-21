package Models;

import java.util.List;

public class Team {
    private String name;
    private String country;
    private String genre;
    private String sport;
    private int foundationYear;
    private List<Participation> olympicParticipations;

    /**
     * Constructor of Team
     * @param name {String} Name
     * @param country {String} Country
     * @param genre {String} Genre
     * @param sport {String} Sport
     * @param foundationYear {int} Year of foundation
     * @param olympicParticipations {List<Participation>} List of participations
     */
    public Team(String name, String country, String genre, String sport, int foundationYear, List<Participation> olympicParticipations) {
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.sport = sport;
        this.foundationYear = foundationYear;
        this.olympicParticipations = olympicParticipations;
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
     * @param name {String} Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get country
     * @return String
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set country
     * @param country {String} Country
     */
    public void setCountry(String country) {
        this.country = country;
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
     * @param genre {String} Genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Get sport
     * @return String
     */
    public String getSport() {
        return sport;
    }

    /**
     * Set sport
     * @param sport {String} Sport
     */
    public void setSport(String sport) {
        this.sport = sport;
    }

    /**
     * Get year of foundation
     * @return int
     */
    public int getFoundationYear() {
        return foundationYear;
    }

    /**
     * Set year of foundation
     * @param foundationYear {int} Year of foundation
     */
    public void setFoundationYear(int foundationYear) {
        this.foundationYear = foundationYear;
    }

    /**
     * Get olympic participationa
     * @return List<Participation>
     */
    public List<Participation> getOlympicParticipations() {
        return olympicParticipations;
    }

    /**
     * Set olympic participations
     * @param olympicParticipations {List<Participation>} Olympic participations
     */
    public void setOlympicParticipations(List<Participation> olympicParticipations) {
        this.olympicParticipations = olympicParticipations;
    }
}