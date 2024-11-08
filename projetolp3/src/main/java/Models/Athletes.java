package Models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "athletes")
@XmlAccessorType(XmlAccessType.FIELD)
public class Athletes {
    @XmlElement(name = "athlete")  // Isso garante que o JAXB reconheça os atletas no XML
    private List<Athlete> athleteList;

    /**
     * Constructor of Athletes (without parameters)
     */
    public Athletes() {}

    /**
     * Get athlete list
     * @return List<Athlete>
     */
    public List<Athlete> getAthleteList() {
        return athleteList;
    }

    /**
     * Set athlete list
     * @param athleteList {List<Athlete>} List of athletes
     */
    public void setAthleteList(List<Athlete> athleteList) {
        this.athleteList = athleteList;
    }
}
