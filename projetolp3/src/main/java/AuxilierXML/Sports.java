package AuxilierXML;

import Models.Sport;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "sports")
@XmlAccessorType(XmlAccessType.FIELD)
public class Sports {
    @XmlElement(name = "sport")  // Isso garante que o JAXB reconheça as sports no XML
    private List<Sport> sportList;

    /**
     * Constructor of Sports (without parameters)
     */
    public Sports() {}

    public List<Sport> getSportList() {
        return sportList;
    }

    public void setSportList(List<Sport> sportList) {
        this.sportList = sportList;
    }
}
