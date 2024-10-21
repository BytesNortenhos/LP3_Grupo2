package Models;

public class WinnerOlympic extends RecordOrWinner {
    private int timeMS;
    private String medal;

    /**
     * Constructor of WinnerOlympic
     * @param year {int} Year
     * @param holder {String} Holder
     * @param timeMS {int} Time in ms
     * @param medal {String} Medal
     */
    public WinnerOlympic(int year, String holder, int timeMS, String medal) {
        super(year, holder);
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
     * @return String
     */
    public String getMedal() {
        return medal;
    }

    /**
     * Set medal
     * @param medal {String} Medal
     */
    public void setMedal(String medal) {
        this.medal = medal;
    }
}
