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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
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

    public Date getDateOfBirth() {
        return dateOfBirth;
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
}
