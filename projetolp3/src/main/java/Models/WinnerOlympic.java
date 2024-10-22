package Models;

public class WinnerOlympic extends RecordOrWinner {
    private int timeMS;
    private String medal;

    public WinnerOlympic(int year, String holder, int timeMS, String medal) {
        super(year, holder);
        this.timeMS = timeMS;
        this.medal = medal;
    }

    public int getTimeMS() {
        return timeMS;
    }

    public void setTimeMS(int timeMS) {
        this.timeMS = timeMS;
    }

    public String getMedal() {
        return medal;
    }

    public void setMedal(String medal) {
        this.medal = medal;
    }
}
