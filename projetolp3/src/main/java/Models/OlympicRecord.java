package Models;

public class OlympicRecord extends RecordOrWinner {
    private int timeMS;
    private int medals;

    /**
     * Constructor of OlympicRecord
     * @param sport {Sport} Sport
     * @param year {int} Year
     * @param athlete {Athlete} Athlete
     * @param team {Team} Team
     * @param timeMS {int} Time in ms
     * @param medals {int} Number of medals
     */
    public OlympicRecord(Sport sport, int year, Athlete athlete, Team team, int timeMS, int medals) {
        super(sport, year, athlete, team);
        this.timeMS = timeMS;
        this.medals = medals;
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