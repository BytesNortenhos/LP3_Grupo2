package Models;

public class WinnerOlympic extends RecordOrWinner {
    private int timeMS;
    private Medal medal;

    /**
     * Constructor of WinnerOlympic
     * @param sport {Sport} Sport
     * @param year {int} Year
     * @param athlete {Athlete} Athlete
     * @param team {Team} Team
     * @param timeMS {int} Time in ms
     * @param medal {Medal} Medal
     */
    public WinnerOlympic(Sport sport, int year, Athlete athlete, Team team, int timeMS, Medal medal) {
        super(sport, year, athlete, team);
        this.timeMS = timeMS;
        this.medal = medal;
    }

    /**
     * Get time in ms
     * @return int
     */
    public int getTimeMS() {
        return timeMS;
    }

    /**
     * Set time in ms
     * @param timeMS {int} Time in ms
     */
    public void setTimeMS(int timeMS) {
        this.timeMS = timeMS;
    }

    /**
     * Get medal
     * @return Medal
     */
    public Medal getMedal() {
        return medal;
    }

    /**
     * Set medal
     * @param medal {Medal} Medal
     */
    public void setMedal(Medal medal) {
        this.medal = medal;
    }
}