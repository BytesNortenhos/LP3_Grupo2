package Models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.sql.Time;

@XmlRootElement(name = "olympicRecord")
@XmlAccessorType(XmlAccessType.FIELD)
public class OlympicRecordXML {
    @XmlElement
    private int year;
    @XmlElement
    private String holder;
    @XmlElement
    private String time;
    @XmlElement
    private int medals;

    /**
     * Constructor of OlympicRecord (without parameters)
     */
    public OlympicRecordXML() {}

    /**
     * Constructor of OlympicRecord
     * @param year {int} Year
     * @param holder {String} Holder
     * @param time {String} Time
     * @param medals {int} Medals
     */
    public OlympicRecordXML(int year, String holder, String time, int medals) {
        this.year = year;
        this.holder = holder;
        this.time = time;
        this.medals = medals;
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
     * @return String
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
     * Get medals
     * @return int
     */
    public int getMedals() {
        return medals;
    }

    /**
     * Set medals
     * @param medals {int} Medals
     */
    public void setMedals(int medals) {
        this.medals = medals;
    }
}
