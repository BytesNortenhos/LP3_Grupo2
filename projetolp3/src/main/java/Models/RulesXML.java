package Models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rules")
@XmlAccessorType(XmlAccessType.FIELD)
public class RulesXML {
    @XmlElement
    private String rule;

    /**
     * Constructor of Rules (without parameters)
     */
    public RulesXML() {}

    /**
     * Get rule
     * @return tring
     */
    public String getRule() {
        return rule;
    }

    /**
     * Set rule
     * @param rule {String} Rule
     */
    public void setRule(String rule) {
        this.rule = rule;
    }
}
