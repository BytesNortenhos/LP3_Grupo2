package Models;

import java.util.List;
import java.util.ArrayList;

public class Team {
    private String name;
    private String country;
    private String genre;
    private String sport;
    private int foundationYear;
    private List<Participation> olympicParticipations;

    public Team(String name, String country, String genre, String sport, int foundationYear, List<Participation> olympicParticipations) {
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.sport = sport;
        this.foundationYear = foundationYear;
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

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public int getFoundationYear() {
        return foundationYear;
    }

    public void setFoundationYear(int foundationYear) {
        this.foundationYear = foundationYear;
    }

    public List<Participation> getOlympicParticipations() {
        return olympicParticipations;
    }

    public void setOlympicParticipations(List<Participation> olympicParticipations) {
        this.olympicParticipations = olympicParticipations;
    }
}