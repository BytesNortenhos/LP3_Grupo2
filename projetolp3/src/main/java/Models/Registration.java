package Models;

public class Registration {
    private int idRegistration;
    private Athlete athlete;
    private Team team;
    private Sport sport;

    private int year;
    private RegistrationStatus status;

    /**
     * Constructor of Registration
     * @param idRegistration {int} Registration ID
     * @param athlete {Athlete} Athlete
     * @param team {Team} Team
     * @param sport {Sport} Sport
     * @param status {RegistrationStatus} Status
     */
    public Registration(int idRegistration, Athlete athlete, Team team, Sport sport, RegistrationStatus status) {
        this.idRegistration = idRegistration;
        this.athlete = athlete;
        this.team = team;
        this.sport = sport;
        this.status = status;
    }

    /**
     * Constructor of Registration
     * @param idRegistration {int} Registration ID
     * @param athlete {Athlete} Athlete
     * @param team {Team} Team
     * @param sport {Sport} Sport
     * @param status {RegistrationStatus} Status
     * @param year {int} Year of the Registration
     */
    public Registration(int idRegistration, Athlete athlete, Team team, Sport sport, RegistrationStatus status, int year) {
        this.idRegistration = idRegistration;
        this.athlete = athlete;
        this.team = team;
        this.sport = sport;
        this.status = status;
        this.year = year;
    }

    /**
     * Constructor of Registration
     * @param idRegistration {int} Registration ID
     * @param athlete {Athlete} Athlete
     * @param sport {Sport} Sport
     * @param status {RegistrationStatus} Status
     * @param year {int} Year of the Registration
     */
    public Registration(int idRegistration, Athlete athlete, Sport sport, RegistrationStatus status, int year) {
        this.idRegistration = idRegistration;
        this.athlete = athlete;
        this.sport = sport;
        this.status = status;
        this.year = year;
    }

    /**
     * Constructor of Registration
     * @param idRegistration {int} Registration ID
     * @param athlete {Athlete} Athlete
     * @param sport {Sport} Sport
     * @param status {RegistrationStatus} Status
     */
    public Registration(int idRegistration, Athlete athlete, Sport sport, RegistrationStatus status) {
        this.idRegistration = idRegistration;
        this.athlete = athlete;
        this.sport = sport;
        this.status = status;
    }

    /**
     * Constructor of Registration
     * @param idRegistration {int} Registration ID
     * @param athlete {Athlete} Athlete
     * @param team {Team} Team
     * @param status {RegistrationStatus} Status
     * @param year {int} Year of the Registration
     */
    public Registration(int idRegistration, Athlete athlete, Team team, RegistrationStatus status, int year) {
        this.idRegistration = idRegistration;
        this.athlete = athlete;
        this.team = team;
        this.status = status;
        this.year = year;
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
    public Team getTeam() {
        return team;
    }

    /**
     * Set Team
     * @param team {Team} Team
     */
    public void setTeam(Team team) {
        this.team = team;
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
