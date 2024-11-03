package Models;

import java.util.Date;

public class Athlete extends Person {
    private String name;
    private Country country;
    private Gender gender;
    private int height;
    private float weight;
    private Date dateOfBirth;

    /**
     * Constructor of Athlete
     * @param id {int} ID
     * @param password {String} Password
     * @param name {String} Name
     * @param country {Country} Country
     * @param gender {Gender} Gender
     * @param height {int} Height
     * @param weight {float} Weight
     * @param dateOfBirth {Date} Date of birth
     */
    public Athlete(int id, String password, String name, Country country, Gender gender, int height, float weight, Date dateOfBirth) {
        super(id, password);
        this.name = name;
        this.country = country;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
    }

    public int getIdAthlete() {
        return super.getId();
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
     * Get gender
     * @return String
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Set gender
     * @param gender {String} Gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
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
     *
     * @return Date
     */
    public java.sql.Date getDateOfBirth() {
        return (java.sql.Date) dateOfBirth;
    }

    /**
     * Set date of birth
     * @param dateOfBirth {Date} Date of birth
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

}
