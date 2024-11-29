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
    public Athlete(int id, String password, String name, int height, float weight, Date dateOfBirth) {
        super(id, password);
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
    }
    public Athlete(int id,  String name, int height, float weight, Date dateOfBirth) {
        super(id);
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
    }
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
    public Athlete(int id, String name, String country) throws SQLException {
        super(id);
        this.name = name;
        CountryDao countryDao = new CountryDao();
        this.country = countryDao.getCountryById(country);
    }
    public int getIdAthlete() {
        return super.getId();
    }

    public void setIdAthlete(int id) {
        super.setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Gender getGenre() {
        return genre;
    }

    public String getImage() {
        return image;
    }

    public void setGenre(Gender genre) {
        this.genre = genre;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public java.sql.Date getDateOfBirth() {
        return (java.sql.Date) dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
