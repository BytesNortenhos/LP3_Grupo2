package Models;

public class Registration {
    private int idRegistration;
    private int idAthlete = 0;
    private int idTeam = 0;
    private int idSport;
    private RegistrationStatus status;

    /**
     * Constructor of Registration
     * @param idRegistration {int} Registration ID
     * @param idAthlete {int} Athlete ID
     * @param idTeam {int} Team ID
     * @param idSport {int} Sport ID
     * @param status {String} Status
     */
    public Registration(int idRegistration, int idAthlete, int idTeam, int idSport, RegistrationStatus status) {
        this.idRegistration = idRegistration;
        this.idAthlete = idAthlete;
        this.idTeam = idTeam;
        this.idSport = idSport;
        this.status = status;
    }

    /**
     * Get Registration ID
     * @return int
     */
    public int getIdRegistration() {
        return idRegistration;
    }

    /**
     * Set Registration ID
     * @param idRegistration {int} Registration ID
     */
    public void setIdRegistration(int idRegistration) {
        this.idRegistration = idRegistration;
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
     * Get Status
     * @return RegistrationStatus
     */
    public RegistrationStatus getStatus() {
        return status;
    }

    /**
     * Set Status
     * @param status {RegistrationStatus} status
     */
    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }
}
