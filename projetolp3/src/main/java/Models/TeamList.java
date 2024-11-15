package Models;

public class TeamList {
    private int idTeamList;
    private int idTeam;
    private Athlete athlete;
    private TeamListStatus status;
    private int year;
    private boolean isActive;

    /**
     * Constructor of TeamList
     * @param idTeamList {int} TeamList ID
     * @param idTeam {int} Team ID
     * @param athlete {Athlete} Athlete
     * @param status {TeamListStatus} Status
     * @param year {int} Year
     * @param isActive {boolean} Is active
     */
    public TeamList(int idTeamList, int idTeam, Athlete athlete, TeamListStatus status, int year, boolean isActive) {
        this.idTeamList = idTeamList;
        this.idTeam = idTeam;
        this.athlete = athlete;
        this.status = status;
        this.year = year;
        this.isActive = isActive;
    }

    /**
     * Get TeamList ID
     * @return int
     */
    public int getIdTeamList() {
        return idTeamList;
    }

    /**
     * Set TeamList ID
     * @param idTeamList {int} TeamList ID
     */
    public void setIdTeamList(int idTeamList) {
        this.idTeamList = idTeamList;
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
     * Get Status
     * @return TeamListStatus
     */
    public TeamListStatus getStatus() {
        return status;
    }

    /**
     * Set Status
     * @param status {TeamListStatus} Status
     */
    public void setStatus(TeamListStatus status) {
        this.status = status;
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
     * Get is active
     * @return boolean
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Set is active
     * @param active {boolean} Is active
     */
    public void setActive(boolean active) {
        isActive = active;
    }
}
