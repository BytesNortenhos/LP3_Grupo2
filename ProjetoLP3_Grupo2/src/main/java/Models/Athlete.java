package Models;

import java.util.List;

public class Athlete {
    private String name;
    private String country;
    private String genre;
    private int height;
    private float weight;
    private String dateOfBirth;
    private List<Participation> olympicParticipations;
    private String accountID;


    public Athlete(String name, String country, String genre, int height, float weight, String dateOfBirth, List<Participation> olympicParticipations) {
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Participation> getOlympicParticipations() {
        return olympicParticipations;
    }

    public void setOlympicParticipations(List<Participation> olympicParticipations) {
        this.olympicParticipations = olympicParticipations;
    }
}
