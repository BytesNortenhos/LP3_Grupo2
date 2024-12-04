package AuxilierXML;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rules")
@XmlAccessorType(XmlAccessType.FIELD)
public class Rules {
    @XmlElement
    private String rule;

    /**
     * Constructor of Rules (without parameters)
     */
    public Rules() {}

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

    /**
     * Override toString method
     * @return String
     */
    @Override
    public String toString() {
        return String.format("Regra: %s", rule);
    }

}
