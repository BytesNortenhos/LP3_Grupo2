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
    private String xmlCountry;
    @XmlElement(name = "genre")
    private String xmlGenre;
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
     * Get olympic participations
     * @return List<ParticipationAthlete>
     */
    public List<ParticipationAthlete> getOlympicParticipations() {
        return olympicParticipations;
    }

    /**
     * Set olympic participations
     * @param olympicParticipations {List<ParticipationAthlete>} Olympic participations
     */
    public void setOlympicParticipations(List<ParticipationAthlete> olympicParticipations) {
        this.olympicParticipations = olympicParticipations;
    }

    /**
     * Get country
     * @return String
     */
    public String getXmlCountry() {
        return xmlCountry;
    }

    /**
     * Set country
     * @param xmlCountry {String} Country
     */
    public void setXmlCountry(String xmlCountry) {
        this.xmlCountry = xmlCountry;
    }

    /**
     * Get genre
     * @return String
     */
    public String getXmlGenre() {
        return xmlGenre;
    }

    /**
     * Set genre
     * @param xmlGenre {String} Genre
     */
    public void setXmlGenre(String xmlGenre) {
        this.xmlGenre = xmlGenre;
    }
    @Override
    public String toString() {
        // Formatando a data de nascimento em um formato legível
        String formattedDateOfBirth = (dateOfBirth != null) ? dateOfBirth.toString() : "N/A";

        // Formatando a lista de participações olímpicas, se existir
        StringBuilder olympicParticipationsString = new StringBuilder();
        if (olympicParticipations != null && !olympicParticipations.isEmpty()) {
            olympicParticipations.forEach(participation -> {
                olympicParticipationsString.append(participation.toString()).append("\n");
            });
        } else {
            olympicParticipationsString.append("Nenhuma participação olímpica registrada");
        }

        return String.format("Atleta: %s\n" +
                        "Nome: %s\n" +
                        "País: %s\n" +
                        "Gênero: %s\n" +
                        "Altura: %d cm\n" +
                        "Peso: %.2f kg\n" +
                        "Data de Nascimento: %s\n" +
                        "Participações Olímpicas: \n%s",
                super.toString(), // Chama o toString da classe pai (Person)
                name,
                xmlCountry,
                xmlGenre,
                height,
                weight,
                formattedDateOfBirth,
                olympicParticipationsString.toString());
    }

}
