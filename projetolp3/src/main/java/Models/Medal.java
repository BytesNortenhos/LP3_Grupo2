package Models;

public class Medal {
    private int idMedal;
    private Athlete athlete;
    private Team team;
    private int year;
    private MedalType medalType;

    private int idAthlete;  // Armazena o ID do atleta
    private int idTeam;      // Armazena o ID do time
    /**
     * Constructor of Medal for Athlete
     * @param idMedal {int} Medal ID
     * @param athlete {Athlete} Athlete
     * @param team {Team} Team
     * @param year {int} Year
     * @param medalType {MedalType} MedalType
     */
    public Medal(int idMedal, Athlete athlete, Team team, int year, MedalType medalType) {
        this.idMedal = idMedal;
        this.athlete = athlete;
        this.team = team;
        this.year = year;
        this.medalType = medalType;
    }
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
     * Get Athlete
     * @return Athlete
     */
    public Athlete getAthlete() {
        return athlete;
    }

    /**
     * Set Athlete
     * @param athlete {Athlete}
     */
    public void setAthlete(Athlete athlete) {
        this.athlete = athlete;
    }

    /**
     * Get Team
     * @return Team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Set Team
     * @param team {Team}
     */
    public void setTeam(Team team) {
        this.team = team;
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