package AuxilierXML;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "winnerOlympic")
@XmlAccessorType(XmlAccessType.FIELD)
public class WinnerOlympicXML {
    @XmlElement
    private int year;
    @XmlElement
    private String holder;
    @XmlElement
    private String time;
    @XmlElement
    private String medal;

    /**
     * Constructor of OlympicRecord (without parameters)
     */
    public WinnerOlympicXML() {}

    /**
     * Constructor of OlympicRecord
     * @param year {int} Year
     * @param holder {String} Holder
     * @param time {String} Time
     * @param medal {String} Medal
     */
    public WinnerOlympicXML(int year, String holder, String time, String medal) {
        this.year = year;
        this.holder = holder;
        this.time = time;
        this.medal = medal;
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

    /**
     * Get time
     * @return Time
     */
    public String getTime() {
        return time;
    }

    /**
     * Set time
     * @param time {String} Time
     */
    public void setTime(String time) {
        this.time = time;
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
