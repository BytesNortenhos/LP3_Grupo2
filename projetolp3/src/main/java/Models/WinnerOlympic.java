package Models;

public class WinnerOlympic extends RecordOrWinner {
    private int result;
    private Medal medal;

    /**
     * Constructor of WinnerOlympic
     * @param sport {Sport} Sport
     * @param year {int} Year
     * @param athlete {Athlete} Athlete
     * @param team {Team} Team
     * @param result {int} Time in ms
     * @param medal {Medal} Medal
     */
    public WinnerOlympic(Sport sport, int year, Athlete athlete, Team team, int result, Medal medal) {
        super(sport, year, athlete, team);
        this.result = result;
        this.medal = medal;
    }
    /**
     * New Constructor of WinnerOlympic with IDs instead of objects
     * @param idSport {int} Sport ID
     * @param year {int} Year
     * @param idAthlete {int} Athlete ID
     * @param idTeam {int} Team ID
     * @param result {int} Time in milliseconds
     * @param medal {Medal} Medal
     */
    public WinnerOlympic(int idSport, int year, int idAthlete, int idTeam, int result, Medal medal) {
        super(idSport, year, idAthlete, idTeam);
        this.result = result;
        this.medal = medal;
    }
    /**
     * Get time in ms
     * @return int
     */
    public int getresult() {
        return result;
    }

    /**
     * Set time in ms
     * @param result {int} Time in ms
     */
    public void setresult(int result) {
        this.result = result;
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