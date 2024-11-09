package AuxilierXML;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "team")
@XmlAccessorType(XmlAccessType.FIELD)
public class Team {
    @XmlElement
    private String name;
    @XmlElement(name = "country")
    private String xmlCountry; // Needed for JAXB
    @XmlElement(name = "genre")
    private String xmlGenre;
    @XmlElement(name = "sport")
    private String xmlSport;
    @XmlElement(name = "foundationYear")
    private int yearFounded;
    @XmlElementWrapper(name = "olympicParticipations")
    @XmlElement(name = "participation")
    private List<ParticipationTeam> olympicParticipations; // Needed for JAXB

    /**
     * Constructor of Team (without parameters)
     */
    public Team() {}

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
     * Get year of foundation
     * @return int
     */
    public int getYearFounded() {
        return yearFounded;
    }

    /**
     * Set year of foundation
     * @param yearFounded {int} Year of foundation
     */
    public void setYearFounded(int yearFounded) {
        this.yearFounded = yearFounded;
    }

    /**
     * Get XML country (used only for JAXB)
     * @return String
     */
    public String getXmlCountry() {
        return xmlCountry;
    }

    /**
     * Set XML country (used only for JAXB)
     * @param xmlCountry {String} Country
     */
    public void setXmlCountry(String xmlCountry) {
        this.xmlCountry = xmlCountry;
    }

    /**
     * Get XML genre (used only for JAXB)
     * @return String
     */
    public String getXmlGenre() {
        return xmlGenre;
    }

    /**
     * Set XML genre (used only for JAXB)
     * @param xmlGenre {String} Genre
     */
    public void setXmlGenre(String xmlGenre) {
        this.xmlGenre = xmlGenre;
    }

    /**
     * Get XML sport (used only for JAXB)
     * @return String
     */
    public String getXmlSport() {
        return xmlSport;
    }

    /**
     * Set XML sport (used only for JAXB)
     * @param xmlSport {String} Sport
     */
    public void setXmlSport(String xmlSport) {
        this.xmlSport = xmlSport;
    }

    /**
     * Get olympic participations (used only for JAXB)
     * @return List<ParticipationTeam>
     */
    public List<ParticipationTeam> getOlympicParticipations() {
        return olympicParticipations;
    }

    /**
     * Set olympic participations (used only for JAXB)
     * @param olympicParticipations {List<ParticipationTeam>} Olympic participations
     */
    public void setOlympicParticipations(List<ParticipationTeam> olympicParticipations) {
        this.olympicParticipations = olympicParticipations;
    }
}