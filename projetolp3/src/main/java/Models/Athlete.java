package Models;
import AuxilierXML.*;
import Dao.CountryDao;
import Dao.GenderDao;
import jakarta.xml.bind.annotation.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Athlete extends Person {
    private String name;
    private int height;
    private float weight;
    private Date dateOfBirth;
    private Country country;
    private Gender genre;
    private String image;

    /**
     * Constructor of Athlete
     * @param id {int} Id
     * @param password {String} Password
     * @param name {String} Name
     * @param country {Country} Country
     * @param genre {Gender} Genre
     * @param height {int} Height
     * @param weight {float} Weight
     * @param dateOfBirth {Date} Date of birth
     */
    public Athlete(int id, String password, String name, Country country, Gender genre, int height, float weight, Date dateOfBirth, String image) {
        super(id, password);
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
        this.image = image;
    }

    /**
     * Constructor of Athlete
     * @param id {int} Id
     * @param password {String} Password
     * @param name {String} Name
     * @param height {int} Height
     * @param weight {float} Weight
     * @param dateOfBirth {Date} Date of birth
     */
    public Athlete(int id, String password, String name, int height, float weight, Date dateOfBirth) {
        super(id, password);
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Constructor of Athlete
     * @param id {int} Id
     * @param name {String} Name
     * @param height {int} Height
     * @param weight {float} Weight
     * @param dateOfBirth {Date} Date of birth
     */
    public Athlete(int id,  String name, int height, float weight, Date dateOfBirth) {
        super(id);
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Constructor of Athlete
     * @param id {int} Id
     * @param name {String} Name
     * @param height {int} Height
     * @param weight {float} Weight
     * @param dateOfBirth {Date} Date of birth
     * @param idCountry {String} Id country
     * @param idGender {int} Gender ID
     */
    public Athlete(int id, String name, int height, float weight, Date dateOfBirth, String idCountry, int idGender) throws SQLException {
        super(id);
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
        CountryDao countryDao = new CountryDao();
        this.country = countryDao.getCountryById(idCountry);
        GenderDao genderDao = new GenderDao();
        this.genre = genderDao.getGenderById(idGender);
    }

    /**
     * Constructor of Athlete
     * @param id {int} Id
     * @param name {String} Name
     * @param country {String} Country
     * @throws SQLException
     */
    public Athlete(int id, String name, String country) throws SQLException {
        super(id);
        this.name = name;
        CountryDao countryDao = new CountryDao();
        this.country = countryDao.getCountryById(country);
    }

    /**
     * Get ID of the athlete
     * @return int
     */
    public int getIdAthlete() {
        return super.getId();
    }

    /**
     * Set ID of the athlete
     * @param id {int} ID
     */
    public void setIdAthlete(int id) {
        super.setId(id);
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
     * @return Gender
     */
    public Gender getGenre() {
        return genre;
    }

    /**
     * Get image
     * @return String
     */
    public String getImage() {
        return image;
    }

    /**
     * Set image
     * @param image {String} Image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Set genre
     * @param genre {Gender} Genre
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
