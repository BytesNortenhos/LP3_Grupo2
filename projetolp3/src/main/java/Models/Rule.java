package Models;

public class Rule {
    private int idRule;
    private Sport sport;
    private String desc;

    /**
     * Constructor of Rule
     * @param idRule {int} Rule ID
     * @param sport {Sport} Sport
     * @param desc {String} Description
     */
    public Rule(int idRule, Sport sport, String desc) {
        this.idRule = idRule;
        this.sport = sport;
        this.desc = desc;
    }

    /**
     * Get Rule ID
     * @return int
     */
    public int getIdRule() {
        return idRule;
    }

    /**
     * Set Rule ID
     * @param idRule {int} ID
     */
    public void setId(int idRule) {
        this.idRule = idRule;
    }

    /**
     * Get Sport
     * @return Sport
     */
    public Sport getSport() {
        return sport;
    }

    /**
     * Set Sport
     * @param sport {Sport} Sport
     */
    public void setSport(Sport sport) {
        this.sport = sport;
    }

    /**
     * Get Desc
     * @return String
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Set Desc
     * @param desc {String} Desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
