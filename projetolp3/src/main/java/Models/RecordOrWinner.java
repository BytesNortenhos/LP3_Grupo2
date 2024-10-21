package Models;

public class RecordOrWinner {
    private int year;
    private String holder;

    public RecordOrWinner(int year, String holder) {
        this.year = year;
        this.holder = holder;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }
}