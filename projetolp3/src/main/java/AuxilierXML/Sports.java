package AuxilierXML;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "sports")
@XmlAccessorType(XmlAccessType.FIELD)
public class Sports {
    @XmlElement(name = "sport")
    private List<Sport> sportList;

    /**
     * Constructor of Sports (without parameters)
     */
    public Sports() {}

    /**
     * Get sport list
     * @return List<Sport>
     */
    public List<Sport> getSportList() {
        return sportList;
    }

    /**
     * Set sport list
     * @param sportList {List<Sport>} List of sports
     */
    public void setSportList(List<Sport> sportList) {
        this.sportList = sportList;
    }
}
