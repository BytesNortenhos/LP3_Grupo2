package Models;

import java.util.Date;
import java.util.List;

public class Athlete extends Person {
    private String name;
    private Country country;
    private Gender genre;
    private int height;
    private float weight;
    private Date dateOfBirth;

    /**
     * Constructor of Athlete
     * @param id {int} ID
     * @param password {String} Password
     * @param name {String} Name
     * @param country {Country} Country
     * @param genre {Gender} Genre
     * @param height {int} Height
     * @param weight {float} Weight
     * @param dateOfBirth {Date} Date of birth
     */
    public Athlete(int id, String password, String name, Country country, Gender genre, int height, float weight, Date dateOfBirth) {
        super(id, password);
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
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
     * @return Country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Set country
     * @param country {Country} Country
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * Get genre
     * @return String
     */
    public Gender getGenre() {
        return genre;
    }

    /**
     * Set genre
     * @param genre {String} Genre
     */
    public void setGenre(Gender genre) {
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
}
