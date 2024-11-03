package Models;

import java.util.Date;

public class Result {
    private int idResult;
    private int idSport;
    private int idAthlete = 0;
    private int idTeam = 0;
    private java.sql.Date date;
    private String result;
    private Local local;

    /**
     * Constructor of Result
     * @param idResult {int} Result ID
     * @param idSport {int} Sport ID
     * @param idAthlete {int} Athlete ID
     * @param idTeam {int} Team ID
     * @param date {Date} Date
     * @param result {String} Result
     * @param local {Local} Local
     */
    public Result(int idResult, int idSport, int idAthlete, int idTeam, Date date, String result, Local local) {
        this.idResult = idResult;
        this.idSport = idSport;
        this.idAthlete = idAthlete;
        this.idTeam = idTeam;
        this.date = (java.sql.Date) date;
        this.result = result;
        this.local = local;
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
     * Get Athlete ID
     * @return int
     */
    public int getIdAthlete() {
        return idAthlete;
    }

    /**
     * Set Athlete ID
     * @param idAthlete {int} Athlete ID
     */
    public void setIdAthlete(int idAthlete) {
        this.idAthlete = idAthlete;
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
     * Get Date
     *
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
}
