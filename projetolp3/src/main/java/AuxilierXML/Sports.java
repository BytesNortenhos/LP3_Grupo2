package AuxilierXML;

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
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Lista de Desportos:\n");

        if (sportList != null && !sportList.isEmpty()) {
            for (Sport sport : sportList) {
                sb.append("  - ").append(sport.toString()).append("\n");
            }
        } else {
            sb.append("Nenhum desporto encontrado.\n");
        }

        return sb.toString();
    }

}