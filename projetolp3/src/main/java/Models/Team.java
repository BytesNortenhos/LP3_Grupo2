package Models;

import jakarta.xml.bind.annotation.*;

import java.util.List;

public class Team {
    private int idTeam;
    private String name;
    private Country country;
    private Gender genre;
    private Sport sport;
    private int yearFounded;

    /**
     * Constructor of Team
     * @param idTeam {int} Team ID
     * @param name {String} Name
     * @param country {Country} Country
     * @param genre {Gender} Genre
     * @param sport {Sport} Sport
     * @param yearFounded {int} Year of foundation
     */
    public Team(int idTeam, String name, Country country, Gender genre, Sport sport, int yearFounded) {
        this.idTeam = idTeam;
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.sport = sport;
        this.yearFounded = yearFounded;
    }

    /**
     * Get Team ID
     * @return int
     */
    public int getIdTeam() {
        return idTeam;
    }

    /**
     * Set Team ID
     * @param idTeam {int} Team ID
     */
    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
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
    public Country getCountry() {
        return country;
    }

    /**
     * Set country
     * @param country {String} Country
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
     * Set genre
     * @param genre {Gender} Genre
     */
    public void setGenre(Gender genre) {
        this.genre = genre;
    }

    /**
     * Get Sport
     * @return Sport
     */
    public Sport getSport() {
        return sport;
    }

    /**
     * Set sport
     * @param sport {Sport} Sport
     */
    public void setSport(Sport sport) {
        this.sport = sport;
    }

    /**
     * Get year of foundation
     * @return int
     */
    public int getYearFounded() {
        return yearFounded;
    }

    /**
     * Set year of foundation
     * @param yearFounded {int} Year of foundation
     */
    public void setYearFounded(int yearFounded) {
        this.yearFounded = yearFounded;
    }
}