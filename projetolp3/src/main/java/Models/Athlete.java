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
    private List<Participation> olympicParticipations;

    public Athlete(int id, String password, String name, Country country, Gender genre, int height, float weight, Date dateOfBirth, List<Participation> olympicParticipations) {
        super(id, password);
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
        this.olympicParticipations = olympicParticipations;
    }

    public Athlete(int id, String password, String name, Country country, Gender genre, int height, float weight, Date dateOfBirth) {
        super(id, password);
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
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

    public List<Participation> getOlympicParticipations() {
        return olympicParticipations;
    }

    public void setOlympicParticipations(List<Participation> olympicParticipations) {
        this.olympicParticipations = olympicParticipations;
    }
    @Override
    public String toString() {
        return "Athlete{" +
                "id=" + getId() +
                ", name=" + name +
                ", country=" + country.getName() +
                ", height=" + height +
                ", weight=" + weight +
                ", dateOfBirth=" + dateOfBirth +
                ", olympicParticipations=" + olympicParticipations +
                '}';
    }

}