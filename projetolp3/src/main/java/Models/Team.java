package Models;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private int idTeam;
    private String name;
    private Country country;
    private Gender genre;
    private Sport sport;
    private int yearFounded;
    private int idSport;
    private int minParticipants;
    private int maxParticipants;
    private ArrayList<TeamList> teamList;

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
     * Constructor of Team
     * @param idTeam {int} Team ID
     * @param name {String} Name
     * @param country {Country} Country
     * @param genre {Gender} Genre
     * @param sport {Sport} Sport
     * @param yearFounded {int} Year of foundation
     * @param minParticipants {int} Minimum number of participants
     * @param maxParticipants {int} Maximum number of participants
     * @param teamList {ArrayList<TeamList>} TeamList
     */
    public Team(int idTeam, String name, Country country, Gender genre, Sport sport, int yearFounded, int minParticipants, int maxParticipants, ArrayList<TeamList> teamList) {
        this.idTeam = idTeam;
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.sport = sport;
        this.yearFounded = yearFounded;
        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
        this.teamList = teamList;
    }

    /**
     * Constructor of Team
     * @param idTeam {int} Team ID
     * @param name {String} Name
     * @param country {Country} Country
     * @param genre {Gender} Genre
     * @param sport {Sport} Sport
     * @param yearFounded {int} Year of foundation
     * @param minParticipants {int} Minimum number of participants
     * @param maxParticipants {int} Maximum number of participants
     */
    public Team(int idTeam, String name, Country country, Gender genre, Sport sport, int yearFounded, int minParticipants, int maxParticipants) {
        this.idTeam = idTeam;
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.sport = sport;
        this.yearFounded = yearFounded;
        this.minParticipants = minParticipants;
        this.maxParticipants = maxParticipants;
    }

    /**
     * Constructor of Team
     * @param idTeam {int} Team ID
     * @param name {String} Name
     * @param country {Country} Country
     * @param genre {Gender} Genre
     * @param idSport {int} Sport ID
     * @param yearFounded {int} Year of foundation
     */
    public Team(int idTeam, String name, Country country, Gender genre, int idSport, int yearFounded) {
        this.idTeam = idTeam;
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.idSport = idSport; // Atribui apenas o id do esporte
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

    /**
     * Get Sport ID
     * @return int
     */
    public int getIdSport() {
        return idSport;
    }

    /**
     * Set Sport ID
     * @param idSport {int} Sport ID
     */
    public void setIdSport(int idSport) {
        this.idSport = idSport;
    }

    /**
     * Get minimum number of participants
     * @return int
     */
    public int getMinParticipants() {
        return minParticipants;
    }

    /**
     * Set minimum number of participants
     * @param minParticipants {int} Minimum number of participants
     */
    public void setMinParticipants(int minParticipants) {
        this.minParticipants = minParticipants;
    }

    /**
     * Get maximum number of participants
     * @return int
     */
    public int getMaxParticipants() {
        return maxParticipants;
    }

    /**
     * Set maximum number of participants
     * @param maxParticipants {int} Maximum number of participants
     */
    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    /**
     * Get TeamList
     * @return ArrayList<TeamList>
     */
    public ArrayList<TeamList> getTeamList() {
        return teamList;
    }

    /**
     * Set TeamList
     * @param teamList {ArrayList<TeamList>} TeamList
     */
    public void setTeamList(ArrayList<TeamList> teamList) {
        this.teamList = teamList;
    }
    @Override
    public String toString() {
        return "Team { idTeam: " + idTeam +
                ", name: '" + name + '\'' +
                ", genre: " + (genre != null ? genre.getIdGender() + " - " + genre.getDesc() : "N/A") +
                ", sport: " + (sport != null ? sport.getIdSport() + " - " + sport.getName() : "N/A") +
                " }";
    }

}