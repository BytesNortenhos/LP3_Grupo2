package Models;

public class Participation {
    private int year;
    private int gold;
    private int silver;
    private int bronze;
    private int certificate;

    /**
     * Constructor of Participation
     * @param year {int} Year
     * @param gold {int} Number of gold medals
     * @param silver {int} Number of silver medals
     * @param bronze {int} Number of bronze medals
     * @param certificate {int} Number of certificates
     */
    public Participation(int year, int gold, int silver, int bronze, int certificate) {
        this.year = year;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
        this.certificate = certificate;
    }

    public Participation() {}

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
     * Get number of gold medals
     * @return int
     */
    public int getGold() {
        return gold;
    }

    /**
     * Set number of gold medals
     * @param gold {int} Number of gold medals
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Get number of silver medals
     * @return int
     */
    public int getSilver() {
        return silver;
    }

    /**
     * Set number of silver medals
     * @param silver {int} Number of silver medals
     */
    public void setSilver(int silver) {
        this.silver = silver;
    }

    /**
     * Get number of bronze medals
     * @return int
     */
    public int getBronze() {
        return bronze;
    }

    /**
     * Set number of bronze medals
     * @param bronze {int} Number of bronze medals
     */
    public void setBronze(int bronze) {
        this.bronze = bronze;
    }

    /**
     * Get number of certificates
     * @return int
     */
    public int getCertificate() {
        return certificate;
    }

    /**
     * Get number of certificates
     * @param certificate {int} Number of certificates
     */
    public void setCertificate(int certificate) {
        this.certificate = certificate;
    }
}
