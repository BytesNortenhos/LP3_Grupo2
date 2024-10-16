package Models;

public class Record {
    private String time;
    private int year;
    private String holder;

    public Record(String time, int year, String holder) {
        this.time = time;
        this.year = year;
        this.holder = holder;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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