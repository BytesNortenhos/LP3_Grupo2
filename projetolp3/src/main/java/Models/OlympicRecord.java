package Models;

public class OlympicRecord extends RecordOrWinner {
    private int timeMS;
    private int medals;

    /**
     * Constructor of OlympicRecord
     * @param year {int} Year
     * @param holder {String} Holder
     * @param timeMS {int} Time in ms
     * @param medals {int} Number of medals
     */
    public OlympicRecord(int year, String holder, int timeMS, int medals) {
        super(year, holder);
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
