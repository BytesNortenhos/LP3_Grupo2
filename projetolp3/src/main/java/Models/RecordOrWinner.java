package Models;

public class RecordOrWinner {
    private int idSport;
    private int year;
    private int idAthlete;
    private int idTeam;

    /**
     * Constructor of RecordOrWinner
     * @param idSport Sport ID
     * @param year Year
     * @param idAthlete Athlete ID
     * @param idTeam Team ID
     */
    public RecordOrWinner(int idSport, int year, int idAthlete, int idTeam) {
        this.idSport = idSport;
        this.year = year;
        this.idAthlete = idAthlete;
        this.idTeam = idTeam;
    }

    /**
     * Constructor of RecordOrWinner (without parameters)
     */
    public RecordOrWinner() {

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
     * Get year
     * @return int
     */
    public int getYear() {
        return year;
    }

    /**
     * Set year
     * @param year {int} Year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Get Athlete ID
     * @return int
     */
    public int getIdAthlete() {
        return idAthlete;
    }

    /**
     * Set Atlhete ID
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
}