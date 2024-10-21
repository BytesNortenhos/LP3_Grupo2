package Models;

import java.util.Date;
import java.util.List;

public class Athlete extends Person {
    private String name;
    private String country;
    private String genre;
    private int height;
    private float weight;
    private Date dateOfBirth;
    private List<Participation> olympicParticipations;

    /**
     * Constructor of Athlete
     * @param id {int} ID
     * @param password {String} Password
     * @param name {String} Name
     * @param country {String} Country
     * @param genre {String} Genre
     * @param height {int} Height
     * @param weight {float} Weight
     * @param dateOfBirth {Date} Date of birth
     * @param olympicParticipations {List<Participation>} List of olympic participations
     */
    public Athlete(int id, String password, String name, String country, String genre, int height, float weight, Date dateOfBirth, List<Participation> olympicParticipations) {
        super(id, password);
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
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
     * Get height
     * @return int
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set height
     * @param height {int} Height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Get weight
     * @return float
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Set weight
     * @param weight {float} Weight
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Get date of birth
     * @return Date
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Set date of birth
     * @param dateOfBirth {Date} Date of birth
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Get olympic participations
     * @return List<Participation>
     */
    public List<Participation> getOlympicParticipations() {
        return olympicParticipations;
    }

    /**
     * Set olympic participations
     * @param olympicParticipations {List<Participation>} List of Olympic Participations
     */
    public void setOlympicParticipations(List<Participation> olympicParticipations) {
        this.olympicParticipations = olympicParticipations;
    }
}
