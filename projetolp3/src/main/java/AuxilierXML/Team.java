package AuxilierXML;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "team")
@XmlAccessorType(XmlAccessType.FIELD)
public class Team {
    @XmlElement
    private String name;
    @XmlElement(name = "country")
    private String xmlCountry;
    @XmlElement(name = "genre")
    private String xmlGenre;
    @XmlElement(name = "sport")
    private String xmlSport;
    @XmlElement(name = "foundationYear")
    private int yearFounded;
    @XmlElementWrapper(name = "olympicParticipations")
    @XmlElement(name = "participation")
    private List<ParticipationTeam> olympicParticipations;

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
     * Get XML country
     * @return String
     */
    public String getXmlCountry() {
        return xmlCountry;
    }

    /**
     * Set XML country
     * @param xmlCountry {String} Country
     */
    public void setXmlCountry(String xmlCountry) {
        this.xmlCountry = xmlCountry;
    }

    /**
     * Get XML genre
     * @return String
     */
    public String getXmlGenre() {
        return xmlGenre;
    }

    /**
     * Set XML genre
     * @param xmlGenre {String} Genre
     */
    public void setXmlGenre(String xmlGenre) {
        this.xmlGenre = xmlGenre;
    }

    /**
     * Get XML sport
     * @return String
     */
    public String getXmlSport() {
        return xmlSport;
    }

    /**
     * Set XML sport
     * @param xmlSport {String} Sport
     */
    public void setXmlSport(String xmlSport) {
        this.xmlSport = xmlSport;
    }

    /**
     * Get olympic participations
     * @return List<ParticipationTeam>
     */
    public List<ParticipationTeam> getOlympicParticipations() {
        return olympicParticipations;
    }

    /**
     * Set olympic participations
     * @param olympicParticipations {List<ParticipationTeam>} Olympic participations
     */
    public void setOlympicParticipations(List<ParticipationTeam> olympicParticipations) {
        this.olympicParticipations = olympicParticipations;
    }

    /**
     * Override toString method
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(name).append("\n");
        sb.append("País: ").append(xmlCountry).append("\n");
        sb.append("Gênero: ").append(xmlGenre).append("\n");
        sb.append("Desporto: ").append(xmlSport).append("\n");
        sb.append("Ano Fundação: ").append(yearFounded).append("\n");

        if (olympicParticipations != null && !olympicParticipations.isEmpty()) {
            for (ParticipationTeam participation : olympicParticipations) {
                sb.append(participation.toString()).append("\n");
            }
        } else {
            sb.append("Sem Participações Olímpicas.\n");
        }
        sb.append("---------------------------\n");
        return sb.toString();
    }

}