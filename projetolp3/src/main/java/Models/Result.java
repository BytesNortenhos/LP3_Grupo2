package Models;

import java.util.Date;

public class Result {
    private int idResult;
    private Sport sport;
    private Athlete athlete;
    private Team team;
    private java.sql.Date date;
    private String result;
    private Local local;
    private int position;

    /**
     * Constructor of Result
     * @param idResult {int} Result ID
     * @param sport {Sport} Sport
     * @param athlete {Athlete} Athlete
     * @param team {Team} Team
     * @param date {Date} Date
     * @param result {String} Result
     * @param local {Local} Local
     */
    public Result(int idResult, Sport sport, Athlete athlete, Team team, Date date, String result, Local local, int position) {
        this.idResult = idResult;
        this.sport = sport;
        this.athlete = athlete;
        this.team = team;
        this.date = (java.sql.Date) date;
        this.result = result;
        this.local = local;
        this.position = position;
    }

    /**
     * Get Result ID
     * @return int
     */
    public int getIdResult() {
        return idResult;
    }

    /**
     * Set Result ID
     * @param idResult {int} Result ID
     */
    public void setIdResult(int idResult) {
        this.idResult = idResult;
    }

    /**
     * Get Sport
     * @return Sport
     */
    public Sport getSport() {
        return sport;
    }

    /**
     * Set Sport
     * @param sport {Sport} Sport
     */
    public void setSport(Sport sport) {
        this.sport = sport;
    }

    /**
     * Get Athlete
     * @return Athlete
     */
    public Athlete getAthlete() {
        return athlete;
    }

    /**
     * Set Athlete
     * @param athlete {Athlete} Athlete
     */
    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    /**
     * Get Team
     * @return Team
     */
    public Team getTeam() {return team;}

    /**
     * Set Team
     * @param team {Team} Team
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Get Date
     * @return Date
     */
    public java.sql.Date getDate() {
        return date;
    }

    /**
     * Set Date
     * @param date {Date} Date
     */
    public void setDate(Date date) {
        this.date = (java.sql.Date) date;
    }

    /**
     * Get Result
     * @return String
     */
    public String getResult() {
        return result;
    }

    /**
     * Set Result
     * @param result {String} Result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Get Local
     * @return Local
     */
    public Local getLocal() {
        return local;
    }

    /**
     * Set Local
     * @param local {Local} local
     */
    public void setLocal(Local local) {
        this.local = local;
    }

    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }
}
