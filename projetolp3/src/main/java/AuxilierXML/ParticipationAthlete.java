package AuxilierXML;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "participation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParticipationAthlete {
    @XmlElement
    private int year;
    @XmlElement
    private int gold;
    @XmlElement
    private int silver;
    @XmlElement
    private int bronze;

    /**
     * Constructor of Participation (without parameters)
     */
    public ParticipationAthlete() {}

    /**
     * Constructor of Participation
     * @param year {int} Year
     * @param gold {int} Gold
     * @param silver {int} Silver
     * @param bronze {int} Bronze
     */
    public ParticipationAthlete(int year, int gold, int silver, int bronze) {
        this.year = year;
        this.gold = gold;
        this.silver = silver;
        this.bronze = bronze;
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
     * Get gold
     * @return int
     */
    public int getGold() {
        return gold;
    }

    /**
     * Set gold
     * @param gold {int} Gold
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Get silver
     * @return int
     */
    public int getSilver() {
        return silver;
    }

    /**
     * Set silver
     * @param silver {int} Silver
     */
    public void setSilver(int silver) {
        this.silver = silver;
    }

    /**
     * Get bronze
     * @return int
     */
    public int getBronze() {
        return bronze;
    }

    /**
     * Set bronze
     * @param bronze {int} Bronze
     */
    public void setBronze(int bronze) {
        this.bronze = bronze;
    }
    @Override
    public String toString() {
        return "Participação Olímpica{" +
                "Ano=" + year +
                ", Ouro=" + gold +
                ", Prata=" + silver +
                ", Bronze=" + bronze +
                '}';
    }

}