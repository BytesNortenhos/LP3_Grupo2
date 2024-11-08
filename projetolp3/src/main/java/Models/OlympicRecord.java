package Models;

public class OlympicRecord extends RecordOrWinner {
    private int timeMS;
    private int medals;

    /**
     * Constructor of OlympicRecord
     * @param idSport {int} Sport ID
     * @param year {int} Year
     * @param idAthlete {int} Athlete ID
     * @param idTeam {int} Team ID
     * @param timeMS {int} Time in ms
     * @param medals {int} Number of medals
     */
    public OlympicRecord(int idSport, int year, int idAthlete, int idTeam, int timeMS, int medals) {
        super(idSport, year, idAthlete, idTeam);
        this.timeMS = timeMS;
        this.medals = medals;
    }

    /**
     * Constructor of OlympicRecord (without parameters)
     */
    public OlympicRecord() {
        super();
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
     * @param timeMS {int} Tempo em milissegundos
     */
    public void setTimeMS(int timeMS) {
        this.timeMS = timeMS;
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
