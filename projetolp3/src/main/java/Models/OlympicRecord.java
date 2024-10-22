package Models;

public class OlympicRecord extends RecordOrWinner {
    private int timeMS;
    private int medals;

    public OlympicRecord(int year, String holder, int timeMS, int medals) {
        super(year, holder);
        this.timeMS = timeMS;
        this.medals = medals;
    }

    public int getTimeMS() {
        return timeMS;
    }

    public void setTimeMS(int timeMS) {
        this.timeMS = timeMS;
    }

    public int getMedals() {
        return medals;
    }

    public void setMedals(int medals) {
        this.medals = medals;
    }
}
