package Models;

public class WinnerOlympic extends RecordOrWinner {
    private int timeMS;
    private Medal medal;

    /**
     * Constructor of WinnerOlympic
     * @param idSport {int} Sport ID
     * @param year {int} Year
     * @param idAthlete {int} Athlete ID
     * @param idTeam {int} Team ID
     * @param timeMS {int} Time in ms
     * @param medal {Medal} Medal
     */
    public WinnerOlympic(int idSport, int year, int idAthlete, int idTeam, int timeMS, Medal medal) {
        super(idSport, year, idAthlete, idTeam);
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
