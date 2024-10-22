package Models;

public class RecordOrWinner {
    private int id;
    private int year;
    private String holder;

    /**
     * Constructor of RecordOrWinner
     * @param id ID
     * @param year Year
     * @param holder Holder
     */
    public RecordOrWinner(int id, int year, String holder) {
        this.year = year;
        this.holder = holder;
    }

    /**
     * Get ID
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * Set ID
     * @param id {int} ID
     */
    public void setId(int id) {
        this.id = id;
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