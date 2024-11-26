package Models;

public class OlympicRecord extends RecordOrWinner {
    private int result;
    private int medals;

    /**
     * Constructor of OlympicRecord
     * @param sport {Sport} Sport
     * @param year {int} Year
     * @param athlete {Athlete} Athlete
     * @param team {Team} Team
     * @param result {int} Time in ms
     * @param medals {int} Number of medals
     */
    public OlympicRecord(Sport sport, int year, Athlete athlete, Team team, int result, int medals) {
        super(sport, year, athlete, team);
        this.result = result;
        this.medals = medals;
    }

    public OlympicRecord(int idSport, int year, int idAthlete, int idTeam, int result, int medals) {
        super(idSport, year, idAthlete, idTeam);  // Calls the constructor in RecordOrWinner with IDs
        this.result = result;
        this.medals = medals;
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
     * @param result {int} Tempo em milissegundos
     */
    public void setresult(int result) {
        this.result = result;
    }

    /**
     * Get medals
     * @return int
     */
    public int getMedals() {
        return medals;
    }

    /**
     * Set number of medals
     * @param medals {int} Number of medals
     */
    public void setMedals(int medals) {
        this.medals = medals;
    }
}