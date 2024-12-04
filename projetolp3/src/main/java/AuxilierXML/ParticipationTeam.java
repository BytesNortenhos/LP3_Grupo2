package AuxilierXML;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "participation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParticipationTeam {
    @XmlElement
    private int year;
    @XmlElement
    private String result;

    /**
     * Constructor of Participation (without parameters)
     */
    public ParticipationTeam() {}

    /**
     * Constructor of Participation
     * @param year {int} Year
     * @param result {String} Result
     */
    public ParticipationTeam(int year, String result) {
        this.year = year;
        this.result = result;
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
     * Get result
     * @return String
     */
    public String getResult() {
        return result;
    }

    /**
     * Set result
     * @param result {String} Result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Override toString method
     * @return String
     */
    @Override
    public String toString() {
        return "Year: " + year + "\nResult: " + result;
    }
}
