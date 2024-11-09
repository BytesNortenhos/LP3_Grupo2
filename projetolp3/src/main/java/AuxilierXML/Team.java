package AuxilierXML;

import Models.Country;
import Models.Gender;
import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "team")
@XmlAccessorType(XmlAccessType.FIELD)
public class Team {
    private int idTeam;
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
    private List<ParticipationTeamXML> olympicParticipations; // Needed for JAXB
    private Country country;
    private Gender genre;
    private int idSport;

    /**
     * Constructor of Team
     * @param idTeam {int} Team ID
     * @param name {String} Name
     * @param country {Country} Country
     * @param genre {Gender} Genre
     * @param idSport {int} Sport
     * @param yearFounded {int} Year of foundation
     */
    public Team(int idTeam, String name, Country country, Gender genre, int idSport, int yearFounded) {
        this.idTeam = idTeam;
        this.name = name;
        this.country = country;
        this.genre = genre;
        this.idSport = idSport;
        this.yearFounded = yearFounded;
    }

    /**
     * Constructor of Team (without parameters)
     */
    public Team() {}

    /**
     * Get Team ID
     * @return int
     */
    public int getIdTeam() {
        return idTeam;
    }

    /**
     * Set Team ID
     * @param idTeam {int} Team ID
     */
    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
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
     * Get country
     * @return String
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Set country
     * @param country {String} Country
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * Get genre
     * @return Gender
     */
    public Gender getGenre() {
        return genre;
    }

    /**
     * Set genre
     * @param genre {Gender} Genre
     */
    public void setGenre(Gender genre) {
        this.genre = genre;
    }

    /**
     * Get Sport ID
     * @return int
     */
    public int getIdSport() {
        return idSport;
    }

    /**
     * Set sport
     * @param idSport {int} Sport DI
     */
    public void setIdSport(int idSport) {
        this.idSport = idSport;
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
    public List<ParticipationTeamXML> getOlympicParticipations() {
        return olympicParticipations;
    }

    /**
     * Set olympic participations (used only for JAXB)
     * @param olympicParticipations {List<ParticipationTeam>} Olympic participations
     */
    public void setOlympicParticipations(List<ParticipationTeamXML> olympicParticipations) {
        this.olympicParticipations = olympicParticipations;
    }
}