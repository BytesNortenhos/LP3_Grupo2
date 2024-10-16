package Models;

public class Participation {
    private int year;
    private int gold;
    private int silver;
    private int bronze;
    private int certificate;

    public Participation(int year, int gold, int silver, int bronze, int certificate) {
        this.year = year;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
        this.certificate = certificate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getSilver() {
        return silver;
    }

    public void setSilver(int silver) {
        this.silver = silver;
    }

    public int getBronze() {
        return bronze;
    }

    public void setBronze(int bronze) {
        this.bronze = bronze;
    }

    public int getCertificate() {
        return certificate;
    }

    public void setCertificate(int certificate) {
        this.certificate = certificate;
    }
}
