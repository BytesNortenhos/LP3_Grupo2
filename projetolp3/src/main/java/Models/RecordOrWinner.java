package Models;

public class RecordOrWinner {
    private int year;
    private String holder;

    /**
     * Constructor of RecordOrWinner
     * @param year Year
     * @param holder Holder
     */
    public RecordOrWinner(int year, String holder) {
        this.year = year;
        this.holder = holder;
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
     * Get holder
     * @return String
     */
    public String getHolder() {
        return holder;
    }

    /**
     * Set holder
     * @param holder {String} Holder
     */
    public void setHolder(String holder) {
        this.holder = holder;
    }
}