package Models;

public class RecordOrWinner {
    private Sport sport;
    private int year;
    private Athlete athlete;
    private Team team;

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