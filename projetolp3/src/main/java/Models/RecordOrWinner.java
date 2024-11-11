package Models;

public class RecordOrWinner {
    private Sport sport;
    private int year;
    private Athlete athlete;
    private Team team;

    private int idSport;     // ID for Sport
    private int idAthlete;   // ID for Athlete
    private int idTeam;      // ID for Team

    /**
     * Constructor of RecordOrWinner
     * @param sport {Sport} Sport
     * @param year {int} Year
     * @param athlete {Athlete} Athlete
     * @param team {Team} Team
     */
    public RecordOrWinner(Sport sport, int year, Athlete athlete, Team team) {
        this.sport = sport;
        this.year = year;
        this.athlete = athlete;
        this.team = team;
    }
    /**
     * New Constructor of RecordOrWinner with IDs instead of objects
     * @param idSport {int} Sport ID
     * @param year {int} Year
     * @param idAthlete {int} Athlete ID
     * @param idTeam {int} Team ID
     */
    public RecordOrWinner(int idSport, int year, int idAthlete, int idTeam) {
        this.idSport = idSport;
        this.year = year;
        this.idAthlete = idAthlete;
        this.idTeam = idTeam;
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
    public void setIdSport(Sport sport) {
        this.sport = sport;
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
     * Get Athlete
     * @return Athlete
     */
    public Athlete getAthlete() {
        return athlete;
    }

    /**
     * Set Atlhete
     * @param athlete {Athlete} Athlete
     */
    public void setIdAthlete(Athlete athlete) {
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
    public void setIdTeam(Team team) {
        this.team = team;
    }
}