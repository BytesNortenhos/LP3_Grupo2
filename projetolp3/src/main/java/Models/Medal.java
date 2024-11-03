package Models;

public class Medal {
    private int idMedal;
    private int idAthlete = 0;
    private int idTeam = 0;
    private int year;
    private MedalType medalType;

    /**
     * Constructor of Medal for Athlete
     * @param idMedal {int} Medal ID
     * @param idAthlete {int} Athlete ID
     * @param idTeam {int} Team ID
     * @param year {int} Year
     * @param medalType {MedalType} MedalType
     */
    public Medal(int idMedal, int idAthlete, int idTeam, int year, MedalType medalType) {
        this.idMedal = idMedal;
        this.idAthlete = idAthlete;
        this.idTeam = idTeam;
        this.year = year;
        this.medalType = medalType;
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

    /**
     * Get MedalType
     * @return MedalType
     */
    public MedalType getMedalType() {
        return medalType;
    }

    /**
     * Set MedalType
     * @param medalType {MedalType}
     */
    public void setMedalType(MedalType medalType) {
        this.medalType = medalType;
    }
}
