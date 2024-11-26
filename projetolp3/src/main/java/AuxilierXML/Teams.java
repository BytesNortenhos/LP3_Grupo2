package AuxilierXML;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "teams")
@XmlAccessorType(XmlAccessType.FIELD)
public class Teams {
    @XmlElement(name = "team")  // Isso garante que o JAXB reconheça as teams no XML
    private List<Team> teamList;

    /**
     * Constructor of Teams (without parameters)
     */
    public Teams() {}

    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (teamList != null && !teamList.isEmpty()) {
            for (Team team : teamList) {
                sb.append(team.toString()).append("\n");
            }
        } else {
            sb.append("No teams available.\n");
        }

        return sb.toString();
    }

}
