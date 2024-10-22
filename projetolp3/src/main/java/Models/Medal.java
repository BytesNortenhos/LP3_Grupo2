package Models;

public class Medal extends MedalType {
    private int idMedal;
    private int idAthlete = 0;
    private int idTeam = 0;
    private int year;

    /**
     * Constructor of Medal for Athlete
     * @param idMedalType {int} Medal Type ID
     * @param descMedalType {String} Medal Type Desc
     * @param idMedal {int} Medal ID
     * @param idAthlete {int} Athlete ID
     * @param year {int} Year
     */
    public Medal(int idMedalType, String descMedalType, int idMedal, int idAthlete, int year) {
        super(idMedalType, descMedalType);
        this.idMedal = idMedal;
        this.idAthlete = idAthlete;
        this.year = year;
    }

    /**
     * Constructor of Medal for Team
     * @param idMedalType {int} Medal Type ID
     * @param descMedalType {String} Medal Type Desc
     * @param idMedal {int} Medal ID
     * @param idTeam {int} Athlete ID
     * @param year {int} Year
     */
    public Medal(int idMedal, int idTeam, int year, int idMedalType, String descMedalType) {
        super(idMedalType, descMedalType);
        this.idMedal = idMedal;
        this.idTeam = idTeam;
        this.year = year;
    }

    /**
     * Get Medal ID
     * @return int
     */
    public int getIdMedal() {
        return idMedal;
    }

    /**
     * Set Medal ID
     * @param idMedal {int} Medal ID
     */
    public void setIdMedal(int idMedal) {
        this.idMedal = idMedal;
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
     * @param idTeam int
     */
    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    /**
     * Get Year
     * @return int
     */
    public int getYear() {
        return year;
    }

    /**
     * Set Year
     * @param year {int} Year
     */
    public void setYear(int year) {
        this.year = year;
    }
}
