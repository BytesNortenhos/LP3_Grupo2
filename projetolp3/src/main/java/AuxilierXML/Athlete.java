package AuxilierXML;

import jakarta.xml.bind.annotation.*;

import java.util.Date;
import java.util.List;

@XmlRootElement(name = "athlete")
@XmlAccessorType(XmlAccessType.FIELD)
public class Athlete extends Person {
    @XmlElement
    private String name;
    @XmlElement(name = "country")
    private String xmlCountry; // Needed for JAXB
    @XmlElement(name = "genre")
    private String xmlGenre; // Needed for JAXB
    @XmlElement
    private int height;
    @XmlElement
    private float weight;
    @XmlElement
    private Date dateOfBirth;
    @XmlElementWrapper(name = "olympicParticipations")
    @XmlElement(name = "participation")
    private List<ParticipationAthlete> olympicParticipations; // Needed for JAXB

    /**
     * Constructor of Athlete
     * @param id {int} ID
     * @param password {String} Password
     * @param name {String} Name
     * @param height {int} Height
     * @param weight {float} Weight
     * @param dateOfBirth {Date} Date of birth
     */
    public Athlete(int id, String password, String name, int height, float weight, Date dateOfBirth) {
        super(id, password);
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Constructor of Athlete (without parameters)
     */
    public Athlete() {
        super();
    }

    /**
     * Get name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Set name
     * @param name {String} Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get height
     * @return int
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set height
     * @param height {int} Height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Get weight
     * @return float
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Set weight
     * @param weight {float} Weight
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Get date of birth
     *
     * @return Date
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Set date of birth
     * @param dateOfBirth {Date} Date of birth
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Get olympic participations (used only for JAXB)
     * @return List<ParticipationAthlete>
     */
    public List<ParticipationAthlete> getOlympicParticipations() {
        return olympicParticipations;
    }

    /**
     * Set olympic participations (used only for JAXB)
     * @param olympicParticipations {List<ParticipationAthlete>} Olympic participations
     */
    public void setOlympicParticipations(List<ParticipationAthlete> olympicParticipations) {
        this.olympicParticipations = olympicParticipations;
    }

    /**
     * Get country (used only for JAXB)
     * @return String
     */
    public String getXmlCountry() {
        return xmlCountry;
    }

    /**
     * Set country (used only for JAXB)
     * @param xmlCountry {String} Country
     */
    public void setXmlCountry(String xmlCountry) {
        this.xmlCountry = xmlCountry;
    }

    /**
     * Get genre (used only for JAXB)
     * @return String
     */
    public String getXmlGenre() {
        return xmlGenre;
    }

    /**
     * Set genre (used only for JAXB)
     * @param xmlGenre {String} Genre
     */
    public void setXmlGenre(String xmlGenre) {
        this.xmlGenre = xmlGenre;
    }
}
